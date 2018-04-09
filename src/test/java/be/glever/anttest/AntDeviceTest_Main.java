package be.glever.anttest;

import be.glever.ant.usb.AntUsbDevice;
import be.glever.ant.usb.AntUsbDeviceFactory;
import be.glever.antplus.hrm.HRMSlave;

public class AntDeviceTest_Main {

	public static void main(String[] args) throws Exception {
		try(AntUsbDevice device = AntUsbDeviceFactory.getAvailableAntDevices().stream().findFirst()
				.orElseThrow(() -> new Exception("No devices found"))){
			device.initialize();
			device.closeAllChannels(); // channels stay open on usb dongle even if program shuts down.

			HRMSlave hrm = new HRMSlave(device);
			hrm.listDevices();

			System.in.read();
		}
	}

}
