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
public class FecDataPage16GeneralFeData extends AbstractPowerDataPage {

    public static final byte PAGE_NR = 16;

    public FecDataPage16GeneralFeData(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    public Optional<EquipmentType> getEquipmentType() {
        byte equipmentTypeBitField = getPageSpecificBytes()[0];
        byte mask = (byte) 0b00001111;  // bits 5-7 are reserved
        int masked = equipmentTypeBitField & mask;
        return EquipmentType.valueOf(masked);
    }

    /**
     * Accumulated time since the start of the workout
     *
     * Unit: Seconds
     * Resolution: 1/4s
     * Rollover: 64s
     */
    public int getElapsedTime() {
        return 4 * getPageSpecificBytes()[1];
    }

    /**
     * Accumulated time since the start of the workout
     *
     * Available for at least rowers, cycle trainers, treadmills and nordic skiers
     *
     * Resolution: 1m
     * Unit: Meters
     * Rollover: 256m
     */
    public int getDistanceTravelled() {
        return getPageSpecificBytes()[2];
    }

    /**
     * Current speed, 0xFFFF is invalid
     * Unit: m/s
     * Resolution 1/1000 m/s
     * Range: 0 - 65.534 m/s
     */
    public double getSpeed() {
        return ByteUtils.fromUShort(getPageSpecificBytes()[3], getPageSpecificBytes()[4]) / 1000.0;
    }

    /**
     * Heart rate from HRM or hand contact sensor
     *
     * Unit: bpm
     * Resolution 1bpm
     * Range: 0-254 bpm
     */
    public int getHeartRate() {
        return getPageSpecificBytes()[5];
    }

    public Optional<HeartRateDataSource> getHeartRateSource() {
        byte capabilities = getPageSpecificBytes()[6];
        return HeartRateDataSource.valueOf(capabilities & 0b00000011);
    }

    public boolean isDistanceTraveledEnabled() {
        byte capabilities = getPageSpecificBytes()[6];
        return ByteUtils.hasBitSet(capabilities, 2);
    }

    public boolean isVirtualSpeed() {
        byte capabilities = (byte) (getPageSpecificBytes()[6] & 0x0F);
        return ByteUtils.hasBitSet(capabilities, 3);
    }

    public Optional<FecState> getFecState() {
        byte fecState = (byte) (getPageSpecificBytes()[6] >> 4);
        return FecState.valueOf(fecState & 0b0000011);
    }

    public boolean getLapToggle() {
        return ByteUtils.hasBitSet(getPageSpecificBytes()[6] >> 4, 3);
    }


    @Override
    public byte getPageNumber() {
        return PAGE_NR;
    }
}
