package be.glever.antplus.hrm.datapage.background;

import be.glever.antplus.hrm.datapage.AbstractHRMDataPage;

public class HrmDataPage1CumulativeOperatingTime extends AbstractHRMDataPage {

    public static final byte PAGE_NR = 0x1;

    public HrmDataPage1CumulativeOperatingTime(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    /**
     * @return The time in seconds since the last reset (= battery replace) of the HRM. This value rolls over at 33554430 seconds ( 9320 hours or 388.x days)
     */
    public int getCumulativeOperatingTime() {
        byte[] pageSpecificByte = getPageSpecificBytes();
        int cumulativeTimeIn2Seconds = pageSpecificByte[0] << pageSpecificByte[1] << pageSpecificByte[2];
        return cumulativeTimeIn2Seconds * 2;
    }

    @Override
    public byte getPageNumber() {
        return PAGE_NR;
    }
}
