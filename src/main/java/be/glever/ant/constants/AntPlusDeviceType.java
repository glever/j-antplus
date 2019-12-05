package be.glever.ant.constants;

public enum AntPlusDeviceType {
    Power((byte) 0x0B),
    HRM((byte) 0x78),
    SpeedCadence((byte) 0x79),
    Cadence((byte) 0x7A),
    Speed((byte) 0x7B);

    private byte value;

    AntPlusDeviceType(byte i) {
        value = i;
    }

    public byte value() {
        return value;
    }
}
