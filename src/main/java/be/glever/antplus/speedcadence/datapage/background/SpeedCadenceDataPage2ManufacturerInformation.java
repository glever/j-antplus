package be.glever.antplus.speedcadence.datapage.background;

import be.glever.ant.util.ByteUtils;
import be.glever.antplus.hrm.datapage.AbstractHRMDataPage;
import be.glever.antplus.speedcadence.datapage.AbstractSpeedCadenceDataPage;

public class SpeedCadenceDataPage2ManufacturerInformation extends AbstractSpeedCadenceDataPage {

    public static final byte PAGE_NR = 0x2;

    public SpeedCadenceDataPage2ManufacturerInformation(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    // TODO: Is there a method to convert manufacturer ID to a string? We'd need a database of the official registry.
    public int getManufacturerId() {
        return super.getPageSpecificBytes()[0];
    }

    public int getSerialNumber() {
        return ByteUtils.toInt(getPageSpecificBytes()[1], getPageSpecificBytes()[2]);
    }

    @Override
    public byte getPageNumber() {
        return PAGE_NR;
    }

    @Override
    public String toString() {
        return String.format("%s, {%s, ManufacturerId=%s, SerialNumber=%s}", getClass().getSimpleName(), super.getToString(), getManufacturerId(), getSerialNumber());
    }
}
