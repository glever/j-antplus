package be.glever.ant.usb;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import javax.usb.UsbConst;
import javax.usb.UsbDevice;
import javax.usb.UsbDisconnectedException;
import javax.usb.UsbEndpoint;
import javax.usb.UsbException;
import javax.usb.UsbInterface;
import javax.usb.UsbNotActiveException;
import javax.usb.UsbPipe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.glever.ant.AntException;
import be.glever.ant.message.AntMessage;
import be.glever.ant.message.control.RequestMessage;
import be.glever.ant.message.requestedresponse.AntVersionMessage;
import be.glever.ant.message.requestedresponse.CapabilitiesResponseMessage;
import be.glever.ant.message.requestedresponse.DeviceSerialNumberMessage;
import be.glever.ant.messagebus.MessageBus;
import be.glever.ant.messagebus.MessageBusListener;

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

	/**
	 * 
	 * @param device
	 */
	public AntUsbDevice(UsbDevice device) {
		LOG.info("new device");
		this.device = device;
	}

	public void initialize() throws AntException {
		UsbInterface usbInterface = getActiveUsbInterface();
		if (usbInterface.isClaimed()) {
			throw new AntException("Usb device already claimed");
		}

		try {
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
			messageBus.addQueueListener(-1, -1, msg -> {
				LOG.debug("Received {}", msg.getClass());
				return true;
			});

			AntUsbMessageReader antMessageUsbReader = new AntUsbMessageReader(inPipe, messageBus);
			new Thread(antMessageUsbReader).start();

			Thread currentThread = Thread.currentThread();
			sendRequestMessage(createRequestMessage(CapabilitiesResponseMessage.MSG_ID), capabilitiesResponse -> {
				this.capabilities = new AntUsbDeviceCapabilities((CapabilitiesResponseMessage) capabilitiesResponse);

				try {
					sendRequestMessage(createRequestMessage(AntVersionMessage.MSG_ID), versionResponse -> {
						this.antVersion = ((AntVersionMessage) versionResponse).getAntVersion();

						sendRequestMessage(createRequestMessage(DeviceSerialNumberMessage.MSG_ID), serialNrResp -> {
							this.serialNumber = ((DeviceSerialNumberMessage) serialNrResp).getSerialNumber();
							initialized = true;
							currentThread.interrupt();
						});
					});
				} catch (Throwable t) {
					currentThread.interrupt();
				}
			});

			try {
				Thread.sleep(Long.MAX_VALUE);
			} catch (InterruptedException e) {
				Thread.interrupted();
			}
			if (!initialized) {
				throw new AntException("Could not initialize");
			}

		} catch (UsbNotActiveException | UsbDisconnectedException | UsbException e) {
			throw new AntException(e);
		}

	}

	private void sendRequestMessage(RequestMessage requestMessage, AntMessageHandler handler) throws AntException {
		byte msgIdRequested = requestMessage.getMessageId();

		sendMessage(requestMessage, msg -> {
			if (msg.getMessageId() != msgIdRequested) {
				return false;
			}
			try {
				handler.handle(msg);
			} catch (AntException e) {
				throw new RuntimeException(e);
			}
			return true;
		});
	}

	private static interface AntMessageHandler {
		void handle(AntMessage msg) throws AntException;
	}

	private RequestMessage createRequestMessage(byte requestedMsgId) {
		return new RequestMessage((byte) 0, requestedMsgId, (byte) 0, (byte) 0);
	}

	public synchronized void sendMessage(AntMessage message, MessageBusListener<AntMessage> listener)
			throws AntException {
		try {
			if (listener != null) {
				messageBus.addQueueListener(DEFAULT_TIMEOUT, 1, listener);
			}
			outPipe.syncSubmit(message.getMessageContent());
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
			UsbInterface activeUsbInterface = getActiveUsbInterface();
			if (activeUsbInterface.isClaimed()) {
				activeUsbInterface.release();
			}
		} catch (UsbNotActiveException | UsbDisconnectedException | UsbException e) {
			throw new IOException(e.getMessage(), e);
		}
	}

}
