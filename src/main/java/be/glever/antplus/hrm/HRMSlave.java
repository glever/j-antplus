package be.glever.antplus.hrm;

import be.glever.ant.AntException;
import be.glever.ant.channel.AntChannel;
import be.glever.ant.constants.AntPlusDeviceType;
import be.glever.ant.message.channel.ChannelEventOrResponseMessage;
import be.glever.ant.message.channel.ChannelEventResponseCode;
import be.glever.ant.message.configuration.*;
import be.glever.ant.message.control.OpenChannelMessage;
import be.glever.ant.usb.AntUsbDevice;
import be.glever.ant.util.ByteUtils;

import java.util.concurrent.ExecutionException;

public class HRMSlave {

	private static final byte ANTPLUS_HRM_FREQUENCY = (byte) 57; // 2457Mhz
	private AntUsbDevice antUsbDevice;

	public HRMSlave(AntUsbDevice antUsbDevice) {
		this.antUsbDevice = antUsbDevice;
	}

	public AntChannel[] listDevices() throws AntException, ExecutionException, InterruptedException {
		HRMChannel channel = new HRMChannel();

		byte channelNumber = 0x00;

		// ASSIGN CHANNEL
		NetworkKeyMessage networkKeyMessage = new NetworkKeyMessage(channelNumber, channel.getNetwork().getKeyBytes());
		ChannelEventOrResponseMessage setNetworkKeyResponse = (ChannelEventOrResponseMessage) antUsbDevice.sendMessage(networkKeyMessage).get();
		if (setNetworkKeyResponse.getResponseCode() != ChannelEventResponseCode.RESPONSE_NO_ERROR) {
			throw new RuntimeException("Could not set network key");
		}

		AssignChannelMessage assignChannelMessage = new AssignChannelMessage(channelNumber, (byte) 0x00, (byte) 0x00);
		ChannelEventOrResponseMessage response = (ChannelEventOrResponseMessage) antUsbDevice.sendMessage(assignChannelMessage).get();
		if (response.getResponseCode() != ChannelEventResponseCode.RESPONSE_NO_ERROR) {
			throw new RuntimeException("Could not assign channel");
		}

		ChannelIdMessage channelIdMessage = new ChannelIdMessage(channelNumber, new byte[]{0x00, 0x00}, AntPlusDeviceType.HRM.value(), (byte)0x00);
		ChannelEventOrResponseMessage channelIdResponse = (ChannelEventOrResponseMessage) antUsbDevice.sendMessage(channelIdMessage).get();
		if (channelIdResponse.getResponseCode() != ChannelEventResponseCode.RESPONSE_NO_ERROR) {
			throw new RuntimeException("Could set channelId");
		}
		ChannelPeriodMessage channelPeriodMessage = new ChannelPeriodMessage(channelNumber,ByteUtils.toUShort(8070));
		ChannelEventOrResponseMessage channelPeriodResponseMessage = (ChannelEventOrResponseMessage) antUsbDevice.sendMessage(channelPeriodMessage).get();
		if (channelPeriodResponseMessage.getResponseCode() != ChannelEventResponseCode.RESPONSE_NO_ERROR) {
			throw new RuntimeException("Could set channel period");
		}

		ChannelRfFrequencyMessage channelRfFrequencyMessage = new ChannelRfFrequencyMessage(channelNumber,(byte)0x39);
		ChannelEventOrResponseMessage channelRfFrequencyResponse = (ChannelEventOrResponseMessage) antUsbDevice.sendMessage(channelRfFrequencyMessage).get();
		if (channelRfFrequencyResponse.getResponseCode() != ChannelEventResponseCode.RESPONSE_NO_ERROR) {
			throw new RuntimeException("Could not set rf frequency");
		}

		response = (ChannelEventOrResponseMessage) antUsbDevice.sendMessage(new OpenChannelMessage(response.getChannelNumber())).get();
		if (response.getResponseCode() != ChannelEventResponseCode.RESPONSE_NO_ERROR) {
			throw new RuntimeException("Could not open channel");
		}

		// OPEN CHANNEL
		return null;
	}

}
