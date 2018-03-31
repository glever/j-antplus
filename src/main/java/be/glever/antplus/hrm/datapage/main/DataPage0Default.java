package be.glever.antplus.hrm.datapage.main;

import be.glever.antplus.hrm.datapage.AbstractHRMDataPage;

/**
 * Default or unknown data page.
 * Used in transmission control patterns.
 * Contains no useful extra info over the generic {@link AbstractHRMDataPage}
 */
public class DataPage0Default extends AbstractHRMDataPage {


	public DataPage0Default(byte[] dataPageBytes) {
		super(dataPageBytes);
	}

	@Override
	public int getPageNumber() {
		return 0x0;
	}
}
