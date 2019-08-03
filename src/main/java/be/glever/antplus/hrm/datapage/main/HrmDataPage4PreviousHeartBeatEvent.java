package be.glever.antplus.hrm.datapage.main;

import be.glever.antplus.hrm.datapage.AbstractHRMDataPage;

/**
 * On of the main HRM DataPages. For non-swimming hrm probably THE main DataPage.
 * Adds the previous Heart Beat time to the datapage.
 */
public class HrmDataPage4PreviousHeartBeatEvent extends AbstractHRMDataPage {

    public static final byte PAGE_NR = 4;

    public HrmDataPage4PreviousHeartBeatEvent(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    /**
     * Similar to {@link AbstractHRMDataPage#getHeartBeatEventTime()}, but for previous heart beat. Provides level of redundancy.
     */
    public double getPreviousHeartBeatEventTime() {
        return calculateHeartBeatEventTime(getPageSpecificBytes()[1], getPageSpecificBytes()[2]);
    }

    @Override
    public byte getPageNumber() {
        return PAGE_NR;
    }

    @Override
    public String toString() {
        return "HrmDataPage4PreviousHeartBeatEvent{" + super.getToString() + ",previousEventTime=" + getPreviousHeartBeatEventTime() + "}";
    }
}
