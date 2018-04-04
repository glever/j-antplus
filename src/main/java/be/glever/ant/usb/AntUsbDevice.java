package be.glever.ant.usb;

import be.glever.ant.AntException;
import be.glever.ant.message.AntMessage;
import be.glever.ant.message.control.RequestMessage;
import be.glever.ant.message.requestedresponse.AntVersionMessage;
import be.glever.ant.message.requestedresponse.CapabilitiesResponseMessage;
import be.glever.ant.message.requestedresponse.DeviceSerialNumberMessage;
import be.glever.ant.messagebus.MessageBus;
import be.glever.ant.messagebus.MessageBusListener;
import be.glever.ant.util.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.usb.*;
import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Provides abstraction of and interaction with an ANT USB dongle.
 * Clients can
 */
public class AntUsbDevice implements Closeable {
	private static Logger LOG = LoggerFactory.getLogger(AntUsbDevice.class);
	private static final long DEFAULT_TIMEOUT = 1000;

	private boolean initialized = false;
	private UsbDevice device;
	private MessageBus<AntMessage> messageBus;

	private UsbPipe outPipe;

	private UsbPipe inPipe;
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

			inPipe = inEndpoint.getUsbPipe();
			inPipe.open();

			this.messageBus = new MessageBus<>();
			messageBus.addQueueListener(-1, -1, new GlobalMessageBusListener());

			antMessageUsbReader = new AntUsbMessageReader(inPipe, messageBus);
			new Thread(antMessageUsbReader).start();

			CompletableFuture<AntMessage> capabilitiesFuture = sendMessage(createRequestMessage(CapabilitiesResponseMessage.MSG_ID));
			this.capabilities = new AntUsbDeviceCapabilities((CapabilitiesResponseMessage) capabilitiesFuture.get());

			CompletableFuture<AntMessage> antVersionFuture = sendMessage(createRequestMessage(AntVersionMessage.MSG_ID));
			this.antVersion = ((AntVersionMessage) antVersionFuture.get()).getAntVersion();

			CompletableFuture<AntMessage> deviceSerialNumberFuture = sendMessage(createRequestMessage(DeviceSerialNumberMessage.MSG_ID));
			this.serialNumber = ((DeviceSerialNumberMessage) deviceSerialNumberFuture.get()).getSerialNumber();

			LOG.debug("Capabilities: {}", this.capabilities.toString());
			LOG.debug("Ant Version: {}", ByteUtils.hexString(this.antVersion));
			LOG.debug("SerialNumber: {}", ByteUtils.hexString(this.serialNumber));

		} catch (Exception e) {
			throw new AntException(e);
		}

	}

	private CompletableFuture<AntMessage> sendMessage(RequestMessage requestMessage) throws AntException {
		byte msgIdRequested = requestMessage.getMsgIdRequested();

		CompletableFuture<AntMessage> future = new CompletableFuture<>();

		sendMessagePrivate(requestMessage, msg -> {
			if (msg.getMessageId() != msgIdRequested) {
				LOG.debug("MsgId {} does not match requested msgId {}, skipping", ByteUtils.hexString(msg.getMessageId()), ByteUtils.hexString(msgIdRequested));
				return true;
			}
			future.complete(msg);
			return false;
		});

		return future;
	}

	private RequestMessage createRequestMessage(byte requestedMsgId) {
		return new RequestMessage((byte) 0, requestedMsgId, (byte) 0, (byte) 0);
	}

	public synchronized void sendMessagePrivate(AntMessage message, MessageBusListener<AntMessage> listener)
			throws AntException {
		try {
			byte[] messageBytes = message.toByteArray();
			if (LOG.isDebugEnabled()) {
				LOG.debug("Sending {} with bytes {}.", message.getClass(), ByteUtils.hexString(messageBytes));
			}

			if (listener != null) {
				messageBus.addQueueListener(DEFAULT_TIMEOUT, 1, listener);
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
			this.antMessageUsbReader.stop();
			UsbInterface activeUsbInterface = getActiveUsbInterface();
			if (activeUsbInterface.isClaimed()) {
				activeUsbInterface.release();
			}
			messageBus.close();

		} catch (Exception e) {
			throw new IOException(e.getMessage(), e);
		}
	}

	private static class GlobalMessageBusListener implements MessageBusListener<AntMessage> {

		@Override
		public boolean handle(AntMessage antMessage) {
			LOG.debug("GlobalMessageListener: Received {}", antMessage.getClass());
			return true;
		}
	}

}
