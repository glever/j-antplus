package be.glever.ant.constants;

public enum AntPlusDeviceType {
    HRM((byte) 0x78);

    private byte value;

    AntPlusDeviceType(byte i) {
        value = i;
    }

    public byte value() {
        return value;
    }
}
