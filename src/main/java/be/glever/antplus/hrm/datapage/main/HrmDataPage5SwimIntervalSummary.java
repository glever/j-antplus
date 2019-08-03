package be.glever.antplus.hrm.datapage.main;

import be.glever.antplus.hrm.datapage.AbstractHRMDataPage;

import static be.glever.ant.util.ByteUtils.toInt;

public class HrmDataPage5SwimIntervalSummary extends AbstractHRMDataPage {

    public static final byte PAGE_NR = 5;

    public HrmDataPage5SwimIntervalSummary(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    /**
     * @return Interval average heart rate in BPM of the interval.
     */
    public int getIntervalAverageHeartRate() {
        return toInt(getPageSpecificBytes()[0]);
    }

    /**
     * @return Interval max heart rate in BPM of the interval.
     */
    public int getIntervalMaximumHeartRate() {
        return toInt(getPageSpecificBytes()[1]);
    }

    /**
     * @return Session average heart rate in BPM of the interval.
     */
    public int getSessionAverageHeartRate() {
        return toInt(getPageSpecificBytes()[2]);
    }

    @Override
    public byte getPageNumber() {
        return PAGE_NR;
    }
}
