package be.glever.ant.util;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class ByteUtilTest {

	@Test
	public void validateHasMask() {
		Assert.assertTrue(ByteUtils.hasMask(0x1, 0b1));
		Assert.assertFalse(ByteUtils.hasMask(0x1, 0b10));
		Assert.assertTrue(ByteUtils.hasMask(0b11, 0b1));
		Assert.assertTrue(ByteUtils.hasMask(0b11, 0b10));
		Assert.assertTrue(ByteUtils.hasMask(0b11, 0b11));
		Assert.assertFalse(ByteUtils.hasMask(0b11, 0b111));
	}

	@Test
	public void validateHasBitSet() {
		Assert.assertTrue(ByteUtils.hasBitSet(0b1, 0));
		Assert.assertFalse(ByteUtils.hasBitSet(0b1, 1));
		Assert.assertTrue(ByteUtils.hasBitSet(0b11, 1));
	}

	@Test
	public void validateUShort() {
		assertArrayEquals(new byte[]{0b1, 0b0}, ByteUtils.toUShort(1));
		assertArrayEquals(new byte[]{0b11, 0b0}, ByteUtils.toUShort(3));
		assertArrayEquals(new byte[]{(byte) 0xFF, 0b0}, ByteUtils.toUShort(255));
		assertArrayEquals(new byte[]{0b0, 0b10}, ByteUtils.toUShort(512));
		assertArrayEquals(new byte[]{(byte) 0xFF, (byte) 0xFF}, ByteUtils.toUShort(65535));
	}
}
