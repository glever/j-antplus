package be.glever.antplus.fec;

import java.util.Arrays;
import java.util.Optional;

public enum EquipmentType {
    Treadmill((byte) 19),
    Elliptical((byte) 20),
    Rower((byte) 22),
    Climber((byte) 23),
    NordicSkier((byte) 24),
    StationaryBike((byte) 25);

    private byte value;

    EquipmentType(byte i) {
        value = i;
    }

    public static Optional<EquipmentType> valueOf(int value) {
        return Arrays.stream(values())
                .filter(legNo -> legNo.value == value)
                .findFirst();
    }

    public byte value() {
        return value;
    }
}
