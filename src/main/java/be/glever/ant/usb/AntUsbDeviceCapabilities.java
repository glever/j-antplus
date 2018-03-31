package be.glever.ant.usb;

import be.glever.ant.message.requestedresponse.CapabilitiesResponseMessage;

/**
 * Lists capabilities of an ant usb device.
 * @author glen
 *
 */
public class AntUsbDeviceCapabilities {

	private CapabilitiesResponseMessage crMsg;

	public AntUsbDeviceCapabilities(CapabilitiesResponseMessage crMsg) {
		this.crMsg = crMsg;
	}

	public byte getMessageId() {
		return crMsg.getMessageId();
	}

	public byte[] getMessageContent() {
		return crMsg.getMessageContent();
	}

	public short getMaxChannels() {
		return crMsg.getMaxChannels();
	}

	public short getMaxNetworks() {
		return crMsg.getMaxNetworks();
	}

	public boolean getReceiveChannels() {
		return crMsg.getReceiveChannels();
	}

	public boolean getTransmitChannels() {
		return crMsg.getTransmitChannels();
	}

	public boolean getReceiveMessages() {
		return crMsg.getReceiveMessages();
	}

	public boolean getTransmitMessages() {
		return crMsg.getTransmitMessages();
	}

	public boolean getAckdMessages() {
		return crMsg.getAckdMessages();
	}

	public boolean getBurstMessages() {
		return crMsg.getBurstMessages();
	}

	public boolean getNetworkEnabled() {
		return crMsg.getNetworkEnabled();
	}

	public boolean getSerialNumberEnabled() {
		return crMsg.getSerialNumberEnabled();
	}

	public boolean getPerChannelTxPowerEnabled() {
		return crMsg.getPerChannelTxPowerEnabled();
	}

	public boolean getLowPrioritySearchEnabled() {
		return crMsg.getLowPrioritySearchEnabled();
	}

	public boolean getScriptEnabled() {
		return crMsg.getScriptEnabled();
	}

	public boolean getSearchListEnabled() {
		return crMsg.getSearchListEnabled();
	}

	public boolean getLedEnabled() {
		return crMsg.getLedEnabled();
	}

	public boolean getExtMessageEnabled() {
		return crMsg.getExtMessageEnabled();
	}

	public boolean getScanModeEnabled() {
		return crMsg.getScanModeEnabled();
	}

	public boolean getProxSearchEnabled() {
		return crMsg.getProxSearchEnabled();
	}

	public boolean getExtAssignEnabled() {
		return crMsg.getExtAssignEnabled();
	}

	public boolean getFsAntFsEnabled() {
		return crMsg.getFsAntFsEnabled();
	}

	public boolean getFit1Enabled() {
		return crMsg.getFit1Enabled();
	}

	public short getMaxSensRcoreChannels() {
		return crMsg.getMaxSensRcoreChannels();
	}

	public boolean getAdvancedBurstEnabled() {
		return crMsg.getAdvancedBurstEnabled();
	}

	public boolean getEventBuffereingEnabled() {
		return crMsg.getEventBuffereingEnabled();
	}

	public boolean getEventFilteringEnabled() {
		return crMsg.getEventFilteringEnabled();
	}

	public boolean getHighDutySearchEnabled() {
		return crMsg.getHighDutySearchEnabled();
	}

	public boolean getSearchSharingEnabled() {
		return crMsg.getSearchSharingEnabled();
	}

	public boolean getSelectiveDataUpdatesEnabled() {
		return crMsg.getSelectiveDataUpdatesEnabled();
	}

	public boolean getEncryptedChannelEnabled() {
		return crMsg.getEncryptedChannelEnabled();
	}

	public boolean getRfActiveNotificationEnabled() {
		return crMsg.getRfActiveNotificationEnabled();
	}

	@Override
	public String toString() {
		return "AntUsbDeviceCapabilities{" +
				"crMsg=" + crMsg +
				'}';
	}
}
