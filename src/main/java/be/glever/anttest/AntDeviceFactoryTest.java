package be.glever.anttest;

import java.io.IOException;
import java.util.List;

import be.glever.ant.AntException;
import be.glever.ant.usb.AntUsbDevice;
import be.glever.ant.usb.AntUsbDeviceFactory;

public class AntDeviceFactoryTest {

	public static void main(String[] args) throws AntException, IOException {
		List<AntUsbDevice> availableAntDevices = new AntUsbDeviceFactory().getAvailableAntDevices();
		for(AntUsbDevice antDevice: availableAntDevices) {
			antDevice.initialize();
			antDevice.close();
		}
	}
}
