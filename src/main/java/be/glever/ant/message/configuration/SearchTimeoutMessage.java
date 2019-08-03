package be.glever.ant.message.configuration;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;
import be.glever.ant.message.AntBlockingMessage;
import be.glever.ant.message.channel.ChannelEventOrResponseMessage;
import be.glever.ant.util.ByteArrayBuilder;

public class SearchTimeoutMessage extends AbstractAntMessage implements AntBlockingMessage {

    private byte channelNumber;
    private byte searchTimeout;

    public SearchTimeoutMessage(byte channelNumber, byte searchTimeout) {
        this.channelNumber = channelNumber;
        this.searchTimeout = searchTimeout;
    }

    public SearchTimeoutMessage() {
    }

    @Override
    public byte getMessageId() {
        return 0x44;
    }

    @Override
    public byte[] getMessageContent() {
        return new ByteArrayBuilder()
                .write(channelNumber)
                .write(searchTimeout)
                .toByteArray();
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
