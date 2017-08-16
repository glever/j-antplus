package be.glever.anttest;

import java.io.IOException;
import java.util.List;

import be.glever.ant.AntException;
import be.glever.ant.usb.AntDevice;
import be.glever.ant.usb.AntDeviceFactory;

public class AntDeviceFactoryTest {

	public static void main(String[] args) throws AntException, IOException {
		List<AntDevice> availableAntDevices = new AntDeviceFactory().getAvailableAntDevices();
		for(AntDevice antDevice: availableAntDevices) {
			antDevice.initialize();
			antDevice.close();
		}
	}
}
