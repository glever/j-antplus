package be.glever.ant.message.configuration;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;
import be.glever.ant.message.AntBlockingMessage;
import be.glever.ant.message.channel.ChannelEventOrResponseMessage;

public class UnassignChannelMessage extends AbstractAntMessage implements AntBlockingMessage {

    private byte channelNumber;

    public UnassignChannelMessage(byte channelNumber) {
        this.channelNumber = channelNumber;
    }

    public UnassignChannelMessage() {
    }

    @Override
    public byte getMessageId() {
        return 0x41;
    }

    @Override
    public byte[] getMessageContent() {
        return new byte[]{channelNumber};
    }

    @Override
    public void setMessageBytes(byte[] messageContentBytes) throws AntException {
        // TODO Auto-generated method stub

    }

    @Override
    public byte getResponseMessageId() {
        return ChannelEventOrResponseMessage.MSG_ID;
    }
}
