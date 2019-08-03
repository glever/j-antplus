package be.glever.antplus.common.datapage;

public class DataPage85MemoryLevel extends AbstractAntPlusDataPage {
    public static final byte PAGE_NR = 85;

    public DataPage85MemoryLevel(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    @Override
    public byte getPageNumber() {
        return PAGE_NR;
    }


    public byte getPercentUsed() {
        return getDataPageBytes()[4];
    }

    public byte[] getTotalSize() {
        return super.dataPageSubArray(5, 7);
    }


    public byte getTotalSizeUnit() {
        return getDataPageBytes()[7];
    }
}
