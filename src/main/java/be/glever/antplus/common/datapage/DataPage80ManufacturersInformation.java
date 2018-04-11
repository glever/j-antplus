package be.glever.antplus.common.datapage;

public class DataPage80ManufacturersInformation extends AbstractAntPlusDataPage {
	public DataPage80ManufacturersInformation(byte[] dataPageBytes) {
		super(dataPageBytes);
	}

	@Override
	public byte getPageNumber() {
		return 80;
	}


	public byte getHwRevision() {
		return getDataPageBytes()[3];
	}

	public byte[] getManufacturerId() {
		return super.dataPageSubArray(4, 6);
	}

	public byte[] getModelNumber() {
		return super.dataPageSubArray(6, 8);
	}
}