package be.glever.antplus.hrm.datapage.background;

import be.glever.antplus.hrm.datapage.AbstractHRMDataPage;

public class HrmDataPage3ProductInformation extends AbstractHRMDataPage {

    public static final byte PAGE_NR = 0x3;

    public HrmDataPage3ProductInformation(byte[] dataPageBytes) {
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
