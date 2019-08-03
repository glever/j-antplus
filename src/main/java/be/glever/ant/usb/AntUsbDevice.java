package be.glever.ant.usb;

import be.glever.ant.AntException;
import be.glever.ant.channel.AntChannel;
import be.glever.ant.message.AbstractAntMessage;
import be.glever.ant.message.AntBlockingMessage;
import be.glever.ant.message.AntMessage;
import be.glever.ant.message.channel.ChannelEventOrResponseMessage;
import be.glever.ant.message.channel.ChannelEventResponseCode;
import be.glever.ant.message.configuration.*;
import be.glever.ant.message.control.OpenChannelMessage;
import be.glever.ant.message.control.RequestMessage;
import be.glever.ant.message.control.ResetSystemMessage;
import be.glever.ant.message.data.BroadcastDataMessage;
import be.glever.ant.message.requestedresponse.AntVersionMessage;
import be.glever.ant.message.requestedresponse.CapabilitiesResponseMessage;
import be.glever.ant.message.requestedresponse.ChannelStatusMessage;
import be.glever.ant.message.requestedresponse.SerialNumberMessage;
import be.glever.ant.messagebus.MessageBusListener;
import be.glever.ant.util.ByteUtils;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.*;
import reactor.core.scheduler.Schedulers;

import javax.usb.*;
import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Provides abstraction of and interaction with an ANT USB dongle.
 * Clients can
 */
public class AntUsbDevice implements Closeable {
    public static final int SLEEP_AFTER_RESET = 500;
    private static Logger LOG = LoggerFactory.getLogger(AntUsbDevice.class);
    private static final long DEFAULT_TIMEOUT = 1000;

    private boolean initialized = false;
    private UsbDevice device;

    private byte[] antVersion;
    private AntUsbDeviceCapabilities capabilities;
    private byte[] serialNumber;
    private AntUsbReader antUsbReader;
    private AntUsbWriter antUsbWriter;
    private AntChannel[] antChannels;
    private final FluxProcessor<AntMessage, AntMessage> antMessageProcessor = DirectProcessor.<AntMessage>create().serialize();
    private final FluxSink<AntMessage> antMessageSink = antMessageProcessor.sink();
    private final FluxProcessor<Throwable, Throwable> errorProcessor = DirectProcessor.<Throwable>create().serialize();
    private final FluxSink<Throwable> errorSink = errorProcessor.sink();

    /**
     * Constructor. Note that clients should still call the {@link #initialize()} method.
     *
     * @param device
     */
    public AntUsbDevice(UsbDevice device) {
        this.device = device;
    }

    private static boolean forceClaim(UsbInterface usbInterface1) {
        return true;
    }


    /**
     * Opens a connection to the ant usb device and reads the basic info.
     *
     * @throws AntException
     */
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
            LOG.error("Reset ant stick failed. Continuing with shutdown of usb interface.", e);
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

    public void closeAllChannels() throws AntException, ExecutionException, InterruptedException {
        short maxChannels = capabilities.getMaxChannels();
        for (int i = 0; i < maxChannels; i++) {
            RequestMessage requestMessage = new RequestMessage((byte) i, ChannelStatusMessage.MSG_ID);
            ChannelStatusMessage responseMessage = (ChannelStatusMessage) sendBlocking(requestMessage);
            byte channelNumber = responseMessage.getChannelNumber();
            ChannelStatusMessage.CHANNEL_STATUS channelStatus = responseMessage.getChannelStatus();
            LOG.debug("Channel {} is in state {}.", channelNumber, channelStatus);

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
    }


    public AntMessage sendBlocking(AntBlockingMessage requestMessage) {
        Flux<AntMessage> response = this.antUsbReader.antMessages()
                .filter(responseMessage -> RequestMatcher.isMatchingResponse(requestMessage, responseMessage))
                .concatMap(AntUsbDevice::mapToErrorIfRequired)
                .take(1);

        Mono<Void> messageSender = Mono.fromRunnable(() -> this.antUsbWriter.write(requestMessage));
        return (AntMessage) Flux.merge(response, messageSender).blockFirst(Duration.ofSeconds(10));
    }


    private static Publisher<AntMessage> mapToErrorIfRequired(AntMessage antMessage) {
        LOG.debug("in flux, received message {}", antMessage);
        if (antMessage instanceof ChannelEventOrResponseMessage) {
            ChannelEventOrResponseMessage channelEventOrResponseMessage = (ChannelEventOrResponseMessage) antMessage;
            if (channelEventOrResponseMessage.getResponseCode() != ChannelEventResponseCode.RESPONSE_NO_ERROR) {
                return Flux.error(new AntException(antMessage));
            }
        }
        return Flux.just(antMessage);
    }

    public Byte getAvailableChannelNumber() {
        for (int i = 0; i < antChannels.length; i++) {
            if (antChannels[i] == null) {
                return (byte) i;
            }
        }
        return null;
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
                .filter(endpoint -> endpoint.getDirection() == UsbConst.ENDPOINT_DIRECTION_IN).findAny().get();
        UsbEndpoint outEndpoint = usbEndpoints.stream()
                .filter(endpoint -> endpoint.getDirection() == UsbConst.ENDPOINT_DIRECTION_OUT).findAny().get();

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

        this.antMessageProcessor
                .doOnNext(antMessage -> {
                    LOG.debug("Sending message to global messagebus listener: {}", antMessage);
                    new GlobalMessageBusListener().handle(antMessage);
                })
                .subscribe();

        this.errorProcessor.doOnNext(
                error -> {
                    LOG.error("TODO Handle error " + error);
                })
                .subscribe();
    }

    private void initAntDevice() throws AntException, InterruptedException, ExecutionException {
        resetUsbDevice();

        CapabilitiesResponseMessage capabilitiesResponseMessage = (CapabilitiesResponseMessage) sendBlocking(RequestMessage.forMessageId(CapabilitiesResponseMessage.MSG_ID));
        this.capabilities = new AntUsbDeviceCapabilities(capabilitiesResponseMessage);
        this.antChannels = new AntChannel[capabilities.getMaxChannels()];

        AntVersionMessage antVersionMessage = (AntVersionMessage) sendBlocking(RequestMessage.forMessageId(AntVersionMessage.MSG_ID));
        this.antVersion = antVersionMessage.getAntVersion();

        SerialNumberMessage serialNumberMessage = (SerialNumberMessage) sendBlocking(RequestMessage.forMessageId(SerialNumberMessage.MSG_ID));
        this.serialNumber = serialNumberMessage.getSerialNumber();

        LOG.debug("Capabilities: {}", this.capabilities.toString());
        LOG.debug("Ant Version: {}", ByteUtils.hexString(this.antVersion));
        LOG.debug("SerialNumber: {}", ByteUtils.hexString(this.serialNumber));
    }

    private void resetUsbDevice() throws InterruptedException {
        Thread.sleep(SLEEP_AFTER_RESET);
        antUsbWriter.write(new ResetSystemMessage());
        Thread.sleep(SLEEP_AFTER_RESET);
    }

    private UsbInterface getActiveUsbInterface() {
        return (UsbInterface) this.device.getActiveUsbConfiguration().getUsbInterfaces().get(0);
    }

    private void closeChannel(byte channelNumber) throws InterruptedException, ExecutionException, AntException {
        UnassignChannelMessage unassignChannelMessage = new UnassignChannelMessage(channelNumber);
        ChannelEventOrResponseMessage unassignResponseMsg = (ChannelEventOrResponseMessage) sendBlocking(unassignChannelMessage);
        if (unassignResponseMsg.getResponseCode() != ChannelEventResponseCode.RESPONSE_NO_ERROR) {
            LOG.debug("Received unexpected message {}. Halting program.", unassignResponseMsg);
            throw new AntException("Could not close channel " + channelNumber);
        }
        LOG.debug("Successfully unassigned channel {}.", channelNumber);
    }

    public void send(AntMessage antMessage) {
        antUsbWriter.write(antMessage);
    }

    private class GlobalMessageBusListener {

        public boolean handle(AntMessage antMessage) {
            LOG.debug("GlobalMessageListener: Received {}", antMessage.toString());

            if (antMessage instanceof ChannelEventOrResponseMessage) {
                ChannelEventOrResponseMessage msg = (ChannelEventOrResponseMessage) antMessage;
                byte channelNr = msg.getChannelNumber();
                notifyChannel(channelNr, msg);

                if (msg.getResponseCode() == ChannelEventResponseCode.EVENT_CHANNEL_CLOSED) {
                    AntUsbDevice.this.antChannels[channelNr] = null;
                }

            } else if (antMessage instanceof BroadcastDataMessage) {
                BroadcastDataMessage msg = (BroadcastDataMessage) antMessage;
                byte channelNumber = msg.getChannelNumber();

                notifyChannel(channelNumber, msg);
            }
            return true;
        }

        private void notifyChannel(byte channelNumber, AntMessage msg) {
            AntChannel antChannel = getAntChannel(channelNumber);
            if (antChannel != null) {
                antChannel.handle(msg);
            }
        }

        private AntChannel getAntChannel(byte channelNr) {
            if (AntUsbDevice.this.antChannels == null) {
                return null; // can occur @ init time after attaching listener but before acquiring capabilities.
            }
            return AntUsbDevice.this.antChannels[channelNr];
        }
    }

}
