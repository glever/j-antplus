package be.glever.ant;

import java.util.List;
import java.util.stream.Collectors;

import javax.usb.UsbDevice;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;

public class AntUtil {

	private static final int PRODUCT_DYNASTREAM_M_USB = 0x1009;
	private static final int VENDOR_DYNASTREAM = 0x0fcf;

	public static  List<UsbDevice> getAntDevices() throws SecurityException, UsbException {
		UsbHub rootUsbHub = UsbHostManager.getUsbServices().getRootUsbHub();

		@SuppressWarnings("unchecked")
		List<UsbDevice> attachedUsbDevices = rootUsbHub.getAttachedUsbDevices();
		
		return attachedUsbDevices.stream()
				.filter(device -> device.getUsbDeviceDescriptor().idVendor() == VENDOR_DYNASTREAM
						&& device.getUsbDeviceDescriptor().idProduct() == PRODUCT_DYNASTREAM_M_USB)
				.collect(Collectors.toList());
	}
}
