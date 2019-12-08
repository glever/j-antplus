package be.glever.antplus.fec.datapage.main;

import be.glever.ant.util.ByteUtils;
import be.glever.antplus.fec.EquipmentType;
import be.glever.antplus.fec.FecState;
import be.glever.antplus.fec.HeartRateDataSource;
import be.glever.antplus.power.datapage.AbstractPowerDataPage;

import java.util.Optional;

/**
 * Default or unknown data page.
 * Used in transmission control patterns.
 * Contains no useful extra info over the generic {@link AbstractPowerDataPage}
 */
public class FecDataPage25Bike extends AbstractPowerDataPage {

    public static final byte PAGE_NR = 25;

    public FecDataPage25Bike(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    /**
     * Rollover: 256
     */
    public int getUpddateEventCount() {
        return getPageSpecificBytes()[0];
    }

    /**
     * Unit: RPM
     * Range: 0-254, 255 invalid
     */
    public int getInstantaneousCadence() {
        return getPageSpecificBytes()[1];
    }

    /**
     * Unit: Watt
     * Rollover: 65536W
     * 0xFFF invalid
     */
    public int getAccumulatedPower() {
        if (getInstantaneousPower() == 0xFFF)
            return 0xFFF;

        return ByteUtils.fromUShort(getPageSpecificBytes()[2], getPageSpecificBytes()[3]);
    }

    /**
     * Unit: Watt
     * Range: 0-4094, 0xFFF invalid
     */
    public int getInstantaneousPower() {
        return ByteUtils.fromUShort(getPageSpecificBytes()[4], (byte) (getPageSpecificBytes()[5] & 0b00001111));
    }

    //public boolean getPowerCalibrationRequired() {

    //}

    //public boolean getResistanceCalibrationRequired() {

    //}

    //public boolean getUserConfigurationRequired() {

    //}

    // TODO: Flags bit field
    // TODO: FE State bit field

    @Override
    public byte getPageNumber() {
        return PAGE_NR;
    }
}
