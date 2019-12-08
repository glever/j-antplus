package be.glever.antplus.fec;

import java.util.Arrays;
import java.util.Optional;

/**
 * Where the FE-C equipment obtains the heart rate measurement from
 */
public enum HeartRateDataSource {
    // Unknown source
    UNKNOWN((byte) 0),
    // ANT+ sensor connected to the FE-C equipment
    ANT_PLUS((byte) 1),
    // 5 kHz wireless HRM worn by the athlete
    EM((byte) 3),
    // Hand contact sensor on the FE-C equipment
    HAND_CONTACTS((byte) 3);

    private byte value;

    HeartRateDataSource(byte value) {
        this.value = value;
    }

    public static Optional<HeartRateDataSource> valueOf(int value) {
        return Arrays.stream(values())
                .filter(legNo -> legNo.value == value)
                .findFirst();
    }

    public byte value() {
        return value;
    }
}
