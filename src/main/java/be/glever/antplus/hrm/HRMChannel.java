package be.glever.antplus.hrm;

import be.glever.ant.channel.AntChannel;
import be.glever.ant.channel.AntChannelId;
import be.glever.ant.channel.AntChannelNetwork;
import be.glever.ant.channel.AntChannelTransmissionType;
import be.glever.ant.constants.AntChannelType;
import be.glever.ant.constants.AntNetworkKeys;
import be.glever.ant.constants.AntPlusDeviceType;
import be.glever.ant.util.ByteUtils;

public class HRMChannel extends AntChannel {
	public static final byte CHANNEL_FREQUENCY = 0x39;
	public static final byte[] DEVICE_NUMBER_WILDCARD = {0x00, 0x00};
	byte[] CHANNEL_PERIOD = ByteUtils.toUShort(8070);
	public static final byte DEFAULT_PUBLIC_NETWORK = 0x00;

	/**
	 * Pair with any (the first found) HRM.
	 */
	public HRMChannel() {
		this( DEVICE_NUMBER_WILDCARD);
	}

	/**
	 * Pair with a known device.
	 * @param deviceNumber
	 */
	public HRMChannel( byte[] deviceNumber) {
		setChannelType(AntChannelType.BIDIRECTIONAL_SLAVE);
		setNetwork(new AntChannelNetwork(DEFAULT_PUBLIC_NETWORK, AntNetworkKeys.ANT_PLUS_NETWORK_KEY));
		setRfFrequency(CHANNEL_FREQUENCY);
		setChannelId(new AntChannelId(AntChannelTransmissionType.PAIRING_TRANSMISSION_TYPE, AntPlusDeviceType.HRM, deviceNumber));
		setChannelPeriod(CHANNEL_PERIOD);
	}
}
