package be.glever.ant.constants;

import java.util.Arrays;

public enum AntNetworkKeys {
    ANT_PLUS_NETWORK_KEY(new byte[]{(byte) 0xb9, (byte) 0xa5, 0x21, (byte) 0xfb, (byte) 0xbd, 0x72, (byte) 0xc3, 0x45}),
    ANT_FS_NETWORK_KEY(new byte[]{(byte) 0xA8, (byte) 0xA4, 0x23, (byte) 0xB9, (byte) 0xF5, 0x5E, 0x63, (byte) 0xC1}),
    ANT_PUBLIC_NETWORK_KEY(new byte[]{(byte) 0xE8, (byte) 0xE4, 0x21, 0x3B, 0x55, 0x7A, 0x67, (byte) 0xC1});

    private final byte[] bytes;

    AntNetworkKeys(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return Arrays.copyOf(bytes, bytes.length);
    }
}
