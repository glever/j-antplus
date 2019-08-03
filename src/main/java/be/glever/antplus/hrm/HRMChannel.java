package be.glever.antplus.hrm;

import be.glever.ant.channel.*;
import be.glever.ant.constants.AntChannelType;
import be.glever.ant.constants.AntNetworkKeys;
import be.glever.ant.constants.AntPlusDeviceType;
import be.glever.ant.message.AntMessage;
import be.glever.ant.message.data.BroadcastDataMessage;
import be.glever.ant.util.ByteUtils;
import be.glever.antplus.common.datapage.AbstractAntPlusDataPage;
import be.glever.antplus.hrm.datapage.HrmDataPageRegistry;
import be.glever.antplus.hrm.datapage.main.HrmDataPage4PreviousHeartBeatEvent;
import be.glever.antplus.hrm.stats.StatCalculator;
import be.glever.antplus.hrm.stats.StatSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HRMChannel extends AntChannel implements AntChannelListener {
    private static final Logger LOG = LoggerFactory.getLogger(HRMChannel.class);
    public static final byte CHANNEL_FREQUENCY = 0x39;
    public static final byte[] DEVICE_NUMBER_WILDCARD = {0x00, 0x00};
    byte[] CHANNEL_PERIOD = ByteUtils.toUShort(8070);
    public static final byte DEFAULT_PUBLIC_NETWORK = 0x00;
    private HrmDataPageRegistry registry = new HrmDataPageRegistry();
    private int heartbeatCount;

    // todo doesnt belong here
    private int heartBeatEventTime;

    private StatCalculator statCalculator = new StatCalculator();

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

        super.addListener(this);
    }

    @Override
    public void handle(AntMessage antMessage) {
        if (antMessage instanceof BroadcastDataMessage) {
            BroadcastDataMessage msg = (BroadcastDataMessage) antMessage;
            byte[] payLoad = msg.getPayLoad();
            removeToggleBit(payLoad);
            AbstractAntPlusDataPage dataPage = registry.constructDataPage(payLoad);

            LOG.debug("Received datapage " + dataPage.toString());
            if (dataPage instanceof HrmDataPage4PreviousHeartBeatEvent) {
                calcStats((HrmDataPage4PreviousHeartBeatEvent) dataPage);
            }
        }
    }

    // TODO responsibility of client
    private void calcStats(HrmDataPage4PreviousHeartBeatEvent dataPage) {
        if (this.heartbeatCount != dataPage.getHeartBeatCount()) {
            this.heartbeatCount = dataPage.getHeartBeatCount();

            StatSummary statSummary = this.statCalculator.push(dataPage);

            LOG.info(statSummary.toString());
        }
    }

    private int addRolloverTime(int timeInSeconds) {
        int timeToAddInSeconds = (64000 / 1024) * 1000;
        return timeInSeconds + timeToAddInSeconds;
    }

    /**
     * For the moment not taking the legacy hrm devices into account.
     * Non-legacy devices swap the first bit of the pageNumber every 4 messages.
     *
     * @param payLoad
     */
    private void removeToggleBit(byte[] payLoad) {
        payLoad[0] = (byte) (0b01111111 & payLoad[0]);
    }
}
