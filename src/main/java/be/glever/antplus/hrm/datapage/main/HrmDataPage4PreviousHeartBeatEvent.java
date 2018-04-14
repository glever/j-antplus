package be.glever.antplus.hrm.datapage.main;

import be.glever.ant.util.ByteUtils;
import be.glever.antplus.hrm.datapage.AbstractHRMDataPage;

/**
 * On of the main HRM DataPages. For non-swimming hrm probably THE main DataPage.
 * Adds the previous Heart Beat time to the datapage.
 */
public class HrmDataPage4PreviousHeartBeatEvent extends AbstractHRMDataPage {

	public HrmDataPage4PreviousHeartBeatEvent(byte[] dataPageBytes) {
		super(dataPageBytes);
	}

	/**
	 * Similar to {@link AbstractHRMDataPage#getHeartBeatEventTime()}, but for previous heart beat. Provides level of redundancy.
	 */
	public int getPreviousHeartBeatEventTime() {
		// byte 0 is manufacturer specific and must be ignored by receiver.
		int prevTimeAnt = ByteUtils.toInt(getPageSpecificBytes()[1], getPageSpecificBytes()[2]);
		return (prevTimeAnt * 1024) / 1000;
	}

	@Override
	public byte getPageNumber() {
		return 4;
	}
}
