package be.glever.ant.util;

import java.nio.ByteBuffer;

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

	public static String hexString(byte bite) {
		return hexString(new byte[]{bite});
	}

	public static int toInt(byte... byteArray){
		if(byteArray.length > 4){
			throw new IllegalArgumentException("ByteArray too large to convert to int. Length was: " + byteArray.length);
		}

		int val  = 0;
		for(byte b: byteArray){
			val = val << 8;
			val |= b;
		}
		return val;
	}
}
