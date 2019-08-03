package be.glever.ant.channel;

import be.glever.ant.constants.AntChannelType;
import be.glever.ant.message.AntMessage;

import java.util.ArrayList;
import java.util.List;

public class AntChannel {
    private AntChannelId channelId;
    private AntChannelType channelType;
    private byte rfFrequency;
    private byte[] channelPeriod;
    private AntChannelNetwork network;
    private List<AntChannelListener> antChannelListeners = new ArrayList<>();


    public AntChannel() {
    }


    public AntChannelType getChannelType() {
        return channelType;
    }


    public void setChannelType(AntChannelType channelType) {
        this.channelType = channelType;
    }


    public byte getRfFrequency() {
        return rfFrequency;
    }


    public void setRfFrequency(byte rfFrequency) {
        if (rfFrequency < 0 || rfFrequency > 124) {
            throw new IllegalArgumentException(
                    "Invalid frequency value: [" + rfFrequency + "]. Must lie within 0-124 (inclusive)");
        }
        this.rfFrequency = rfFrequency;
    }


    public AntChannelId getChannelId() {
        return channelId;
    }


    public void setChannelId(AntChannelId channelId) {
        this.channelId = channelId;
    }


    public byte[] getChannelPeriod() {
        return channelPeriod;
    }


    public void setChannelPeriod(byte[] channelPeriod) {
        this.channelPeriod = channelPeriod;
    }


    public AntChannelNetwork getNetwork() {
        return network;
    }


    public void setNetwork(AntChannelNetwork network) {
        this.network = network;
    }

    public void handle(AntMessage msg) {
        antChannelListeners.stream().forEach(listener -> listener.handle(msg));
    }

    public void addListener(AntChannelListener listener) {
        antChannelListeners.add(listener);
    }

    public void removeListener(AntChannelListener listener) {
        antChannelListeners.remove(listener);
    }
}
