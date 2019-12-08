package be.glever.antplus.speedcadence.datapage.main;

import be.glever.ant.util.ByteUtils;
import be.glever.antplus.speedcadence.datapage.AbstractSpeedCadenceDataPage;

public class SpeedCadenceDataPage5Motion extends AbstractSpeedCadenceDataPage {

    public static final byte PAGE_NR = 5;

    public SpeedCadenceDataPage5Motion(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    /**
     * Whether the wheel or crank is moving
     *
     * @return whether the wheel or crank is moving
     */
    public boolean isMoving() {
        //return 0 == (getPageSpecificBytes()[0] & 0b0000001);
        return !ByteUtils.hasBitSet(getPageSpecificBytes()[0], 0);
    }

    @Override
    public byte getPageNumber() {
        return PAGE_NR;
    }
}
