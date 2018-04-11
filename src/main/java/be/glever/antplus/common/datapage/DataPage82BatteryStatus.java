package be.glever.antplus.common.datapage;

public class DataPage82BatteryStatus extends AbstractAntPlusDataPage {
	public DataPage82BatteryStatus(byte[] dataPageBytes) {
		super(dataPageBytes);
	}

	@Override
	public byte getPageNumber() {
		return 82;
	}


	public byte getBatteryIdentifier(){
		return getDataPageBytes()[2];
	}

	public byte[] getCumulativeOperatingTime(){
		return super.dataPageSubArray(3,6);
	}


	public byte getFractionalBatteryVoltage(){
		return getDataPageBytes()[6];
	}


	public byte getDescriptiveBitField(){
		return getDataPageBytes()[7];
	}
}
