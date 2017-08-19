package be.glever.anttest;

import be.glever.ant.usb.AntUsbDevice;
import be.glever.ant.usb.AntUsbDeviceFactory;

public class AntDeviceTest_Main {

	public static void main(String[] args) throws Exception {
		AntUsbDevice device = new AntUsbDeviceFactory().getAvailableAntDevices().stream().findFirst()
				.orElseThrow(() -> new Exception("test"));
		device.initialize();
		device.close();
	}

}
