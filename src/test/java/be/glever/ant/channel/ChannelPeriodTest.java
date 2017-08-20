package be.glever.ant.channel;

import org.junit.Assert;
import org.junit.Test;

public class ChannelPeriodTest {

	private static final int DEFAULT_PERIOD = 8192;

	@Test
	public void defaultChannelPeriodShouldBe8192() {
		byte[] value = new AntChannelPeriod().getValue();
		Assert.assertEquals(DEFAULT_PERIOD, value[1] << 8 | value[0]);
	}
	
	@Test 
	public void defaultChannelPeriodShouldBe4Hz() {
		byte[] value = new AntChannelPeriod(4).getValue();
		Assert.assertEquals(DEFAULT_PERIOD, value[1] << 8 | value[0]);
	}
}
