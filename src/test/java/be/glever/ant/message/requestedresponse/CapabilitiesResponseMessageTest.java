package be.glever.ant.message.requestedresponse;

import org.junit.Test;

import be.glever.ant.AntException;

public class CapabilitiesResponseMessageTest {

	public static final byte[] M_USB_CAPABILITIES = { (byte) 0xa4, 0x8, 0x54, 0x8, 0x8, 0x0, (byte) 0xba, 0x36,0x0, (byte) 0xdf, 0x4, (byte) 0xaf };

	@Test
	public void parseMusbCapabilities() throws AntException {
		byte[] message = M_USB_CAPABILITIES;
		
		CapabilitiesResponseMessage msg = new CapabilitiesResponseMessage();
		msg.parse(message);
//		System.out.println(msg.toString());
	}
}
