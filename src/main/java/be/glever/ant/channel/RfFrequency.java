package be.glever.ant.channel;

/**
 * Defines the radio freqency which lies between 2400MHz and 2524MHz. The
 * formula used to calculate the value to MHz is: value = desired_freq - 2400.
 * 
 * Note that most of ANT+ devices operate on 2450MHz and 2457MHz and should be
 * avoided by non-ANT+ devices.
 * 
 * @author glen
 *
 */
public class RfFrequency {

	private byte value;

	/**
	 * Sets the default Frequency to 2466MHz as per the ANT spec.
	 */
	public RfFrequency() {
		this((byte) 66);
	}

	public RfFrequency(byte value) {
		if (value < 0 || value > 124) {
			throw new IllegalArgumentException(
					"Invalid frequency value: [" + value + "]. Must lie within 0-124 (inclusive)");
		}

		this.value = value;
	}

	public byte getValue() {
		return value;
	}

}
