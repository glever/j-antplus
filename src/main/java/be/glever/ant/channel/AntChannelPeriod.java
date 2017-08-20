package be.glever.ant.channel;

public class AntChannelPeriod {
	public static final double DEFAULT_FREQUENCY = 4;
	private byte[] value; // TODO remember this is little endian
	
	
	/**
	 * Creates a {@link ChannelPeriod} based upon the given Hertz following the formula
	 * period = 32768 / Hz
	 * @param frequencyInHz
	 */
	public AntChannelPeriod(double frequencyInHz) {
		int period = (int) (32768 / frequencyInHz);
		value = new byte[2];
		value[0] = (byte) period;
		value[1] = (byte) (period >> 8);
	}
	
	public AntChannelPeriod() {
		this(DEFAULT_FREQUENCY);
	}
	public byte[] getValue() {
		return value;
	}
}
