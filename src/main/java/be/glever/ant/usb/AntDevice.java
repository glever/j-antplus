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
import javax.usb.UsbNotOpenException;
import javax.usb.UsbPipe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;
import be.glever.ant.message.AntMessage;
import be.glever.ant.messagebus.MessageBus;

public class AntDevice implements Closeable {
	private static Logger LOG = LoggerFactory.getLogger(AntDevice.class);

	private boolean initialized = false;
	private UsbDevice device;
	private MessageBus<AntMessage> messageBus;

	private UsbPipe outPipe;

	private UsbPipe inPipe;

	/**
	 * 
	 * @param device
	 */
	public AntDevice(UsbDevice device) {
		LOG.info("new device");
		this.device = device;
		device.getActiveUsbConfiguration();
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
			UsbEndpoint inEndpoint = usbEndpoints.stream().filter(endpoint -> endpoint.getDirection() == UsbConst.ENDPOINT_DIRECTION_IN).findAny().get();
			UsbEndpoint outEndpoint = usbEndpoints.stream().filter(endpoint -> endpoint.getDirection() == UsbConst.ENDPOINT_DIRECTION_OUT).findAny().get();

			outPipe = outEndpoint.getUsbPipe();
			outPipe.open();

			inPipe = inEndpoint.getUsbPipe();
			inPipe.open();
			
			initialized = true;
		} catch (UsbNotActiveException | UsbDisconnectedException | UsbException e) {
			throw new AntException(e);
		}

	}
	
	public synchronized void sendMessage(AntMessage message) throws AntException {
		try {
			outPipe.asyncSubmit(message.getMessageContent());
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
			throw new IOException(e.getMessage(),e);
		}
	}

}
