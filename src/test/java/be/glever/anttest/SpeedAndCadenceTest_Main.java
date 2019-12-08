package be.glever.anttest;

import be.glever.ant.message.AntMessage;
import be.glever.ant.message.data.BroadcastDataMessage;
import be.glever.ant.usb.AntUsbDevice;
import be.glever.ant.usb.AntUsbDeviceFactory;
import be.glever.antplus.common.datapage.AbstractAntPlusDataPage;
import be.glever.antplus.speedcadence.SpeedChannel;
import be.glever.antplus.speedcadence.datapage.SpeedAndCadenceDataPageRegistry;
import be.glever.antplus.speedcadence.datapage.main.SpeedAndCadenceDataPage0Default;
import be.glever.util.logging.Log;

import java.io.IOException;
import java.util.List;

/**
 * Combined speed and cadence sensor. UNTESTED!
 */
public class SpeedAndCadenceTest_Main {
    private static final Log LOG = Log.getLogger(SpeedAndCadenceTest_Main.class);
    private SpeedAndCadenceDataPageRegistry registry = new SpeedAndCadenceDataPageRegistry();

    private int prevSpeedRevCount = 0;
    private int firstSpeedRevCount = 0;
    private long prevSpeedEventTime = 0;

    private int prevCadenceRevCount = 0;
    private int firstCadenceRevCount = 0;
    private long prevCadenceEventTime = 0;

    private SpeedAndCadenceTest_Main() throws IOException {
        try (AntUsbDevice device = AntUsbDeviceFactory.getAvailableAntDevices().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("No devices found"))) {
            device.initialize();
            device.closeAllChannels(); // channels stay open on usb dongle even if program shuts down.
            SpeedChannel channel = new SpeedChannel(device);
            channel.getEvents().doOnNext(this::handle).subscribe();
            System.in.read();
        }
    }

    public static void main(String[] args) throws Exception {
        new SpeedAndCadenceTest_Main();
    }

    private void handle(AntMessage antMessage) {
        if (antMessage instanceof BroadcastDataMessage) {
            BroadcastDataMessage msg = (BroadcastDataMessage) antMessage;
            byte[] payLoad = msg.getPayLoad();
            removeToggleBit(payLoad);
            AbstractAntPlusDataPage dataPage = registry.constructDataPage(payLoad);

            LOG.debug(() -> "Received datapage " + dataPage.toString());
            if (dataPage instanceof SpeedAndCadenceDataPage0Default) {
                calcSpeedDistance((SpeedAndCadenceDataPage0Default) dataPage, 69);
                calcCadence((SpeedAndCadenceDataPage0Default) dataPage);
            }
        } else {
            //LOG.warn(()->format("Ignoring message  %s", antMessage));
        }
    }

    private void calcSpeedDistance(SpeedAndCadenceDataPage0Default dataPage, double diameter) {
        int curSpeedRevCount = dataPage.getCumulativeSpeedRevolutions();

        if (firstSpeedRevCount == 0)
            firstSpeedRevCount = curSpeedRevCount;

        // Skip this, if we get the same measurement as last time
        if (prevSpeedRevCount == curSpeedRevCount)
            return;

        double circumference = Math.PI * diameter;
        long speedEventTime = dataPage.getSpeedEventTime();

        // Can only calculate speed, if we've actually moved yet
        double speed = (prevSpeedEventTime == 0) ? 0 : calculateSpeed(circumference, prevSpeedRevCount, curSpeedRevCount, prevSpeedEventTime, speedEventTime);
        double kmhSpeed = speed * 3.6;
        double travelledDistance = calculateDistance(circumference, curSpeedRevCount, firstSpeedRevCount);

        System.out.println("The bike is currently (maybe) moving at " + kmhSpeed + " km/h and has travelled " + travelledDistance + " m.");

        prevSpeedRevCount = curSpeedRevCount;
        prevSpeedEventTime = speedEventTime;
    }

    private void calcCadence(SpeedAndCadenceDataPage0Default dataPage) {
        int curCadenceRevCount = dataPage.getCumulativeCadenceRevolutions();

        if (firstCadenceRevCount == 0)
            firstCadenceRevCount = curCadenceRevCount;

        // Skip this, if we get the same measurement as last time
        if (prevCadenceRevCount == curCadenceRevCount)
            return;

        long speedEventTime = dataPage.getCadenceEventTime();

        // Can only calculate speed, if we've actually moved yet
        double cadence = (prevCadenceEventTime == 0) ? 0 : calculateCadence(prevCadenceRevCount, curCadenceRevCount, prevCadenceEventTime, speedEventTime);

        System.out.println("The crank is being rotated at " + cadence + " RPM.");

        prevCadenceRevCount = curCadenceRevCount;
        prevCadenceEventTime = speedEventTime;
    }

    /**
     * Calculate the speed in m/s from a current and a previous measurement
     *
     * @param circumference of the wheel
     * @param prevRevCount
     * @param curRevCount
     * @param prevTime
     * @param curTime
     * @return
     */
    private double calculateSpeed(double circumference, int prevRevCount, int curRevCount, long prevTime, long curTime) {
        double timeDiff = curTime - prevTime;
        double revDiff = curRevCount - prevRevCount;
        return 1000 * circumference * (revDiff / timeDiff);
    }

    private double calculateDistance(double circumference, int curRevCount, int firstRevCount) {
        return circumference * (curRevCount - firstRevCount);
    }

    private double calculateCadence(int prevRevCount, int curRevCount, long prevTime, long curTime) {
        double timeDiff = curTime - prevTime;
        int revDiff = curRevCount - prevRevCount;
        return 60 * (revDiff / timeDiff);
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
