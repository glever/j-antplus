package be.glever.antplus.common.datapage;

public class DataPage78MultiComponentSystemManufacturersInformation extends AbstractAntPlusDataPage {
    public static final byte PAGE_NR = 78;

    public DataPage78MultiComponentSystemManufacturersInformation(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    @Override
    public byte getPageNumber() {
        return PAGE_NR;
    }

    public byte getComponentIdentifier() {
        return getDataPageBytes()[2];
    }

    public byte getHwRevision() {
        return getDataPageBytes()[3];
    }

    public byte[] getManufacturerId() {
        return super.dataPageSubArray(4, 6);
    }

    public byte[] getModelNumber() {
        return dataPageSubArray(6, 8);
    }
}
