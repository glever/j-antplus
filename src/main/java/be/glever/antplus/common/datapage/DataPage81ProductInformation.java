package be.glever.antplus.common.datapage;

public class DataPage81ProductInformation extends AbstractAntPlusDataPage {
	public DataPage81ProductInformation(byte[] dataPageBytes) {
		super(dataPageBytes);
	}

	@Override
	public byte getPageNumber() {
		return 81;
	}

	public byte getSupplementalSwRevision() {
		return getDataPageBytes()[2];
	}

	public byte getMainSwRevision() {
		return getDataPageBytes()[3];
	}

	public byte[] getLowest32BitsOfSerialNumber() {
		return super.dataPageSubArray(4, 8);
	}
}
