package be.glever.antplus.common;

import be.glever.ant.AntException;
import be.glever.ant.channel.AntChannel;
import be.glever.ant.message.control.RequestMessage;
import be.glever.ant.message.requestedresponse.ChannelStatusMessage;
import be.glever.ant.usb.AntUsbDevice;
import be.glever.ant.usb.AntUsbDeviceCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class AntPlusDeviceScanner {
	private static final Logger LOG = LoggerFactory.getLogger(AntPlusDeviceScanner.class);

	private AntUsbDevice antUsbDevice;

	public AntPlusDeviceScanner(AntUsbDevice antUsbDevice) {

		this.antUsbDevice = antUsbDevice;
	}

	public List<AntChannel> scanAvailableDevices() throws AntException, ExecutionException, InterruptedException {
		AntUsbDeviceCapabilities capabilities = this.antUsbDevice.getCapabilities();
		short maxChannels = capabilities.getMaxChannels();
		for (int i = 0; i < maxChannels; i++) {
			RequestMessage requestMessage = new RequestMessage((byte) i, ChannelStatusMessage.MSG_ID);
			ChannelStatusMessage responseMessage = (ChannelStatusMessage) antUsbDevice.sendMessage(requestMessage).get();
			if (responseMessage.getChannelStatus() == ChannelStatusMessage.CHANNEL_STATUS.UnAssigned) {
				// scan channel for ant device
			} else {
				LOG.debug("Channel {} is in status {}. Not scanning.", responseMessage.getChannelNumber(), responseMessage.getChannelStatus());
			}

		}

		return new ArrayList<>();
	}

}
