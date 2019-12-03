package be.glever.antplus.speedcadence.datapage.background;

import be.glever.ant.util.ByteUtils;
import be.glever.antplus.hrm.datapage.AbstractHRMDataPage;
import be.glever.antplus.hrm.datapage.background.HrmDataPage1CumulativeOperatingTime;
import be.glever.antplus.speedcadence.datapage.AbstractSpeedCadenceDataPage;
import be.glever.util.logging.Log;

import java.time.Duration;

public class SpeedCadenceDataPage1CumulativeOperatingTime extends AbstractSpeedCadenceDataPage {

    public static final byte PAGE_NR = 0x1;
    private static final Log LOG = Log.getLogger(SpeedCadenceDataPage1CumulativeOperatingTime.class);

    public SpeedCadenceDataPage1CumulativeOperatingTime(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    /**
     * How long the device has been operating for, since the last battery replacement
     *
     * Unit: seconds
     * Rollover: 3355443s seconds
     * TODO: It's from byte 1 to 3, is the implementation correct?
     *
     * @return the time the device has been operating for.
     */
    public int getCumulativeOperatingTime() {
        return 2 * ByteUtils.fromUnsignedBytes(getPageSpecificBytes());
    }

    @Override
    public byte getPageNumber() {
        return PAGE_NR;
    }


    @Override
    public String toString() {
        return String.format("%s, {%s, CumulativeOperatingTime=%s (%s)}", getClass().getSimpleName(), super.getToString(), getCumulativeOperatingTime(), Duration.ofSeconds(getCumulativeOperatingTime()));
    }
}
