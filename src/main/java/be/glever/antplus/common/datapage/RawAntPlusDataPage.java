package be.glever.antplus.common.datapage;

/**
 * Basic DataPage representation.
 * This instance must typically be converted to a more specific datapage when the type of the Ant+ Device is known.
 */
public class RawAntPlusDataPage extends AbstractAntPlusDataPage {
	public RawAntPlusDataPage(byte[] dataPageBytes) {
		super(dataPageBytes);
	}

	@Override
	public byte getPageNumber() {
		return getDataPageBytes()[0];
	}
}
