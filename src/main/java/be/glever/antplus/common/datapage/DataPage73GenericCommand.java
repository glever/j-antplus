package be.glever.antplus.common.datapage;

import java.util.Arrays;

public class DataPage73GenericCommand extends AbstractAntPlusDataPage {
	public DataPage73GenericCommand(byte[] dataPageBytes) {
		super(dataPageBytes);
	}

	@Override
	public byte getPageNumber() {
		return 73;
	}


	public byte[] getSlaveSerialNumber() {
		return Arrays.copyOfRange(getDataPageBytes(), 1, 3);
	}

	public byte[] getSlaveManufacturerId() {
		return super.dataPageSubArray(3, 5);

	}

	public byte getSequenceNr() {
		return getDataPageBytes()[5];
	}

	public byte[] getCommandNumber() {
		return super.dataPageSubArray(6, 8);
	}

}
