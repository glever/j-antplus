package be.glever.ant.usb;

import java.io.Closeable;
import java.io.IOException;

import javax.usb.UsbDevice;
import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;
import javax.usb.UsbInterface;
import javax.usb.UsbNotActiveException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.glever.ant.AntException;

public class AntDevice implements Closeable {
	private static Logger LOG = LoggerFactory.getLogger(AntDevice.class);

	private boolean initialized = false;
	private UsbDevice device;

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

			initialized = true;
		} catch (UsbNotActiveException | UsbDisconnectedException | UsbException e) {
			throw new AntException(e);
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
				// TODO Auto-generated catch block
			}
		} catch (UsbNotActiveException | UsbDisconnectedException | UsbException e) {
			throw new IOException(e.getMessage(),e);
		}
	}

}
