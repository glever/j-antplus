package be.glever.antplus.common.datapage;

public class DataPage79MultiComponentSystemProductInformation extends AbstractAntPlusDataPage {
	public DataPage79MultiComponentSystemProductInformation(byte[] dataPageBytes) {
		super(dataPageBytes);
	}

	@Override
	public byte getPageNumber() {
		return 0;
	}


	public byte getComponentIdentifier() {
		return getDataPageBytes()[1];
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
