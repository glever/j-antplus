package be.glever.antplus.hrm.common.datapage;

public class DataPage70Request extends AbstractDataPage {

	public DataPage70Request(byte[] dataPageBytes) {
		super(dataPageBytes);
	}

	@Override
	public int getPageNumber() {
		return 70;
	}
}
