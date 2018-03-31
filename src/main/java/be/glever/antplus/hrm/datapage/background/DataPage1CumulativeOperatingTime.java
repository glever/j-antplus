package be.glever.antplus.hrm.datapage.background;

import be.glever.antplus.hrm.datapage.AbstractHRMDataPage;

public class DataPage1CumulativeOperatingTime extends AbstractHRMDataPage {

	public DataPage1CumulativeOperatingTime(byte[] dataPageBytes) {
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
	public int getPageNumber() {
		return 0x1;
	}
}
