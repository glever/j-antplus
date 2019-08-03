package be.glever.antplus.common.datapage;

public class DataPage87ErrorDescription extends AbstractAntPlusDataPage {
    public static final byte PAGE_NR = 87;

    public DataPage87ErrorDescription(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    @Override
    public byte getPageNumber() {
        return PAGE_NR;
    }


    // TODO extract meaningful value from bits 0:3
    public byte getSystemComponentIndex() {
        return getDataPageBytes()[2];
    }

    // TODO extract meaningful value from bits 6:7
    public byte getErrorLevel() {
        return getDataPageBytes()[2];
    }

    public byte getProfileSpecificErrorCodes() {
        return getDataPageBytes()[3];
    }

    public byte[] getManufacturerSpecificErrorCodes() {
        return super.dataPageSubArray(4, 8);
    }


}
