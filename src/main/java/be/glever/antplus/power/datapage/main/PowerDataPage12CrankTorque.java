package be.glever.antplus.power.datapage.main;

import be.glever.ant.util.ByteUtils;
import be.glever.antplus.power.PedalPower;
import be.glever.antplus.power.datapage.AbstractPowerDataPage;

/**
 * Default or unknown data page.
 * Used in transmission control patterns.
 * Contains no useful extra info over the generic {@link AbstractPowerDataPage}
 */
public class PowerDataPage12CrankTorque extends AbstractPowerDataPage {

    public static final byte PAGE_NR = 0x12;

    public PowerDataPage12CrankTorque(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    public int getUpdateEventCount() {
        return getPageSpecificBytes()[0];
    }

    /**
     * Rollover: 256
     */
    public int getCrankRevolutions() {
        return getPageSpecificBytes()[1];
    }

    /**
     * Instantaneous crank cadence. 255 if unavailable
     * Unit: RPM
     * Range: 0 - 254 RPM
     */
    public int getInstantaneousCadence() {
        return getPageSpecificBytes()[2];
    }

    /**
     * Rollower: 32s
     * Unit: 1/2048s
     */
    public double getCrankPeriod() {
        byte[] pageBytes = getPageSpecificBytes();
        return ByteUtils.fromUShort(pageBytes[3], pageBytes[4]);
    }

    /**
     * Rollover: 2048Nm
     * Unit: 1/32Nm
     */
    public double getAccumulatedTorque() {
        byte[] pageBytes = getPageSpecificBytes();
        return ByteUtils.fromUShort(pageBytes[5], pageBytes[6]);
    }

    @Override
    public byte getPageNumber() {
        return PAGE_NR;
    }
}
