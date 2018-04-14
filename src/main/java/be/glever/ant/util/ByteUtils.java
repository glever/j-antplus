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
		StringBuilder sb = new StringBuilder(3 * bytes.length);

		boolean first = true;
		for (byte bite : bytes) {
			if (first) {
				first = false;
			} else {
				sb.append('-');
			}
			sb.append(String.format("%02X", (0xFF & bite)));
		}
		return sb.toString();
	}

	public static boolean hasMask(int value, int mask) {
		return (value & mask) == mask;
	}

	public static boolean hasBitSet(int value, int bitPos) {
		return hasMask(value, 1 << bitPos);
	}

	public static String hexString(byte bite) {
		return hexString(new byte[]{bite});
	}

	public static int toInt(byte... byteArray) {
		if (byteArray.length > 4) {
			throw new IllegalArgumentException("ByteArray too large to convert to int. Length was: " + byteArray.length);
		}

		int val = 0;
		for (byte b : byteArray) {
			val = val << 8;
			val |= b;
		}
		return val;
	}

	/**
	 * returns 2 byte array representing the unsigned value
	 * @param val
	 */
	public static byte[] toUShort(int val) {
		byte[] ret = new byte[2];
		ret[0] = (byte) val;
		ret[1] = (byte) (val >> 8);
		return ret;
	}
}
