package be.glever.anttest;

import be.glever.ant.usb.AntUsbDevice;
import be.glever.ant.usb.AntUsbDeviceFactory;

public class AntDeviceTest_Main {

	public static void main(String[] args) throws Exception {
		try(AntUsbDevice device = AntUsbDeviceFactory.getAvailableAntDevices().stream().findFirst()
				.orElseThrow(() -> new Exception("No devices found"))){
			device.initialize();
		}
	}

}
