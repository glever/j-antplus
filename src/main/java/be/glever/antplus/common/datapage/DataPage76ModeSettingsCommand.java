package be.glever.antplus.common.datapage;

import be.glever.antplus.common.datapage.AbstractAntPlusDataPage;

public class DataPage76ModeSettingsCommand extends AbstractAntPlusDataPage {

	public enum SPORT_MODE {
		RUNNING, CYCLING, SWIMMING;
	}

	public DataPage76ModeSettingsCommand(byte[] dataPageBytes) {
		super(dataPageBytes);
	}

	public DataPage76ModeSettingsCommand(SPORT_MODE mode) {
		super(createDataPageBytes(mode));
	}

	private static byte[] createDataPageBytes(SPORT_MODE mode) {
		byte[] bytes = new byte[8];

		bytes[0] = 76;
		bytes[1] = (byte) 0xFF;
		bytes[2] = (byte) 0xFF;
		bytes[3] = (byte) 0xFF;
		bytes[4] = (byte) 0xFF;
		bytes[5] = (byte) 0xFF;
		bytes[6] = (byte) 0xFF;

		byte byte8 = 0;
		switch (mode) {
			case CYCLING:
				byte8 = 0x01;
				break;
			case RUNNING:
				byte8 = 0x02;
				break;
			case SWIMMING:
				byte8 = 0x05;
				break;
			default:
				throw new IllegalArgumentException("Unsupported sport mode given: " + mode);
		}
		bytes[7] = byte8;

		return bytes;
	}

	@Override
	public byte getPageNumber() {
		return 76;
	}
}
