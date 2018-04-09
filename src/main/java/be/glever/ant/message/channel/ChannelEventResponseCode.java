package be.glever.ant.message.channel;

public enum ChannelEventResponseCode {
	RESPONSE_NO_ERROR((byte) 0x00),
	EVENT_RX_SEARCH_TIMEOUT((byte) 0x01),
	EVENT_RX_FAIL((byte) 0x01),
	EVENT_TX((byte) 0x03),
	EVENT_TRANSFER_RX_FAILED((byte) 0x04),
	EVENT_TRANSFER_TX_COMPLETED((byte) 0x05),
	EVENT_TRANSFER_TX_FAILED((byte) 0x06),
	EVENT_CHANNEL_CLOSED((byte) 0x07),
	EVENT_RX_FAIL_GO_TO_SEARCH((byte) 0x08),
	EVENT_CHANNEL_COLLISION((byte) 0x09),
	EVENT_TRANSFER_TX_START((byte) 0x0a),
	EVENT_TRANSFER_NEXT_DATA_BLOCK((byte) 0x11),
	CHANNEL_IN_WRONG_STATE((byte) 0x15),
	CHANNEL_NOT_OPENED((byte) 0x16),
	CHANNEL_ID_NOT_SET((byte) 0x18),
	CLOSE_ALL_CHANNELS((byte) 0x19),
	TRANSFER_IN_PROGRESS((byte) 0x1f),
	TRANSFER_SEQUENCE_NUMBER_ERROR((byte) 0x20),
	TRANSFER_IN_ERROR((byte) 0x21),
	MESSAGE_SIZE_EXCEEDS_LIMIT((byte) 0x27),
	INVALID_MESSAGE((byte) 0x28),
	INVALID_NETWORK_NUMBER((byte) 0x29),
	INVALID_LIST_ID((byte) 0x30),
	INVALID_SCAN_TX_CHANNEL((byte) 031),
	INVALID_PARAMETER_PROVIDED((byte) 0x33),
	EVENT_SERIAL_QUEUE_OVERFLOW((byte) 0x34),
	EVENT_QUEUE_OVERFLOW((byte) 0x35),
	ENCRYPT_NEGOTIATION_SUCCESS((byte) 0x38),
	ENCRYPT_NEGOTIATION_FAIL((byte) 0x39),
	NVM_FULL_ERROR((byte) 0x40),
	NVM_WRITE_ERROR((byte) 0x41),
	USB_STRING_WRITE_FAIL((byte) 0x70),
	MESG_SERIAL_ERROR_ID((byte) 0xAE),
	UNDOCUMENTED_ANT_ERROR_CODE((byte) 0xFF); // bug here when ant decides to use 0xFF


	private byte code;

	ChannelEventResponseCode(byte value) {
		this.code = value;
	}

	public static ChannelEventResponseCode fromValue(byte value) {
		for (ChannelEventResponseCode channelEvenResponseCode : values()) {
			if (value == channelEvenResponseCode.code) {
				return channelEvenResponseCode;
			}
		}
		return UNDOCUMENTED_ANT_ERROR_CODE;
	}
}
