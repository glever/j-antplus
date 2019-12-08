package be.glever.antplus.speedcadence;

import be.glever.ant.AntException;
import be.glever.ant.channel.AntChannel;
import be.glever.ant.constants.AntPlusDeviceType;
import be.glever.ant.message.channel.ChannelEventOrResponseMessage;
import be.glever.ant.message.channel.ChannelEventResponseCode;
import be.glever.ant.message.configuration.*;
import be.glever.ant.message.control.OpenChannelMessage;
import be.glever.ant.usb.AntUsbDevice;

import java.util.concurrent.ExecutionException;

public class SpeedAndCadenceSlave {

    private static final byte ANTPLUS_SPEED_CADENCE_FREQUENCY = (byte) 57; // 2457Mhz
    private AntUsbDevice antUsbDevice;

    public SpeedAndCadenceSlave(AntUsbDevice antUsbDevice) {
        this.antUsbDevice = antUsbDevice;
    }

    public AntChannel[] listDevices() throws AntException, ExecutionException, InterruptedException {
        SpeedChannel channel = new SpeedChannel(antUsbDevice);

        byte channelNumber = 0x00;

        // ASSIGN CHANNEL
        NetworkKeyMessage networkKeyMessage = new NetworkKeyMessage(channelNumber, channel.getNetwork().getNetworkKey());
        ChannelEventOrResponseMessage setNetworkKeyResponse = (ChannelEventOrResponseMessage) antUsbDevice.sendBlocking(networkKeyMessage);
        if (setNetworkKeyResponse.getResponseCode() != ChannelEventResponseCode.RESPONSE_NO_ERROR) {
            throw new RuntimeException("Could not set network key");
        }

        AssignChannelMessage assignChannelMessage = new AssignChannelMessage(channelNumber, (byte) 0x40, (byte) 0x00);
        ChannelEventOrResponseMessage response = (ChannelEventOrResponseMessage) antUsbDevice.sendBlocking(assignChannelMessage);
        if (response.getResponseCode() != ChannelEventResponseCode.RESPONSE_NO_ERROR) {
            throw new RuntimeException("Could not assign channel");
        }

        ChannelIdMessage channelIdMessage = new ChannelIdMessage(channelNumber, new byte[]{0x00, 0x00}, AntPlusDeviceType.HRM.value(), (byte) 0x00);
        ChannelEventOrResponseMessage channelIdResponse = (ChannelEventOrResponseMessage) antUsbDevice.sendBlocking(channelIdMessage);
        if (channelIdResponse.getResponseCode() != ChannelEventResponseCode.RESPONSE_NO_ERROR) {
            throw new RuntimeException("Could set channelId");
        }
        ChannelPeriodMessage channelPeriodMessage = new ChannelPeriodMessage(channelNumber, SpeedAndCadenceChannel.CHANNEL_PERIOD);
        ChannelEventOrResponseMessage channelPeriodResponseMessage = (ChannelEventOrResponseMessage) antUsbDevice.sendBlocking(channelPeriodMessage);
        if (channelPeriodResponseMessage.getResponseCode() != ChannelEventResponseCode.RESPONSE_NO_ERROR) {
            throw new RuntimeException("Could set channel period");
        }

        ChannelRfFrequencyMessage channelRfFrequencyMessage = new ChannelRfFrequencyMessage(channelNumber, (byte) 0x39);
        ChannelEventOrResponseMessage channelRfFrequencyResponse = (ChannelEventOrResponseMessage) antUsbDevice.sendBlocking(channelRfFrequencyMessage);
        if (channelRfFrequencyResponse.getResponseCode() != ChannelEventResponseCode.RESPONSE_NO_ERROR) {
            throw new RuntimeException("Could not set rf frequency");
        }

        response = (ChannelEventOrResponseMessage) antUsbDevice.sendBlocking(new OpenChannelMessage(response.getChannelNumber()));
        if (response.getResponseCode() != ChannelEventResponseCode.RESPONSE_NO_ERROR) {
            throw new RuntimeException("Could not open channel");
        }

        // OPEN CHANNEL
        return null;
    }

}
