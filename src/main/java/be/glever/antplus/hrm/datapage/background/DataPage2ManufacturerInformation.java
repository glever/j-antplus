package be.glever.antplus.hrm.datapage.background;

import be.glever.ant.util.ByteUtils;
import be.glever.antplus.hrm.datapage.AbstractHRMDataPage;

public class DataPage2ManufacturerInformation extends AbstractHRMDataPage {

	public DataPage2ManufacturerInformation(byte[] dataPageBytes) {
		super(dataPageBytes);
	}

	public int getManufacturerId() {
		return super.getPageSpecificBytes()[0];
	}

	public int getSerialNumber() {
		return ByteUtils.toInt(getPageSpecificBytes()[1], getPageSpecificBytes()[2]);
	}

	@Override
	public int getPageNumber() {
		return 0x2;
	}
}
