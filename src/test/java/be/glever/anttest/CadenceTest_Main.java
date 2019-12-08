package be.glever.anttest;

import be.glever.ant.message.AntMessage;
import be.glever.ant.message.data.BroadcastDataMessage;
import be.glever.ant.usb.AntUsbDevice;
import be.glever.ant.usb.AntUsbDeviceFactory;
import be.glever.antplus.common.datapage.AbstractAntPlusDataPage;
import be.glever.antplus.common.datapage.DataPage78MultiComponentSystemManufacturersInformation;
import be.glever.antplus.speedcadence.CadenceChannel;
import be.glever.antplus.speedcadence.SpeedChannel;
import be.glever.antplus.speedcadence.datapage.AbstractSpeedCadenceDataPage;
import be.glever.antplus.speedcadence.datapage.SpeedCadenceDataPageRegistry;
import be.glever.antplus.speedcadence.datapage.main.SpeedAndCadenceDataPage0Default;
import be.glever.antplus.speedcadence.datapage.main.SpeedCadenceDataPage5Motion;
import be.glever.util.logging.Log;

import java.io.IOException;
import java.nio.channels.Channel;
import java.util.List;

public class CadenceTest_Main {
    private static final Log LOG = Log.getLogger(CadenceTest_Main.class);
    private SpeedCadenceDataPageRegistry registry = new SpeedCadenceDataPageRegistry();

    private int prevCadenceRevCount = 0;
    private int firstCadenceRevCount = 0;
    private long prevCadenceEventTime = 0;

    private CadenceTest_Main() throws IOException {
        List<AntUsbDevice> foo = AntUsbDeviceFactory.getAvailableAntDevices();
        try (AntUsbDevice device = foo.stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("No devices found"))) {
            device.initialize();
            device.closeAllChannels(); // channels stay open on usb dongle even if program shuts down.
            CadenceChannel channel = new CadenceChannel(device);
            channel.getEvents().doOnNext(this::handle).subscribe();
            System.in.read();
        }
    }

    public static void main(String[] args) throws Exception {
        new CadenceTest_Main();
    }

    private void handle(AntMessage antMessage) {
        if (antMessage instanceof BroadcastDataMessage) {
            BroadcastDataMessage msg = (BroadcastDataMessage) antMessage;
            //System.out.println(msg.getMessageId());
            byte[] payLoad = msg.getPayLoad();
            removeToggleBit(payLoad);
            AbstractAntPlusDataPage dataPage = registry.constructDataPage(payLoad);

            //LOG.debug(() -> "Received datapage " + dataPage.toString());
            if (dataPage instanceof DataPage78MultiComponentSystemManufacturersInformation) {
                DataPage78MultiComponentSystemManufacturersInformation infoPage = (DataPage78MultiComponentSystemManufacturersInformation) dataPage;
                System.out.println("Manufacturer ID: " + infoPage.getManufacturerId());
                System.out.println("Model Number: " + infoPage.getModelNumber());
            }
            if (dataPage instanceof AbstractSpeedCadenceDataPage) {
                calcCadence((AbstractSpeedCadenceDataPage) dataPage);

            }
            if (dataPage instanceof SpeedCadenceDataPage5Motion) {
                System.out.println("The crank is moving? " + ((SpeedCadenceDataPage5Motion) dataPage).isMoving());
            }
        } else {
            //LOG.warn(()->format("Ignoring message  %s", antMessage));
        }
    }

    private void calcCadence(AbstractSpeedCadenceDataPage dataPage) {
        int curCadenceRevCount = dataPage.getCumulativeRevolutions();

        if (firstCadenceRevCount == 0)
            firstCadenceRevCount = curCadenceRevCount;

        // Skip this, if we get the same measurement as last time
        if (prevCadenceRevCount == curCadenceRevCount)
            return;

        long speedEventTime = dataPage.getEventTime();

        // Can only calculate speed, if we've actually moved yet
        double cadence = (prevCadenceEventTime == 0) ? 0 : calculateCadence(prevCadenceRevCount, curCadenceRevCount, prevCadenceEventTime, speedEventTime);

        System.out.println("The crank is being rotated at " + cadence + " RPM.");

        prevCadenceRevCount = curCadenceRevCount;
        prevCadenceEventTime = speedEventTime;
    }

    private double calculateCadence(int prevRevCount, int curRevCount, long prevTime, long curTime) {
        double timeDiff = curTime - prevTime;
        int revDiff = curRevCount - prevRevCount;
        return 1000 * 60 * (revDiff / timeDiff);
    }

    /**
     * For the moment not taking the legacy hrm devices into account.
     * Non-legacy devices swap the first bit of the pageNumber every 4 messages.
     *
     * @param payLoad
     */
    private void removeToggleBit(byte[] payLoad) {
        payLoad[0] = (byte) (0b01111111 & payLoad[0]);
    }
}
