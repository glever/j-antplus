package be.glever.antplus.fec;

import java.util.Arrays;
import java.util.Optional;

public enum FecState {
    ASLEEP_OFF((byte) 1),
    READY((byte) 2),
    IN_USE((byte) 3),
    FINISHED_PAUSED((byte) 4);

    private byte value;

    FecState(byte i) {
        value = i;
    }

    public static Optional<FecState> valueOf(int value) {
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
            case ASLEEP_OFF:
                return "Asleep or Off";
            case READY:
                return "Ready";
            case IN_USE:
                return "In use";
            case FINISHED_PAUSED:
                return "Finished or Paused";
            default:
                throw new IllegalArgumentException();
        }
    }
}

