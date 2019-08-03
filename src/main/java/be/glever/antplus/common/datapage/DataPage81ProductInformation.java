package be.glever.antplus.common.datapage;

public class DataPage81ProductInformation extends AbstractAntPlusDataPage {
    public static final byte PAGE_NR = 81;

    public DataPage81ProductInformation(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    @Override
    public byte getPageNumber() {
        return PAGE_NR;
    }

    public byte getSupplementalSwRevision() {
        return getDataPageBytes()[2];
    }

    public byte getMainSwRevision() {
        return getDataPageBytes()[3];
    }

    public byte[] getLowest32BitsOfSerialNumber() {
        return super.dataPageSubArray(4, 8);
    }
}
