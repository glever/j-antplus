package be.glever.ant.channel;

import be.glever.ant.constants.AntNetworkKeys;

public class AntChannelNetwork {
    private byte networkNumber;
    private byte[] networkKey;

    public AntChannelNetwork(byte networkNumber, AntNetworkKeys key) {
        this.networkNumber = networkNumber;
        this.networkKey = key.getBytes();
    }

    public byte getNetworkNumber() {
        return networkNumber;
    }

    public void setNetworkNumber(byte networkNumber) {
        this.networkNumber = networkNumber;
    }

    public byte[] getNetworkKey() {
        return networkKey;
    }

    public void setNetworkKey(byte[] networkKey) {
        this.networkKey = networkKey;
    }

}
