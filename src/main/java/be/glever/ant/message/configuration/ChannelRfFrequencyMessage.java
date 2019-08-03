package be.glever.ant.message.configuration;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;
import be.glever.ant.message.AntBlockingMessage;
import be.glever.ant.message.channel.ChannelEventOrResponseMessage;
import be.glever.ant.util.ByteArrayBuilder;

public class ChannelRfFrequencyMessage extends AbstractAntMessage implements AntBlockingMessage {

    private byte channelNumber;
    private byte channelRfFrequency;

    public ChannelRfFrequencyMessage(byte channelNumber, byte channelRfFrequency) {
        this.channelNumber = channelNumber;
        this.channelRfFrequency = channelRfFrequency;
    }

    public ChannelRfFrequencyMessage() {
    }

    @Override
    public byte getMessageId() {
        return 0x45;
    }

    @Override
    public byte[] getMessageContent() {
        return new ByteArrayBuilder()
                .write(channelNumber)
                .write(channelRfFrequency)
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
