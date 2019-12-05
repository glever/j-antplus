package be.glever.antplus.fec;

import java.util.Arrays;
import java.util.Optional;

public enum HeartRateDataSource {
    UNKNOWN((byte) 0),
    ANT_PLUS((byte) 1),
    EM((byte) 3),
    HAND_CONTACTS((byte) 3);

    private byte value;

    HeartRateDataSource(byte i) {
        value = i;
    }

    public static Optional<HeartRateDataSource> valueOf(int value) {
        return Arrays.stream(values())
                .filter(legNo -> legNo.value == value)
                .findFirst();
    }

    public byte value() {
        return value;
    }

    @Override
    public String toString() {
        switch (this) {
            case UNKNOWN:
                return "Unknown";
            case ANT_PLUS:
                return "ANT+";
            case EM:
                return "EM (5 kHz)";
            case HAND_CONTACTS:
                return "Hand contact sensors";
            default:
                throw new IllegalArgumentException();
        }
    }
}
