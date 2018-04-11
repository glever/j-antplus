package be.glever.antplus.common.datapage;

public class DataPage83TimeAndDate extends AbstractAntPlusDataPage {
	public DataPage83TimeAndDate(byte[] dataPageBytes) {
		super(dataPageBytes);
	}

	@Override
	public byte getPageNumber() {
		return 83;
	}

	public byte getSeconds() {
		return getDataPageBytes()[2];
	}

	public byte getMinutes() {
		return getDataPageBytes()[3];
	}

	public byte getHours() {
		return getDataPageBytes()[4];
	}

	public byte getDayOfWeek() {
		return (byte) (getDataPageBytes()[5] & 0b00000111);
	}


	public byte getDayOfMonth() {
		return (byte) (getDataPageBytes()[5] & 0b11111000);
	}

	public byte getMonth() {
		return getDataPageBytes()[6];
	}


	public byte getYear() {
		return getDataPageBytes()[7];
	}
}
