package be.glever.antplus.hrm;

import be.glever.ant.channel.*;
import be.glever.ant.constants.AntChannelType;
import be.glever.ant.constants.AntPlusDeviceType;
import be.glever.ant.constants.AntNetworkKeys;

import static be.glever.ant.channel.AntDeviceNumber.DEVICE_NUMBER_FOR_SEARCHING;

public class HRMChannel extends AntChannel {
	public static final byte CHANNEL_FREQUENCY = 57;

	public HRMChannel() {
		setChannelType(AntChannelType.BIDIRECTIONAL_SLAVE);
		setNetwork(new AntChannelNetwork((byte) 0x00, AntNetworkKeys.ANT_PLUS_NETWORK_KEY));
		setRfFrequency(new AntChannelRfFrequency(CHANNEL_FREQUENCY));
		setChannelId(new AntChannelId(AntChannelTransmissionType.PAIRING_TRANSMISSION_TYPE, AntPlusDeviceType.HRM, DEVICE_NUMBER_FOR_SEARCHING));
		setChannelPeriod(AntChannelPeriod.DEFAULT_CHANNEL_PERIOD);
	}
}
