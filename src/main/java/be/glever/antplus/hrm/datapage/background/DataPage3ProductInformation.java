package be.glever.antplus.hrm.datapage.background;

import be.glever.antplus.hrm.datapage.AbstractHRMDataPage;

public class DataPage3ProductInformation extends AbstractHRMDataPage {

	public DataPage3ProductInformation(byte[] dataPageBytes) {
		super(dataPageBytes);
	}

	public byte getHardwareVersion() {
		return super.getPageSpecificBytes()[0];
	}

	public byte getSoftwareVersion() {
		return super.getPageSpecificBytes()[1];
	}

	public byte getModelNumber() {
		return super.getPageSpecificBytes()[2];
	}

	@Override
	public int getPageNumber() {
		return 0x3;
	}
}
