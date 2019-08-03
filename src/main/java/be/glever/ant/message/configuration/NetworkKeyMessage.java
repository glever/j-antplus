package be.glever.ant.message.configuration;

import be.glever.ant.AntException;
import be.glever.ant.message.AbstractAntMessage;
import be.glever.ant.message.AntBlockingMessage;
import be.glever.ant.message.channel.ChannelEventOrResponseMessage;
import be.glever.ant.util.ByteArrayBuilder;

public class NetworkKeyMessage extends AbstractAntMessage implements AntBlockingMessage {
    private static final byte MSG_ID = 0x46;
    private byte[] messageBytes;

    public NetworkKeyMessage() {
    }

    public NetworkKeyMessage(byte channelNumber, byte[] networkKey) {
        this.messageBytes = new ByteArrayBuilder().write(channelNumber).write(networkKey).toByteArray();
    }


    @Override
    public byte[] getMessageContent() {
        return this.messageBytes;
    }

    @Override
    public void setMessageBytes(byte[] messageContentBytes) throws AntException {
        this.messageBytes = messageContentBytes;
    }

    @Override
    public byte getMessageId() {
        return MSG_ID;
    }

    @Override
    public byte getResponseMessageId() {
        return ChannelEventOrResponseMessage.MSG_ID;
    }
}
