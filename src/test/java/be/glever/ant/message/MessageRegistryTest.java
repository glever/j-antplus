package be.glever.ant.message;

import org.junit.Assert;
import org.junit.Test;

import be.glever.ant.AntException;
import be.glever.ant.message.requestedresponse.CapabilitiesResponseMessage;
import be.glever.ant.message.requestedresponse.CapabilitiesResponseMessageTest;

public class MessageRegistryTest {

	@Test
	public void containsCapabilitiesResponseMessage() throws AntException {
		AntMessage message = new MessageRegistry().parse(CapabilitiesResponseMessageTest.M_USB_CAPABILITIES);
		Assert.assertTrue(message instanceof CapabilitiesResponseMessage);
	}
}
