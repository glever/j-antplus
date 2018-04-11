package be.glever.antplus.common.datapage;

import java.util.Arrays;

public class DataPage68AntFsHostCommandResponse extends AbstractAntPlusDataPage{
	public DataPage68AntFsHostCommandResponse(byte[] dataPageBytes) {
		super(dataPageBytes);
	}

	@Override
	public byte getPageNumber() {
		return 68;
	}

	public byte getCommandOrResponseByte(){
		return getDataPageBytes()[1];
	}

	public byte[] getParameters(){
		return Arrays.copyOfRange(getDataPageBytes(), 2, 8);
	}
}
