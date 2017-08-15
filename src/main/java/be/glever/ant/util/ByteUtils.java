package be.glever.ant.util;

public class ByteUtils {

	public static String binaryString(byte[] bytes) {
		StringBuilder sb = new StringBuilder(8 * bytes.length);
		for (byte bite : bytes) {
			sb.append(Integer.toBinaryString(bite & 0xFF));
		}
		return sb.toString();
	}

	public static String hexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder(4 * bytes.length);
		for (byte bite : bytes) {
			sb.append('[');
			sb.append(Integer.toHexString(bite & 0xFF));
			sb.append(']');
		}
		return sb.toString();
	}
	
	public static boolean hasMask(int value, int mask) {
		return (value & mask) == mask;
	}
	
	public static boolean hasBitSet(int value, int bitPos) {
		return hasMask(value, 1 << bitPos);
	}
}
