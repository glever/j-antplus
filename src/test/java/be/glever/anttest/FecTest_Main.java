package be.glever.anttest;

import be.glever.ant.message.AntMessage;
import be.glever.ant.message.data.BroadcastDataMessage;
import be.glever.ant.usb.AntUsbDevice;
import be.glever.ant.usb.AntUsbDeviceFactory;
import be.glever.antplus.common.datapage.AbstractAntPlusDataPage;
import be.glever.antplus.fec.EquipmentType;
import be.glever.antplus.fec.FecChannel;
import be.glever.antplus.fec.FecState;
import be.glever.antplus.fec.HeartRateDataSource;
import be.glever.antplus.fec.datapage.AbstractFecDataPage;
import be.glever.antplus.fec.datapage.FecDataPageRegistry;
import be.glever.antplus.fec.datapage.main.FecDataPage16GeneralFeData;
import be.glever.antplus.fec.datapage.main.FecDataPage25Bike;
import be.glever.util.logging.Log;

import java.io.IOException;
import java.util.Optional;

import static java.lang.String.format;

public class FecTest_Main {
    private static final Log LOG = Log.getLogger(HrmTest_Main.class);
    private FecDataPageRegistry registry = new FecDataPageRegistry();

    private boolean capsReported = false;

    private FecTest_Main() throws IOException, InterruptedException {

        try (AntUsbDevice device = AntUsbDeviceFactory.getAvailableAntDevices().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("No devices found"))) {
            device.initialize();
            device.closeAllChannels(); // channels stay open on usb dongle even if program shuts down.
            FecChannel channel = new FecChannel(device);
            channel.getEvents().doOnNext(this::handle).subscribe();

            System.in.read();
        }
    }

    public static void main(String[] args) throws Exception {
        new FecTest_Main();
    }

    private void handle(AntMessage antMessage) {
        if (antMessage instanceof BroadcastDataMessage) {
            BroadcastDataMessage msg = (BroadcastDataMessage) antMessage;
            byte[] payLoad = msg.getPayLoad();
            removeToggleBit(payLoad);

            AbstractAntPlusDataPage dataPage = registry.constructDataPage(payLoad);
            if (dataPage == null)
                return;

            LOG.debug(() -> "Received datapage " + dataPage.toString());
            if (dataPage instanceof AbstractFecDataPage) {
                AbstractFecDataPage fecDataPage = ((AbstractFecDataPage) dataPage);
            }

            if (dataPage instanceof FecDataPage25Bike) {
                FecDataPage25Bike fecDataPage = ((FecDataPage25Bike) dataPage);
                int instantaneousPower = fecDataPage.getInstantaneousPower();
                int accumulatedPower = fecDataPage.getAccumulatedPower();
                int instantaneousCadence = fecDataPage.getInstantaneousCadence();

                System.out.println("Instant power: " + instantaneousPower);
                System.out.println("Accumulated power: " + accumulatedPower);
                System.out.println("Instant cadence: " + instantaneousCadence);
                System.out.println();
            }
            if (dataPage instanceof FecDataPage16GeneralFeData) {
                FecDataPage16GeneralFeData fecDataPage = ((FecDataPage16GeneralFeData) dataPage);
                boolean distanceTraveledEnabled = fecDataPage.isDistanceTraveledEnabled();
                boolean isVirtualSpeed = fecDataPage.isVirtualSpeed();
                Optional<HeartRateDataSource> heartRateSource = fecDataPage.getHeartRateSource();
                int distance = fecDataPage.getDistanceTravelled();
                int time = fecDataPage.getElapsedTime();
                Optional<EquipmentType> equipmentType = fecDataPage.getEquipmentType();
                Optional<FecState> fecState = fecDataPage.getFecState();
                int heartRate = fecDataPage.getHeartRate();
                boolean lapToggle = fecDataPage.getLapToggle();
                double speed = fecDataPage.getSpeed();

                System.out.println("Is distance travelled enabled: " + distanceTraveledEnabled);
                System.out.println("Is virtual speed: " + isVirtualSpeed);
                System.out.println("HeartRate source: " + heartRateSource);
                System.out.println("Distance: " + distance);
                System.out.println("Elapsed time: " + time);
                System.out.println("Equipment type: " + equipmentType);
                System.out.println("FE-C State: " + fecState);
                System.out.println("Heart rate: " + heartRate);
                System.out.println("Lap toggle: " + lapToggle);
                System.out.println("Speed: " + speed);
                System.out.println();
            }

        } else {
            LOG.warn(()->format("Ignoring message  %s", antMessage));
        }
    }

    /**
     * For the moment not taking the legacy hrm devices into account.
     * Non-legacy devices swap the first bit of the pageNumber every 4 messages.
     */
    private void removeToggleBit(byte[] payLoad) {
        payLoad[0] = (byte) (0b01111111 & payLoad[0]);
    }

}
