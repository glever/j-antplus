package be.glever.antplus.common.datapage;

public class DataPage86GetPairedDevices extends AbstractAntPlusDataPage {
    public static final byte PAGE_NR = 86;

    public DataPage86GetPairedDevices(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    @Override
    public byte getPageNumber() {
        return PAGE_NR;
    }


    public byte getPeripheralDeviceIndex() {
        return getDataPageBytes()[1];
    }


    public byte getTotalNumberOfConnectedDevices() {
        return getDataPageBytes()[2];
    }

    public byte getChannelState() {
        return getDataPageBytes()[3];
    }


    public byte[] getPeripheralDeviceIdDeviceNumber() {
        return super.dataPageSubArray(4, 6);
    }

    public byte getPeripheralDeviceIdTransmissionType() {
        return getDataPageBytes()[6];
    }

    public byte getPeripheralDeviceIdDeviceType() {
        return getDataPageBytes()[7];
    }
}
