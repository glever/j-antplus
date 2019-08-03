package be.glever.antplus.common.datapage;

import java.util.Arrays;

public class DataPage71CommandStatus extends AbstractAntPlusDataPage {
    public static final byte PAGE_NR = 71;

    public DataPage71CommandStatus(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    @Override
    public byte getPageNumber() {
        return PAGE_NR;
    }


    public byte getLastReceivedCommandId() {
        return getDataPageBytes()[1];
    }

    public byte getSequenceNumber() {
        return getDataPageBytes()[2];
    }

    public byte getCommandStatus() {
        return getDataPageBytes()[3];
    }

    public byte[] getData() {
        return Arrays.copyOfRange(getDataPageBytes(), 4, 8);
    }
}
