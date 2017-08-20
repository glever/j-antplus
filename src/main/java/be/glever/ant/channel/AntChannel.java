package be.glever.ant.channel;

public class AntChannel {
	private AntChannelType channelType;
	private AntChannelRfFrequency rfFrequency;
	private AntChannelId channelId;
	private AntChannelPeriod channelPeriod;
	private AntChannelNetwork network;
	
	
	public AntChannel() {
	}


	public AntChannelType getChannelType() {
		return channelType;
	}


	public void setChannelType(AntChannelType channelType) {
		this.channelType = channelType;
	}


	public AntChannelRfFrequency getRfFrequency() {
		return rfFrequency;
	}


	public void setRfFrequency(AntChannelRfFrequency rfFrequency) {
		this.rfFrequency = rfFrequency;
	}


	public AntChannelId getChannelId() {
		return channelId;
	}


	public void setChannelId(AntChannelId channelId) {
		this.channelId = channelId;
	}


	public AntChannelPeriod getChannelPeriod() {
		return channelPeriod;
	}


	public void setChannelPeriod(AntChannelPeriod channelPeriod) {
		this.channelPeriod = channelPeriod;
	}


	public AntChannelNetwork getNetwork() {
		return network;
	}


	public void setNetwork(AntChannelNetwork network) {
		this.network = network;
	}

	
}
