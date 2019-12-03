package be.glever.antplus.speedcadence.datapage.main;

import be.glever.antplus.speedcadence.datapage.AbstractSpeedCadenceDataPage;

/**
 * Default or unknown data page.
 * Used in transmission control patterns.
 * Contains no useful extra info over the generic {@link AbstractSpeedCadenceDataPage}
 */
public class SpeedCadenceDataPage0Default extends AbstractSpeedCadenceDataPage {

    public static final byte PAGE_NR = 0;

    public SpeedCadenceDataPage0Default(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    @Override
    public byte getPageNumber() {
        return PAGE_NR;
    }
}
