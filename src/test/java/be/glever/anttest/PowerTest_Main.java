package be.glever.anttest;

import be.glever.ant.message.AntMessage;
import be.glever.ant.message.data.AchnowledgeDataMessage;
import be.glever.ant.message.data.BroadcastDataMessage;
import be.glever.ant.usb.AntUsbDevice;
import be.glever.ant.usb.AntUsbDeviceFactory;
import be.glever.antplus.common.datapage.AbstractAntPlusDataPage;
import be.glever.antplus.power.PedalPower;
import be.glever.antplus.power.PowerChannel;
import be.glever.antplus.power.datapage.AbstractPowerDataPage;
import be.glever.antplus.power.datapage.PowerDataPageRegistry;
import be.glever.antplus.power.datapage.main.*;
import be.glever.util.logging.Log;

import java.io.IOException;

import static java.lang.String.format;

public class PowerTest_Main {
    private static final Log LOG = Log.getLogger(HrmTest_Main.class);
    private int heartbeatCount;
    private PowerDataPageRegistry registry = new PowerDataPageRegistry();

    private boolean capsReported = false;

    private PowerTest_Main() throws IOException, InterruptedException {

        try (AntUsbDevice device = AntUsbDeviceFactory.getAvailableAntDevices().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("No devices found"))) {
            device.initialize();
            device.closeAllChannels(); // channels stay open on usb dongle even if program shuts down.
            PowerChannel channel = new PowerChannel(device);
            channel.getEvents().doOnNext(this::handle).subscribe();

            System.in.read();
        }
    }

    public static void main(String[] args) throws Exception {
        new PowerTest_Main();
    }

    private void handle(AntMessage antMessage) {
        if (antMessage instanceof BroadcastDataMessage) {
            BroadcastDataMessage msg = (BroadcastDataMessage) antMessage;
            byte[] payLoad = msg.getPayLoad();
            removeToggleBit(payLoad);
            AbstractAntPlusDataPage dataPage = registry.constructDataPage(payLoad);

            LOG.debug(() -> "Received datapage " + dataPage.toString());
            if (dataPage instanceof AbstractPowerDataPage) {
                AbstractPowerDataPage powerPage = ((AbstractPowerDataPage) dataPage);
            }
            if (dataPage instanceof PowerDataPage10PowerOnly) {
                PowerDataPage10PowerOnly powerPage = ((PowerDataPage10PowerOnly) dataPage);
                double accumulatedPower = powerPage.getAccumulatedPower();
                int updateEventCount = powerPage.getUpdateEventCount();
                int instantaneousCadence = powerPage.getInstantaneousCadence();
                double instantaneousPower = powerPage.getInstantaneousPower();
                PedalPower powerDistribution = powerPage.getPedalPowerDistribution();

                System.out.println("Accumulated power: " + accumulatedPower);
                System.out.println("Update event count: " + updateEventCount);
                System.out.println("Instantaneous cadence: " + instantaneousCadence);
                System.out.println("Instantaneous power: " + instantaneousPower);

                System.out.println("Zero is left: " + powerDistribution.getZeroIsLeft());
                System.out.println("Power distribution: " + powerDistribution.getPercentage());
                System.out.println();
            }
            if (dataPage instanceof PowerDataPage11WheelTorque) {
                PowerDataPage11WheelTorque powerPage = ((PowerDataPage11WheelTorque) dataPage);
            }
            if (dataPage instanceof PowerDataPage12CrankTorque) {
                PowerDataPage12CrankTorque powerPage = ((PowerDataPage12CrankTorque) dataPage);
            }
            if (dataPage instanceof PowerDataPage13TorqueEffectivenessPedalSmoothness) {
                PowerDataPage13TorqueEffectivenessPedalSmoothness powerPage = ((PowerDataPage13TorqueEffectivenessPedalSmoothness) dataPage);
            }
            if (dataPage instanceof PowerDataPage20CrankTorqueFrequency) {
                PowerDataPage20CrankTorqueFrequency powerPage = ((PowerDataPage20CrankTorqueFrequency) dataPage);
            }

        } else if (antMessage instanceof AchnowledgeDataMessage) {
            AchnowledgeDataMessage msg = (AchnowledgeDataMessage) antMessage;
            byte[] payLoad = msg.getPayLoad();
            removeToggleBit(payLoad);
            AbstractAntPlusDataPage dataPage = registry.constructDataPage(payLoad);
        } else {
            LOG.warn(()->format("Ignoring message  %s", antMessage));
        }
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
