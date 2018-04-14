package be.glever.antplus.hrm.datapage.main;

import be.glever.antplus.hrm.datapage.AbstractHRMDataPage;

/**
 * Default or unknown data page.
 * Used in transmission control patterns.
 * Contains no useful extra info over the generic {@link AbstractHRMDataPage}
 */
public class HrmDataPage0Default extends AbstractHRMDataPage {


	public HrmDataPage0Default(byte[] dataPageBytes) {
		super(dataPageBytes);
	}

	@Override
	public byte getPageNumber() {
		return 0x0;
	}
}
