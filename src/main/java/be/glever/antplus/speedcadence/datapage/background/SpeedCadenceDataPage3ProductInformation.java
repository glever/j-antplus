package be.glever.antplus.speedcadence.datapage.background;

import be.glever.antplus.hrm.datapage.AbstractHRMDataPage;
import be.glever.antplus.speedcadence.datapage.AbstractSpeedCadenceDataPage;

public class SpeedCadenceDataPage3ProductInformation extends AbstractSpeedCadenceDataPage {

    public static final byte PAGE_NR = 0x3;

    public SpeedCadenceDataPage3ProductInformation(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    public byte getHardwareVersion() {
        return super.getPageSpecificBytes()[0];
    }

    public byte getSoftwareVersion() {
        return super.getPageSpecificBytes()[1];
    }

    public byte getModelNumber() {
        return super.getPageSpecificBytes()[2];
    }

    @Override
    public byte getPageNumber() {
        return PAGE_NR;
    }

    @Override
    public String toString() {
        return String.format("%s, {%s, HardwareVersion=%s, SoftwareVersion=%s, ModelNumber=%s}", getClass().getSimpleName(), super.getToString(), getHardwareVersion(), getSoftwareVersion(), getModelNumber());
    }
}
