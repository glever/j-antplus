package be.glever.antplus.power.datapage.main;

import be.glever.ant.util.ByteUtils;
import be.glever.antplus.power.datapage.AbstractPowerDataPage;

/**
 * Default or unknown data page.
 * Used in transmission control patterns.
 * Contains no useful extra info over the generic {@link AbstractPowerDataPage}
 */
public class PowerDataPage13TorqueEffectivenessPedalSmoothness extends AbstractPowerDataPage {


    public static final byte PAGE_NR = 0x13;

    public PowerDataPage13TorqueEffectivenessPedalSmoothness(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    public int getUpdateEventCount() {
        return getPageSpecificBytes()[0];
    }

    /**
     * Left crank torque effectiveness
     *
     * Unit: 0.5%
     * Range: 0-100%
     */
    public int getLeftTorqueEffectiveness() {
        return getPageSpecificBytes()[1];
    }

    /**
     * Right crank torque effectiveness
     *
     * Unit: 0.5%
     * Range: 0-100%
     */
    public int getRightTorqueEffectiveness() {
        return getPageSpecificBytes()[2];
    }

    /**
     * Torque smoothness of the left crank, invalid if 0xFF
     *
     * Unit: 0.5%
     * Range: 0-100%
     */
    public int getLeftPedalSmoothness() {
        if (getRightPedalSmoothness() == 0xFF)
            return 0xFF;

        return getPageSpecificBytes()[3];
    }

    /**
     * Torque smoothness of the right crank, invalid if 0xFF
     *
     * Unit: 0.5%
     * Range: 0-100%
     */
    public int getRightPedalSmoothness() {
        return getPageSpecificBytes()[4];
    }

    /**
     * Combined torque smoothness of the left and right crank, invalid if 0xFF
     *
     * Unit: 0.5%
     * Range: 0-100%
     */
    public int getCombinedPedalSmoothness() {
        if (getRightPedalSmoothness() != 0xFF)
            return 0xFF;

        return getPageSpecificBytes()[3];
    }

    @Override
    public byte getPageNumber() {
        return PAGE_NR;
    }
}
