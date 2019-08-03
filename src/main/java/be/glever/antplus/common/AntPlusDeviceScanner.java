package be.glever.antplus.common;

import be.glever.ant.AntException;
import be.glever.ant.channel.AntChannel;
import be.glever.ant.message.channel.ChannelEventOrResponseMessage;
import be.glever.ant.message.channel.ChannelEventResponseCode;
import be.glever.ant.message.configuration.AssignChannelMessage;
import be.glever.ant.message.control.OpenRxScanModeMessage;
import be.glever.ant.message.control.RequestMessage;
import be.glever.ant.message.requestedresponse.ChannelStatusMessage;
import be.glever.ant.usb.AntUsbDevice;
import be.glever.ant.usb.AntUsbDeviceCapabilities;
import be.glever.antplus.hrm.HRMChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class AntPlusDeviceScanner {
    private static final Logger LOG = LoggerFactory.getLogger(AntPlusDeviceScanner.class);

    private AntUsbDevice antUsbDevice;

    public AntPlusDeviceScanner(AntUsbDevice antUsbDevice) {
        this.antUsbDevice = antUsbDevice;
    }

    /**
     * Scans the Ant+ Network for all Ant+ devices.
     * For each found device, an AntChannel will be  assigned, until no more devices found or the max number of channels reached (see {@link AntUsbDevice#getCapabilities()}.
     * If you need to support more devices than {@link AntUsbDeviceCapabilities#getMaxChannels()}, use continuous scanning (unsupported at time of writing).
     * Downside is that all RF bandwith is spent scanning and no channels can be formed so you are limited to receiving only..
     *
     * @return List of assigned {@link AntChannel}s. Client should open the channel if he wishes to receive all messages, or unassign if he is not interested.
     * @throws AntException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public List<AntChannel> scanAvailableDevices() throws AntException, ExecutionException, InterruptedException {
        AntUsbDeviceCapabilities capabilities = this.antUsbDevice.getCapabilities();
        short maxChannels = capabilities.getMaxChannels();
        for (int i = 0; i < maxChannels; i++) {
            RequestMessage requestMessage = new RequestMessage((byte) i, ChannelStatusMessage.MSG_ID);
            ChannelStatusMessage responseMessage = (ChannelStatusMessage) antUsbDevice.sendBlocking(requestMessage);

            if (responseMessage.getChannelStatus() == ChannelStatusMessage.CHANNEL_STATUS.UnAssigned) {
                // scan channel for ant device
            } else {
                LOG.debug("Channel {} is in status {}. Not scanning.", responseMessage.getChannelNumber(), responseMessage.getChannelStatus());
            }
        }

        return new ArrayList<>();
    }

    public void openRxScanMode() throws AntException, ExecutionException, InterruptedException {
        HRMChannel channel = new HRMChannel();
        AssignChannelMessage assignChannelMessage = new AssignChannelMessage((byte) 0x00, channel.getChannelType().getValue(), channel.getNetwork().getNetworkNumber());
        if (((ChannelEventOrResponseMessage) antUsbDevice.sendBlocking(assignChannelMessage)).getResponseCode() != ChannelEventResponseCode.RESPONSE_NO_ERROR) {
            throw new RuntimeException("Could not assign channel");
        }

        OpenRxScanModeMessage rxScanModeMessage = new OpenRxScanModeMessage(false);
        antUsbDevice.send(rxScanModeMessage);
    }
}
