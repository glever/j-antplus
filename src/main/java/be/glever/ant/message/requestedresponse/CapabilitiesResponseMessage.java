package be.glever.ant.message.requestedresponse;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;

import java.util.Arrays;

import static be.glever.ant.util.ByteUtils.hasBitSet;

public class CapabilitiesResponseMessage extends AbstractAntMessage {

    public static final byte MSG_ID = 0x54;
    private byte[] messageBytes;

    public CapabilitiesResponseMessage() {
    }

    @Override
    public byte getMessageId() {
        return MSG_ID;
    }

    @Override
    public byte[] getMessageContent() {
        return messageBytes;
    }

    @Override
    public void setMessageBytes(byte[] messageContentBytes) throws AntException {
        if (messageContentBytes.length < 6 || messageContentBytes.length > 8) {
            throw new AntException(
                    String.format("Incorrect message length. Given: %s, expected: 6-8", messageContentBytes.length));
        }
        this.messageBytes = messageContentBytes;
    }

    public byte getMaxChannels() {
        return messageBytes[0];
    }

    public short getMaxNetworks() {
        return messageBytes[1];
    }

    private short getStandardOptionsByte() {
        return messageBytes[2];
    }

    public boolean getReceiveChannels() {
        return !hasBitSet(getStandardOptionsByte(), 0);
    }

    public boolean getTransmitChannels() {
        return !hasBitSet(getStandardOptionsByte(), 1);
    }

    public boolean getReceiveMessages() {
        return !hasBitSet(getStandardOptionsByte(), 2);
    }

    public boolean getTransmitMessages() {
        return !hasBitSet(getStandardOptionsByte(), 3);
    }

    public boolean getAckdMessages() {
        return !hasBitSet(getStandardOptionsByte(), 4);
    }

    public boolean getBurstMessages() {
        return !hasBitSet(getStandardOptionsByte(), 5);
    }

    private byte getAdvancedOptionsByte() {
        return messageBytes[3];
    }

    public boolean getNetworkEnabled() {
        return hasBitSet(getAdvancedOptionsByte(), 1);
    }

    public boolean getSerialNumberEnabled() {
        return hasBitSet(getAdvancedOptionsByte(), 3);
    }

    public boolean getPerChannelTxPowerEnabled() {
        return hasBitSet(getAdvancedOptionsByte(), 4);
    }

    public boolean getLowPrioritySearchEnabled() {
        return hasBitSet(getAdvancedOptionsByte(), 5);
    }

    public boolean getScriptEnabled() {
        return hasBitSet(getAdvancedOptionsByte(), 6);
    }

    public boolean getSearchListEnabled() {
        return hasBitSet(getAdvancedOptionsByte(), 7);
    }

    private byte getAdvancedOptions2Byte() {
        return messageBytes[4];
    }

    public boolean getLedEnabled() {
        return hasBitSet(getAdvancedOptions2Byte(), 0);
    }

    public boolean getExtMessageEnabled() {
        return hasBitSet(getAdvancedOptions2Byte(), 1);
    }

    public boolean getScanModeEnabled() {
        return hasBitSet(getAdvancedOptions2Byte(), 2);
    }

    public boolean getProxSearchEnabled() {
        return hasBitSet(getAdvancedOptions2Byte(), 4);
    }

    public boolean getExtAssignEnabled() {
        return hasBitSet(getAdvancedOptions2Byte(), 5);
    }

    public boolean getFsAntFsEnabled() {
        return hasBitSet(getAdvancedOptions2Byte(), 6);
    }

    public boolean getFit1Enabled() {
        return hasBitSet(getAdvancedOptions2Byte(), 7);
    }

    public short getMaxSensRcoreChannels() {
        return messageBytes[5];
    }

    private byte getAdvancedOptions3Byte() {
        return messageBytes.length > 6 ? messageBytes[6] : 0;
    }

    public boolean getAdvancedBurstEnabled() {
        return hasBitSet(getAdvancedOptions3Byte(), 0);
    }

    public boolean getEventBuffereingEnabled() {
        return hasBitSet(getAdvancedOptions3Byte(), 1);
    }

    public boolean getEventFilteringEnabled() {
        return hasBitSet(getAdvancedOptions3Byte(), 2);
    }

    public boolean getHighDutySearchEnabled() {
        return hasBitSet(getAdvancedOptions3Byte(), 3);
    }

    public boolean getSearchSharingEnabled() {
        return hasBitSet(getAdvancedOptions3Byte(), 4);
    }

    public boolean getSelectiveDataUpdatesEnabled() {
        return hasBitSet(getAdvancedOptions3Byte(), 6);
    }

    public boolean getEncryptedChannelEnabled() {
        return hasBitSet(getAdvancedOptions3Byte(), 7);
    }

    private byte getAdvancedOptions4Byte() {
        return messageBytes.length > 7 ? messageBytes[7] : 0;
    }

    public boolean getRfActiveNotificationEnabled() {
        return hasBitSet(getAdvancedOptions4Byte(), 0);
    }

    @Override
    public String toString() {
        return "CapabilitiesResponseMessage [getMessageId()=" + getMessageId() + ", getMessageContent()="
                + Arrays.toString(getMessageContent()) + ", getMaxChannels()=" + getMaxChannels()
                + ", getMaxNetworks()=" + getMaxNetworks() + ", getReceiveChannels()=" + getReceiveChannels()
                + ", getTransmitChannels()=" + getTransmitChannels() + ", getReceiveMessages()=" + getReceiveMessages()
                + ", getTransmitMessages()=" + getTransmitMessages() + ", getAckdMessages()=" + getAckdMessages()
                + ", getBurstMessages()=" + getBurstMessages() + ", getNetworkEnabled()=" + getNetworkEnabled()
                + ", getSerialNumberEnabled()=" + getSerialNumberEnabled() + ", getPerChannelTxPowerEnabled()="
                + getPerChannelTxPowerEnabled() + ", getLowPrioritySearchEnabled()=" + getLowPrioritySearchEnabled()
                + ", getScriptEnabled()=" + getScriptEnabled() + ", getSearchListEnabled()=" + getSearchListEnabled()
                + ", getLedEnabled()=" + getLedEnabled() + ", getExtMessageEnabled()=" + getExtMessageEnabled()
                + ", getScanModeEnabled()=" + getScanModeEnabled() + ", getProxSearchEnabled()="
                + getProxSearchEnabled() + ", getExtAssignEnabled()=" + getExtAssignEnabled() + ", getFsAntFsEnabled()="
                + getFsAntFsEnabled() + ", getFit1Enabled()=" + getFit1Enabled() + ", getMaxSensRcoreChannels()="
                + getMaxSensRcoreChannels() + ", getAdvancedBurstEnabled()=" + getAdvancedBurstEnabled()
                + ", getEventBuffereingEnabled()=" + getEventBuffereingEnabled() + ", getEventFilteringEnabled()="
                + getEventFilteringEnabled() + ", getHighDutySearchEnabled()=" + getHighDutySearchEnabled()
                + ", getSearchSharingEnabled()=" + getSearchSharingEnabled() + ", getSelectiveDataUpdatesEnabled()="
                + getSelectiveDataUpdatesEnabled() + ", getEncryptedChannelEnabled()=" + getEncryptedChannelEnabled()
                + ", getRfActiveNotificationEnabled()=" + getRfActiveNotificationEnabled() + "]";
    }

}
