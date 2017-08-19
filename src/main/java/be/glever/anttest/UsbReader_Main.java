package be.glever.anttest;

import java.io.UnsupportedEncodingException;
import java.nio.channels.ShutdownChannelGroupException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.usb.UsbConst;
import javax.usb.UsbDevice;
import javax.usb.UsbDisconnectedException;
import javax.usb.UsbEndpoint;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbInterface;
import javax.usb.UsbInterfacePolicy;
import javax.usb.UsbPipe;
import javax.usb.UsbPlatformException;
import javax.usb.event.UsbPipeDataEvent;
import javax.usb.event.UsbPipeErrorEvent;
import javax.usb.event.UsbPipeListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.glever.ant.AntUtil;
import be.glever.ant.message.AntMessage;
import be.glever.ant.messagebus.MessageBus;
import be.glever.ant.usb.AntUsbMessageReader;
import be.glever.ant.util.ByteUtils;

public class UsbReader_Main {
	private static final Logger LOG = LoggerFactory.getLogger(UsbReader_Main.class);
	
	public static void main(String[] args) throws SecurityException, UsbException, UnsupportedEncodingException,
			UsbDisconnectedException, InterruptedException {
		UsbDevice device = getAntDevices().iterator().next();
		System.out.println("Device found: ");
		System.out.println(device.getUsbDeviceDescriptor());

		UsbInterface usbInterface = (javax.usb.UsbInterface) device.getActiveUsbConfiguration().getUsbInterfaces()
				.get(0);
		System.out.println(usbInterface.isActive());
		System.out.println(usbInterface.isClaimed());
		usbInterface.claim();
		System.out.println(usbInterface.isActive());
		System.out.println(usbInterface.isClaimed());

		@SuppressWarnings("unchecked")
		List<UsbEndpoint> usbEndpoints = usbInterface.getUsbEndpoints();
		UsbEndpoint inEndpoint = usbEndpoints.stream()
				.filter(endpoint -> endpoint.getDirection() == UsbConst.ENDPOINT_DIRECTION_IN).findAny().get();
		UsbEndpoint outEndpoint = usbEndpoints.stream()
				.filter(endpoint -> endpoint.getDirection() == UsbConst.ENDPOINT_DIRECTION_OUT).findAny().get();

		UsbPipe outPipe = outEndpoint.getUsbPipe();
		UsbPipe inPipe = inEndpoint.getUsbPipe();

		MessageBus<AntMessage> messageBus = new MessageBus<>();
		messageBus.addQueueListener(-1, -1, message -> {
			return true;
		});
		AntUsbMessageReader usbReader = new AntUsbMessageReader(inPipe, messageBus);
		new Thread(usbReader).start();

		outPipe.open();
		outPipe.syncSubmit(new byte[128]);
		// outPipe.syncSubmit( new byte [] {(byte)0xa4, (byte)0x20, (byte)0xae,
		// (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0,
		// (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0,
		// (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0,
		// (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0,
		// (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x2a, (byte)0x0, (byte)0x0,
		// (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0,
		// (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0,
		// (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0,
		// (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0,(byte)0xa4, (byte)0x20,
		// (byte)0xae, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0,
		// (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0,
		// (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0,
		// (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0,
		// (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x2a, (byte)0x0,
		// (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0,
		// (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0,
		// (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0,
		// (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0, (byte)0x0});
		outPipe.syncSubmit(new byte[] { (byte) 0xa4, (byte) 0x02, (byte) 0x4D, (byte) 0x00, (byte) 0x54, (byte) 0xBF });
		// [A4][02][4D][00][54][BF]
		outPipe.close();

		Thread.sleep(10000);
		usbReader.stop();
	}

	private static List<UsbDevice> getAntDevices() throws UsbException {
		List<UsbDevice> antDevices = null;
		boolean sleep = false;
		do {
			antDevices = AntUtil.getAntDevices();
			if (antDevices.size() == 0) {
				sleep = true;
				try {
					System.out.println("no device found, sleeping...");
					Thread.sleep(1000);
				} catch (InterruptedException ignored) {
					// ignore
				}
			} else {
				sleep = false;
			}
		} while (sleep);

		return antDevices;
	}

}
