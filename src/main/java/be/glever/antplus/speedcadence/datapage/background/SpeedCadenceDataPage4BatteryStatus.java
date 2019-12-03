package be.glever.antplus.speedcadence.datapage.background;

import be.glever.antplus.speedcadence.datapage.AbstractSpeedCadenceDataPage;

import static be.glever.ant.util.ByteUtils.toInt;

public class SpeedCadenceDataPage4BatteryStatus extends AbstractSpeedCadenceDataPage {
    public static final byte PAGE_NR = 4;

    // TODO: Combine with HRM battery level, it's the same
    enum ANT_SPEED_CADENCE_BATTERY_LEVEL {
        RESERVED,
        NEW,
        GOOD,
        OK,
        LOW,
        CRITICAL,
        INVALID
    }

    public SpeedCadenceDataPage4BatteryStatus(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    @Override
    public byte getPageNumber() {
        return PAGE_NR;
    }

    /**
     * Range: 0-255
     * Unit: 1/256 V
     * @return fractional battery voltage.
     */
    public int getFractionalBatteryVoltage() {
        // TODO: Convert into something useful
        byte batteryLevelByte = getPageSpecificBytes()[0];

        return batteryLevelByte == (byte) 0xFF ? -1 : toInt(batteryLevelByte);
    }

    public ANT_SPEED_CADENCE_BATTERY_LEVEL getBatteryVoltageDescription() {
        switch (toInt(getPageSpecificBytes()[2])) {
            case 0:
            case 6:
                return ANT_SPEED_CADENCE_BATTERY_LEVEL.RESERVED;
            case 1:
                return ANT_SPEED_CADENCE_BATTERY_LEVEL.NEW;
            case 2:
                return ANT_SPEED_CADENCE_BATTERY_LEVEL.GOOD;
            case 3:
                return ANT_SPEED_CADENCE_BATTERY_LEVEL.OK;
            case 4:
                return ANT_SPEED_CADENCE_BATTERY_LEVEL.LOW;
            case 5:
                return ANT_SPEED_CADENCE_BATTERY_LEVEL.CRITICAL;
            case 7:
            default:
                return ANT_SPEED_CADENCE_BATTERY_LEVEL.INVALID;
        }
    }
}
