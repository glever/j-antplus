package be.glever.ant.usb;

import be.glever.ant.AntException;
import be.glever.ant.channel.AntChannel;
import be.glever.ant.message.AntBlockingMessage;
import be.glever.ant.message.AntMessage;
import be.glever.ant.message.channel.ChannelEventOrResponseMessage;
import be.glever.ant.message.channel.ChannelEventResponseCode;
import be.glever.ant.message.configuration.*;
import be.glever.ant.message.control.OpenChannelMessage;
import be.glever.ant.message.control.RequestMessage;
import be.glever.ant.message.control.ResetSystemMessage;
import be.glever.ant.message.requestedresponse.AntVersionMessage;
import be.glever.ant.message.requestedresponse.CapabilitiesResponseMessage;
import be.glever.ant.message.requestedresponse.ChannelStatusMessage;
import be.glever.ant.message.requestedresponse.SerialNumberMessage;
import be.glever.ant.util.ByteUtils;
import be.glever.util.logging.Log;
import org.reactivestreams.Publisher;
import reactor.core.publisher.*;
import reactor.core.scheduler.Schedulers;

import javax.usb.*;
import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

import static java.lang.String.format;

/**
 * Provides abstraction of and interaction with an ANT USB dongle.
 * Clients can
 */
public class AntUsbDevice implements Closeable {
    private static final int SLEEP_AFTER_RESET = 500;
    private static final long DEFAULT_TIMEOUT = 1000;
    private static Log LOG = Log.getLogger(AntUsbDevice.class);
    private final FluxProcessor<AntMessage, AntMessage> antMessageProcessor = DirectProcessor.<AntMessage>create().serialize();
    private final FluxSink<AntMessage> antMessageSink = antMessageProcessor.sink();
    private final FluxProcessor<Throwable, Throwable> errorProcessor = DirectProcessor.<Throwable>create().serialize();
    private final FluxSink<Throwable> errorSink = errorProcessor.sink();
    private UsbDevice device;


    private byte[] antVersion;
    private AntUsbDeviceCapabilities capabilities;
    private byte[] serialNumber;
    private AntUsbReader antUsbReader;
    private AntUsbWriter antUsbWriter;
    private AntChannel[] antChannels;


    public AntUsbDevice(UsbDevice device) {
        this.device = device;
    }

    private static boolean forceClaim(UsbInterface usbInterface1) {
        return true;
    }

    private static Publisher<AntMessage> mapToErrorIfRequired(AntMessage antMessage) {
        LOG.debug(() -> format("in flux, received message %s", antMessage));
        if (antMessage instanceof ChannelEventOrResponseMessage) {
            ChannelEventOrResponseMessage channelEventOrResponseMessage = (ChannelEventOrResponseMessage) antMessage;
            if (channelEventOrResponseMessage.getResponseCode() != ChannelEventResponseCode.RESPONSE_NO_ERROR) {
                return Flux.error(new AntException(antMessage));
            }
        }
        return Flux.just(antMessage);
    }

    public void initialize() throws AntException {

        try {
            initUsbInterface();
            initAntDevice();
        } catch (Exception e) {
            throw new AntException(e);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            resetUsbDevice();
        } catch (Exception e) {
            LOG.error(() -> "Reset ant stick failed. Continuing with shutdown of usb interface.", e);
        }

        try {
            this.antUsbReader.stop();
            UsbInterface activeUsbInterface = getActiveUsbInterface();
            if (activeUsbInterface.isClaimed()) {
                activeUsbInterface.release();
            }
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    public void closeAllChannels() throws AntException {
        short maxChannels = capabilities.getMaxChannels();
        for (int i = 0; i < maxChannels; i++) {
            RequestMessage requestMessage = new RequestMessage((byte) i, ChannelStatusMessage.MSG_ID);
            ChannelStatusMessage responseMessage = (ChannelStatusMessage) sendBlocking(requestMessage);
            byte channelNumber = responseMessage.getChannelNumber();
            ChannelStatusMessage.CHANNEL_STATUS channelStatus = responseMessage.getChannelStatus();
            LOG.debug(() -> format("Channel %s is in state %s.", channelNumber, channelStatus));

            switch (channelStatus) {
                case UnAssigned:
                    break;
                case Assigned:
                    closeChannel(channelNumber);
                    break;
                default:
                    throw new AntException("Don't know (yet) how to close channel in current state " + channelStatus + ".");
            }
        }
    }

    public AntUsbDeviceCapabilities getCapabilities() {
        return capabilities;
    }

    public void openChannel(AntChannel channel) {

        byte channelNumber = getAvailableChannelNumber();

        sendBlocking(new NetworkKeyMessage(channelNumber, channel.getNetwork().getNetworkKey()));
        sendBlocking(new AssignChannelMessage(channelNumber, channel.getChannelType().getValue(), channel.getNetwork().getNetworkNumber()));
        sendBlocking(new ChannelIdMessage(channelNumber, channel.getChannelId().getDeviceNumber(), channel.getChannelId().getDeviceType(), channel.getChannelId().getTransmissionType().getValue()));
        sendBlocking(new ChannelPeriodMessage(channelNumber, channel.getChannelPeriod()));
        sendBlocking(new ChannelRfFrequencyMessage(channelNumber, channel.getRfFrequency()));
        sendBlocking(new OpenChannelMessage(channelNumber));

        this.antChannels[channelNumber] = channel;
        channel.setChannelNumber(channelNumber);
        channel.subscribeTo(this.antMessageProcessor);
    }

    public AntMessage sendBlocking(AntBlockingMessage requestMessage) {
        Flux<AntMessage> response = this.antMessageProcessor
                .filter(responseMessage -> RequestMatcher.isMatchingResponse(requestMessage, responseMessage))
                .concatMap(AntUsbDevice::mapToErrorIfRequired)
                .take(1);

        Mono<Void> messageSender = Mono.fromRunnable(() -> this.antUsbWriter.write(requestMessage));
        return (AntMessage) Flux.merge(response, messageSender).blockFirst(Duration.ofSeconds(10));
    }

    private byte getAvailableChannelNumber() {
        for (int i = 0; i < antChannels.length; i++) {
            if (antChannels[i] == null) {
                return (byte) i;
            }
        }
        throw new IllegalStateException("No available channels found");
    }

    private void initUsbInterface() throws AntException, UsbException {
        UsbInterface usbInterface = getActiveUsbInterface();
        if (usbInterface.isClaimed()) {
            throw new AntException("Usb device already claimed");
        }

        usbInterface.claim(AntUsbDevice::forceClaim);

        @SuppressWarnings("unchecked")
        List<UsbEndpoint> usbEndpoints = usbInterface.getUsbEndpoints();
        UsbEndpoint inEndpoint = usbEndpoints.stream()
                .filter(endpoint -> endpoint.getDirection() == UsbConst.ENDPOINT_DIRECTION_IN)
                .findAny()
                .orElseThrow(() -> new IllegalStateException("Could not obtain usb input pipe."));
        UsbEndpoint outEndpoint = usbEndpoints.stream()
                .filter(endpoint -> endpoint.getDirection() == UsbConst.ENDPOINT_DIRECTION_OUT)
                .findAny()
                .orElseThrow(() -> new IllegalStateException("Could not obtain usb output pipe."));

        UsbPipe usbOutPipe = outEndpoint.getUsbPipe();
        usbOutPipe.open();

        UsbPipe inPipe = inEndpoint.getUsbPipe();
        inPipe.open();

        antUsbReader = new AntUsbReader(inPipe);
        new Thread(antUsbReader, "ant-usb-reader").start();
        antUsbWriter = new AntUsbWriter(usbOutPipe);

        subscribeTo(antUsbReader);
    }

    private void subscribeTo(AntUsbReader antUsbMessageReader) {
        antUsbMessageReader
                .antMessages()
                .publishOn(Schedulers.elastic())
                .doOnNext(this.antMessageSink::next)
                .doOnError(this.errorSink::next)
                .subscribe();

        this.errorProcessor.doOnNext(
                error -> LOG.error(() -> "TODO Handle error " + error))
                .subscribe();
    }

    private void initAntDevice() throws AntException, InterruptedException {
        resetUsbDevice();

        CapabilitiesResponseMessage capabilitiesResponseMessage = (CapabilitiesResponseMessage) sendBlocking(RequestMessage.forMessageId(CapabilitiesResponseMessage.MSG_ID));
        this.capabilities = new AntUsbDeviceCapabilities(capabilitiesResponseMessage);
        this.antChannels = new AntChannel[capabilities.getMaxChannels()];

        AntVersionMessage antVersionMessage = (AntVersionMessage) sendBlocking(RequestMessage.forMessageId(AntVersionMessage.MSG_ID));
        this.antVersion = antVersionMessage.getAntVersion();

        SerialNumberMessage serialNumberMessage = (SerialNumberMessage) sendBlocking(RequestMessage.forMessageId(SerialNumberMessage.MSG_ID));
        this.serialNumber = serialNumberMessage.getSerialNumber();

        LOG.debug(() -> format("Capabilities: %s", this.capabilities.toString()));
        LOG.debug(() -> format("Ant Version: %s", ByteUtils.hexString(this.antVersion)));
        LOG.debug(() -> format("SerialNumber: %s", ByteUtils.hexString(this.serialNumber)));
    }

    private void resetUsbDevice() throws InterruptedException {
        Thread.sleep(SLEEP_AFTER_RESET);
        antUsbWriter.write(new ResetSystemMessage());
        Thread.sleep(SLEEP_AFTER_RESET);
    }

    private UsbInterface getActiveUsbInterface() {
        return (UsbInterface) this.device.getActiveUsbConfiguration().getUsbInterfaces().get(0);
    }

    private void closeChannel(byte channelNumber) throws AntException {
        UnassignChannelMessage unassignChannelMessage = new UnassignChannelMessage(channelNumber);
        ChannelEventOrResponseMessage unassignResponseMsg = (ChannelEventOrResponseMessage) sendBlocking(unassignChannelMessage);
        if (unassignResponseMsg.getResponseCode() != ChannelEventResponseCode.RESPONSE_NO_ERROR) {
            LOG.debug(() -> format("Received unexpected message %s. Halting program.", unassignResponseMsg));
            throw new AntException("Could not close channel " + channelNumber);
        }
        LOG.debug(() -> format("Successfully unassigned channel %s.", channelNumber));
    }

    public void send(AntMessage antMessage) {
        antUsbWriter.write(antMessage);
    }


    public byte[] getAntVersion() {
        return antVersion;
    }

    public byte[] getSerialNumber() {
        return serialNumber;
    }
}
