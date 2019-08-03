package be.glever.antplus.common.datapage;

public class DataPage74OpenChannelCommand extends AbstractAntPlusDataPage {
    public static final byte PAGE_NR = 74;

    public DataPage74OpenChannelCommand(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    @Override
    public byte getPageNumber() {
        return PAGE_NR;
    }

    public byte[] getLower3BytesOfSerialNumber() {
        return super.dataPageSubArray(1, 4);
    }

    public byte getDeviceType() {
        return getDataPageBytes()[4];
    }

    public byte getRfFrequency() {
        return getDataPageBytes()[5];
    }

    public byte[] getChannelPeriod() {
        return super.dataPageSubArray(6, 8);
    }
}
