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
import be.glever.ant.message.data.BroadcastDataMessage;
import be.glever.ant.util.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

public class HRMChannel extends AntChannel {
    public static final byte CHANNEL_FREQUENCY = 0x39;
    public static final byte[] DEVICE_NUMBER_WILDCARD = {0x00, 0x00};
    public static final byte DEFAULT_PUBLIC_NETWORK = 0x00;
    private static final Logger LOG = LoggerFactory.getLogger(HRMChannel.class);
    byte[] CHANNEL_PERIOD = ByteUtils.toUShort(8070);

    // todo doesnt belong here
    private int heartBeatEventTime;


    private Flux<AntMessage> eventFlux;

    /**
     * Pair with any (the first found) HRM.
     */
    public HRMChannel() {
        this(DEVICE_NUMBER_WILDCARD);
    }

    /**
     * Pair with a known device.
     *
     * @param deviceNumber
     */
    public HRMChannel(byte[] deviceNumber) {
        setChannelType(AntChannelType.BIDIRECTIONAL_SLAVE);
        setNetwork(new AntChannelNetwork(DEFAULT_PUBLIC_NETWORK, AntNetworkKeys.ANT_PLUS_NETWORK_KEY));
        setRfFrequency(CHANNEL_FREQUENCY);
        setChannelId(new AntChannelId(AntChannelTransmissionType.PAIRING_TRANSMISSION_TYPE, AntPlusDeviceType.HRM, deviceNumber));
        setChannelPeriod(CHANNEL_PERIOD);
    }

    @Override
    public void subscribeTo(Flux<AntMessage> messageFlux) {
        eventFlux = messageFlux.filter(this::isMatchingAntMessage);
    }

    public Flux<AntMessage> getEventFlux() {
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
