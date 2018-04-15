package be.glever.antplus.hrm.datapage.background;

import be.glever.antplus.hrm.datapage.AbstractHRMDataPage;

import static be.glever.ant.util.ByteUtils.toInt;

public class HrmDataPage7BatteryStatus extends AbstractHRMDataPage {
	public static final byte PAGE_NR = 7;

	enum ANT_HRM_BATTERY_LEVEL {
		RESERVED,
		NEW,
		GOOD,
		OK,
		LOW,
		CRITICAL,
		INVALID
	}

	public HrmDataPage7BatteryStatus(byte[] dataPageBytes) {
		super(dataPageBytes);
	}

	@Override
	public byte getPageNumber() {
		return PAGE_NR;
	}

	/**
	 * @return Battery level in percentage or -1 if not used.
	 */
	public int getBatteryLevelPercentage() {
		byte batteryLevelByte = getPageSpecificBytes()[0];

		return batteryLevelByte == 0xFF ? -1 : toInt(batteryLevelByte);
	}

	/**
	 * @return Fractional battery voltage. Not sure what this is, perhaps just a better granularity (1/256V) than percentage? At the moment just return this byte toInt
	 */
	public int getFractionalBatteryVoltage() {
		return toInt(getPageSpecificBytes()[1]);
	}

	public ANT_HRM_BATTERY_LEVEL getBatteryVoltageDescription() {
		switch (toInt(getPageSpecificBytes()[2])) {
			case 0:
			case 6:
				return ANT_HRM_BATTERY_LEVEL.RESERVED;
			case 1:
				return ANT_HRM_BATTERY_LEVEL.NEW;
			case 2:
				return ANT_HRM_BATTERY_LEVEL.GOOD;
			case 3:
				return ANT_HRM_BATTERY_LEVEL.OK;
			case 4:
				return ANT_HRM_BATTERY_LEVEL.LOW;
			case 5:
				return ANT_HRM_BATTERY_LEVEL.CRITICAL;
			default:
				return ANT_HRM_BATTERY_LEVEL.CRITICAL;
		}
	}


}
