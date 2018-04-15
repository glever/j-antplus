package be.glever.antplus.hrm.datapage.background;

import be.glever.ant.util.ByteUtils;
import be.glever.antplus.hrm.datapage.AbstractHRMDataPage;

public class HrmDataPage2ManufacturerInformation extends AbstractHRMDataPage {

	public static final byte PAGE_NR = 0x2;

	public HrmDataPage2ManufacturerInformation(byte[] dataPageBytes) {
		super(dataPageBytes);
	}

	public int getManufacturerId() {
		return super.getPageSpecificBytes()[0];
	}

	public int getSerialNumber() {
		return ByteUtils.toInt(getPageSpecificBytes()[1], getPageSpecificBytes()[2]);
	}

	@Override
	public byte getPageNumber() {
		return PAGE_NR;
	}
}
