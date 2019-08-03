package be.glever.antplus.common.datapage;

import java.util.Arrays;

public class DataPage70Request extends AbstractAntPlusDataPage {
    public static final byte PAGE_NR = 70;

    public DataPage70Request(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    @Override
    public byte getPageNumber() {
        return PAGE_NR;
    }

    public byte[] getSlaveSerialNumber() {
        return Arrays.copyOfRange(getDataPageBytes(), 1, 3);
    }

    public byte getDescriptorByte1() {
        return getDataPageBytes()[3];
    }

    public byte getDescriptorByte2() {
        return getDataPageBytes()[4];
    }

    public byte getRequestedTransmissionResponse() {
        return getDataPageBytes()[5];
    }

    public byte getRequestedPageNumber() {
        return getDataPageBytes()[6];
    }

    public byte getCommandType() {
        return getDataPageBytes()[7];
    }
}
