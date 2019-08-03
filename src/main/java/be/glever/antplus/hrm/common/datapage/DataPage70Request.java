package be.glever.antplus.hrm.common.datapage;

import be.glever.antplus.common.datapage.AbstractAntPlusDataPage;

public class DataPage70Request extends AbstractAntPlusDataPage {

    public static final int PAGE_NUMBER = 70;

    public DataPage70Request(byte[] dataPageBytes) {
        super(dataPageBytes);
    }

    /**
     * DataPage to request specific datapage from device so as not to have to wait until device sends it itself.
     * Meant for faster display of info to user.
     *
     * @param timesToTransmitRequestedPage     (1-128)
     * @param useAcknowledgementMessages
     * @param transmitUntilAcknowledgeReceived If true, timesToTransmitRequestedPage and useAcknowledgementMessages are ignored.
     * @param requestedDataPage                The HRM common datapage to request
     */
    public DataPage70Request(int timesToTransmitRequestedPage, boolean useAcknowledgementMessages, boolean transmitUntilAcknowledgeReceived, int requestedDataPage) {
        super(createDataPageBytes(timesToTransmitRequestedPage, useAcknowledgementMessages, transmitUntilAcknowledgeReceived, (byte) requestedDataPage));
    }

    private static byte[] createDataPageBytes(int timesToTransmitRequestedPage, boolean useAcknowledgementMessages, boolean transmitUntilAcknowledgeReceived, byte requestedDataPage) {
        byte[] dataPageBytes = new byte[8];
        dataPageBytes[0] = (byte) PAGE_NUMBER;
        dataPageBytes[1] = (byte) 0xFF;
        dataPageBytes[2] = (byte) 0xFF;
        dataPageBytes[3] = (byte) 0xFF;
        dataPageBytes[4] = (byte) 0xFF;

        if (transmitUntilAcknowledgeReceived) {
            dataPageBytes[5] = (byte) 0x80;
        } else {
            byte timesToTransmitMask = 0b01111111;
            dataPageBytes[5] = (byte) (0b01111111 & timesToTransmitRequestedPage);

            byte useAckMsgMask = useAcknowledgementMessages ? (byte) ~timesToTransmitMask : 0;
            dataPageBytes[5] = (byte) (dataPageBytes[5] | useAckMsgMask);
        }

        dataPageBytes[6] = requestedDataPage;

        dataPageBytes[7] = 1;
        return dataPageBytes;
    }

    @Override
    public byte getPageNumber() {
        return PAGE_NUMBER;
    }
}
