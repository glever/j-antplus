package be.glever.antplus.hrm;

import be.glever.ant.channel.AntChannel;
import be.glever.ant.channel.AntChannelId;
import be.glever.ant.channel.AntChannelNetwork;
import be.glever.ant.channel.AntChannelTransmissionType;
import be.glever.ant.constants.AntChannelType;
import be.glever.ant.constants.AntNetworkKeys;
import be.glever.ant.constants.AntPlusDeviceType;
import be.glever.ant.message.AntMessage;
import be.glever.ant.message.channel.ChannelEventOrResponseMessage;
import be.glever.ant.message.data.AchnowledgeDataMessage;
import be.glever.ant.message.data.BroadcastDataMessage;
import be.glever.ant.usb.AntUsbDevice;
import be.glever.ant.util.ByteUtils;
import be.glever.antplus.hrm.datapage.background.HrmDataPage6Capabilities;
import be.glever.util.logging.Log;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;

public class HRMChannel extends AntChannel {
	public static final byte CHANNEL_FREQUENCY = 0x39;
	public static final byte[] DEVICE_NUMBER_WILDCARD = {0x00, 0x00};
	public static final byte DEFAULT_PUBLIC_NETWORK = 0x00;
	private static final Log LOG = Log.getLogger(HRMChannel.class);
	private final AntUsbDevice device;
	byte[] CHANNEL_PERIOD = ByteUtils.toUShort(8070);

	// todo doesnt belong here
	private int heartBeatEventTime;


	private Flux<AntMessage> eventFlux;

	/**
	 * Pair with any (the first found) HRM.
	 *
	 * @param device
	 */
	public HRMChannel(AntUsbDevice device) {
		this(device, DEVICE_NUMBER_WILDCARD);
	}

	/**
	 * Pair with a known device.
	 *
	 * @param device
	 * @param deviceNumber
	 */
	public HRMChannel(AntUsbDevice device, byte[] deviceNumber) {
		this.device = device;
		setChannelType(AntChannelType.BIDIRECTIONAL_SLAVE);
		setNetwork(new AntChannelNetwork(DEFAULT_PUBLIC_NETWORK, AntNetworkKeys.ANT_PLUS_NETWORK_KEY));
		setRfFrequency(CHANNEL_FREQUENCY);
		setChannelId(new AntChannelId(AntChannelTransmissionType.PAIRING_TRANSMISSION_TYPE, AntPlusDeviceType.HRM, deviceNumber));
		setChannelPeriod(CHANNEL_PERIOD);

		this.device.openChannel(this).block(Duration.ofSeconds(10));
	}

	@Override
	public void subscribeTo(Flux<AntMessage> messageFlux) {
		eventFlux = messageFlux
				.filter(this::isMatchingAntMessage)
				.distinctUntilChanged(AntMessage::toByteArray, Arrays::equals);

//        messageFlux.take(1).subscribe(message -> requestHrmInfo());
	}

	private void requestHrmInfo() {
		this.device.send(new AchnowledgeDataMessage(this.getChannelNumber(), HrmDataPage6Capabilities.PAGE_NR));
	}


	@Override
	public Flux<AntMessage> getEvents() {
		return eventFlux;
	}

	private boolean isMatchingAntMessage(AntMessage message) {
		if (message.getMessageId() == BroadcastDataMessage.MSG_ID) {
			return ((BroadcastDataMessage) message).getChannelNumber() == super.getChannelNumber();
		} else if (message.getMessageId() == ChannelEventOrResponseMessage.MSG_ID) {
			return ((ChannelEventOrResponseMessage) message).getChannelNumber() == super.getChannelNumber();
		}
		return false;
	}


}
