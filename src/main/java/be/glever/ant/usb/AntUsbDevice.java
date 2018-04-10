package be.glever.ant.usb;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;
import be.glever.ant.message.AntBlockingMessage;
import be.glever.ant.message.AntMessage;
import be.glever.ant.message.channel.ChannelEventOrResponseMessage;
import be.glever.ant.message.channel.ChannelEventResponseCode;
import be.glever.ant.message.configuration.UnassignChannelMessage;
import be.glever.ant.message.control.RequestMessage;
import be.glever.ant.message.requestedresponse.AntVersionMessage;
import be.glever.ant.message.requestedresponse.CapabilitiesResponseMessage;
import be.glever.ant.message.requestedresponse.ChannelStatusMessage;
import be.glever.ant.message.requestedresponse.SerialNumberMessage;
import be.glever.ant.messagebus.MessageBusListener;
import be.glever.ant.util.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.usb.*;
import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Provides abstraction of and interaction with an ANT USB dongle.
 * Clients can
 */
public class AntUsbDevice implements Closeable {
	private static Logger LOG = LoggerFactory.getLogger(AntUsbDevice.class);
	private static final long DEFAULT_TIMEOUT = 1000;

	private boolean initialized = false;
	private UsbDevice device;

	private UsbPipe outPipe;

	private byte[] antVersion;
	private AntUsbDeviceCapabilities capabilities;
	private byte[] serialNumber;
	private AntUsbMessageReader antMessageUsbReader;

	/**
	 * Constructor. Note that clients should still call the {@link #initialize()} method.
	 * @param device
	 */
	public AntUsbDevice(UsbDevice device) {
		LOG.info("new device");
		this.device = device;
	}


	/**
	 * Opens a connection to the ant usb device and reads the basic info.
	 * @throws AntException
	 */
	public void initialize() throws AntException {

		try {
			UsbInterface usbInterface = getActiveUsbInterface();
			if (usbInterface.isClaimed()) {
				throw new AntException("Usb device already claimed");
			}

			usbInterface.claim();

			@SuppressWarnings("unchecked")
			List<UsbEndpoint> usbEndpoints = usbInterface.getUsbEndpoints();
			UsbEndpoint inEndpoint = usbEndpoints.stream()
					.filter(endpoint -> endpoint.getDirection() == UsbConst.ENDPOINT_DIRECTION_IN).findAny().get();
			UsbEndpoint outEndpoint = usbEndpoints.stream()
					.filter(endpoint -> endpoint.getDirection() == UsbConst.ENDPOINT_DIRECTION_OUT).findAny().get();

			outPipe = outEndpoint.getUsbPipe();
			outPipe.open();

			UsbPipe inPipe = inEndpoint.getUsbPipe();
			inPipe.open();


			antMessageUsbReader = new AntUsbMessageReader(inPipe);
			antMessageUsbReader.addQueueListener(-1, -1, new GlobalMessageBusListener());
			new Thread(antMessageUsbReader).start();

			CompletableFuture<AntMessage> capabilitiesFuture = sendMessage(createRequestMessage(CapabilitiesResponseMessage.MSG_ID));
			this.capabilities = new AntUsbDeviceCapabilities((CapabilitiesResponseMessage) capabilitiesFuture.get());

			CompletableFuture<AntMessage> antVersionFuture = sendMessage(createRequestMessage(AntVersionMessage.MSG_ID));
			this.antVersion = ((AntVersionMessage) antVersionFuture.get()).getAntVersion();

			CompletableFuture<AntMessage> deviceSerialNumberFuture = sendMessage(createRequestMessage(SerialNumberMessage.MSG_ID));
			this.serialNumber = ((SerialNumberMessage) deviceSerialNumberFuture.get()).getSerialNumber();

			LOG.debug("Capabilities: {}", this.capabilities.toString());
			LOG.debug("Ant Version: {}", ByteUtils.hexString(this.antVersion));
			LOG.debug("SerialNumber: {}", ByteUtils.hexString(this.serialNumber));

		} catch (Exception e) {
			throw new AntException(e);
		}

	}

	private RequestMessage createRequestMessage(byte requestedMsgId) {
		return new RequestMessage((byte) 0, requestedMsgId, (byte) 0, (byte) 0);
	}

	public CompletableFuture<AntMessage> sendMessage(AntMessage requestMessage) throws AntException {
		CompletableFuture<AntMessage> future = new CompletableFuture<>();
		sendMessage(requestMessage, createListenerIfNeeded(requestMessage, future));
		return future;
	}

	private MessageBusListener<AntMessage> createListenerIfNeeded(AntMessage msgToSend, CompletableFuture<AntMessage> future) {

		if (msgToSend instanceof RequestMessage) {
			return msg -> {
				RequestMessage requestMsgToSend = (RequestMessage) msgToSend;
				if (requestMsgToSend.getMsgIdRequested() == msg.getMessageId()) {
					LOG.debug("RequestMessageListener, treating message " + ByteUtils.hexString(((AbstractAntMessage) msg).getMessageContent()));
					future.complete(msg);
					return true;
				}
				LOG.debug("RequestMessageListener. Waiting for msgId {}  NOT treating message {}", ByteUtils.hexString(requestMsgToSend.getMsgIdRequested()), ByteUtils.hexString(msg.toByteArray()));
				return false;
			};
		} else if (msgToSend instanceof AntBlockingMessage) {
			return msg -> {
				if (msg instanceof ChannelEventOrResponseMessage) {
					ChannelEventOrResponseMessage channelEventOrResponseMessage = (ChannelEventOrResponseMessage) msg;
					if (((ChannelEventOrResponseMessage) msg).getRespondToMessageId() == msgToSend.getMessageId()) {
						LOG.debug("ChannelEventMessageListener, treating message " + ByteUtils.hexString(((ChannelEventOrResponseMessage) msg).getMessageContent()));
						future.complete(msg);
						return true;
					}
					LOG.debug("ChannelEventMessageListener. Waiting for msgId {}  NOT treating message {}"
							, ByteUtils.hexString(channelEventOrResponseMessage.getRespondToMessageId())
							, ByteUtils.hexString(msg.toByteArray()));
				}
				return false;
			};
		}
		return null;
	}

	private synchronized void sendMessage(AntMessage message, MessageBusListener<AntMessage> listener)
			throws AntException {
		try {
			byte[] messageBytes = message.toByteArray();
			if (LOG.isDebugEnabled()) {
				LOG.debug("Sending {} with bytes {}.", message.getClass().getSimpleName(), ByteUtils.hexString(messageBytes));
			}

			if (listener != null) {
				antMessageUsbReader.addQueueListener(DEFAULT_TIMEOUT, 1, listener);
			}
			outPipe.syncSubmit(messageBytes);
		} catch (Throwable t) {
			throw new AntException(t);
		}
	}

	private UsbInterface getActiveUsbInterface() {
		return (UsbInterface) this.device.getActiveUsbConfiguration().getUsbInterfaces().get(0);
	}

	@Override
	public void close() throws IOException {
		try {
			closeAllChannels();
		} catch (Exception e) {
			LOG.error("Error during shutdown at closeAllChannels step. Continuing with release of usb device.", e);
		}

		try {
			this.antMessageUsbReader.stop();
			UsbInterface activeUsbInterface = getActiveUsbInterface();
			if (activeUsbInterface.isClaimed()) {
				activeUsbInterface.release();
			}
			antMessageUsbReader.close();

		} catch (Exception e) {
			throw new IOException(e.getMessage(), e);
		}
	}

	public void closeAllChannels() throws AntException, ExecutionException, InterruptedException {
		short maxChannels = capabilities.getMaxChannels();
		for (int i = 0; i < maxChannels; i++) {
			RequestMessage requestMessage = new RequestMessage((byte) i, ChannelStatusMessage.MSG_ID);
			ChannelStatusMessage responseMessage = (ChannelStatusMessage) sendMessage(requestMessage).get();
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

	private void closeChannel(byte channelNumber) throws InterruptedException, ExecutionException, AntException {
		UnassignChannelMessage unassignChannelMessage = new UnassignChannelMessage(channelNumber);
		ChannelEventOrResponseMessage unassignResponseMsg = (ChannelEventOrResponseMessage) sendMessage(unassignChannelMessage).get();
		if (unassignResponseMsg.getResponseCode() != ChannelEventResponseCode.RESPONSE_NO_ERROR) {
			LOG.debug("Received unexpected message {}. Halting program.", unassignResponseMsg);
			throw new AntException("Could not close channel " + channelNumber);
		}
		LOG.debug("Successfully unassigned channel {}.", channelNumber);
	}

	private static class GlobalMessageBusListener implements MessageBusListener<AntMessage> {

		@Override
		public boolean handle(AntMessage antMessage) {
			LOG.debug("GlobalMessageListener: Received {}", antMessage.toString());
			return true;
		}
	}

	public AntUsbDeviceCapabilities getCapabilities() {
		return capabilities;
	}
}
