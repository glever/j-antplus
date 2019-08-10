package be.glever.antplus.hrm.datapage;

import be.glever.ant.util.ByteUtils;
import be.glever.antplus.common.datapage.AbstractAntPlusDataPage;

import java.util.Arrays;

public abstract class AbstractHRMDataPage extends AbstractAntPlusDataPage {

    public AbstractHRMDataPage(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    /**
     * Subclasses typically parse this byte to useful data.
     */
    protected byte[] getPageSpecificBytes() {
        return Arrays.copyOfRange(getDataPageBytes(), 1, 4);
    }

    /**
     * Time of last valid heartbeat event in milliseconds sinds "epoch".
     * Note that the ANT+ field size for this is 16 bit and resolution is 1/1024 seconds,
     * so the  value rolls over at 63.999 seconds ( ((1/1024)*(2^17)) - (1/1024)) ).
     */
    public long getHeartBeatEventTime() {
        return calculateHeartBeatEventTime(getDataPageBytes()[4], getDataPageBytes()[5]);
    }

    protected long calculateHeartBeatEventTime(byte bite1, byte bite2) {
        double timeAnt = ByteUtils.fromUShort(bite1, bite2);
        return (long) ((timeAnt / 1024) * 1000);
    }

    /**
     * Number of heartbeats since rollover.
     * Ant+ field size is 1 byte so rollover = 256.
     */
    public int getHeartBeatCount() {
        return 0xFF & getDataPageBytes()[6];
    }

    /**
     * Heart rate computed by device. Meant for direct display without further interpretation.
     *
     * @return The heart rate computed by device or -1 if invalid.
     */
    public int getComputedHeartRateInBpm() {
        return 0xFF & getDataPageBytes()[7];
    }

    protected String getToString() {
        return "heartBeatEventTime=" + getHeartBeatEventTime()
                + ",heartBeatCount=" + getHeartBeatCount()
                + ",computedHeartRateInBpm=" + getComputedHeartRateInBpm();
    }


}
