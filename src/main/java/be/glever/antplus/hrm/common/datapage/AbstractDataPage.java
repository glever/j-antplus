package be.glever.antplus.hrm.common.datapage;

/**
 * Abstract super class for all DataPages.
 */
public abstract class AbstractDataPage {

	private byte[] dataPageBytes;

	public AbstractDataPage(byte[] dataPageBytes) {
		this.dataPageBytes = dataPageBytes;
	}

	public byte[] getDataPageBytes() {
		return dataPageBytes;
	}

	public abstract int getPageNumber();

	// TODO may be HRM  or main/background datapage specific. Pull down if necessary.
	public boolean getPageChangeToggle() {
		return (1 & getDataPageBytes()[1]) == 1;
	}
}
