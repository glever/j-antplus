package be.glever.ant.message;

import be.glever.ant.AntException;
import be.glever.ant.message.channeleventresponse.ChannelEventResponse;
import be.glever.ant.message.configuration.*;
import be.glever.ant.message.control.*;
import be.glever.ant.message.notification.SerialErrorMessage;
import be.glever.ant.message.notification.StartupNotificationMessage;
import be.glever.ant.message.requestedresponse.*;
import be.glever.ant.util.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Container and factory for all known antmessages.
 */
public class AntMessageRegistry {
	private static final Logger LOG = LoggerFactory.getLogger(AntMessageRegistry.class);
	private static Map<Byte, Class<? extends AbstractAntMessage>> registry = new HashMap<>();

	static {
		// configuration
		add(AssignChannelMessage.class);
		add(ChannelPeriodMessage.class);
		add(ChannelRfFrequencyMessage.class);
		add(SearchTimeoutMessage.class);
		add(UnassignChannelMessage.class);

		// control
		add(CloseChannelMessage.class);
		add(OpenChannelMessage.class);
		add(OpenRxScanModeMessage.class);
		add(RequestMessage.class);
		add(ResetSystemMessage.class);

		// data

		// notification
		add(StartupNotificationMessage.class);
		add(SerialErrorMessage.class);

		// requested response
		add(AntVersionMessage.class);
		add(ChannelIdMessage.class);
		add(ChannelStatusMessage.class);
		add(DeviceSerialNumberMessage.class);
		add(CapabilitiesResponseMessage.class);

		// Channel events
		add(ChannelEventResponse.class);
	}

	/**
	 * Parses the given bytes into the correct {@link AbstractAntMessage}
	 * implementation. Precondition: the bytes represent 1 valid Ant Message (from
	 * SYNC to checksum).
	 *
	 * @param bytes
	 * @return
	 */
	public static AntMessage from(byte[] bytes) throws AntException {
		byte msgLength = (byte) (bytes[1] + 4);// sync, msglen, msgid and checksum excluded
		byte msgId = bytes[2];
		bytes = Arrays.copyOf(bytes, msgLength);

		Class<? extends AbstractAntMessage> messageClass = registry.get(msgId);
		AntMessage messageInstance = null;
		if (messageClass == null) {
			LOG.error("Could not convert {} to an AntMessage. Bytes received were {}", ByteUtils.hexString(msgId),
					ByteUtils.hexString(bytes));
			messageInstance = new UnknownMessage(bytes);
		} else {
			messageInstance = instantiate(messageClass);
			messageInstance.parse(bytes);
		}

		LOG.debug("Converted {} to {}", ByteUtils.hexString(bytes), messageInstance.getClass());
		return messageInstance;
	}

	private static void add(Class<? extends AbstractAntMessage> antMessageImplClass) {
		byte messageId = instantiate(antMessageImplClass).getMessageId();
		if (registry.containsKey(messageId)) {
			Class<? extends AbstractAntMessage> existingClass = registry.get(messageId);
			throw new IllegalStateException(
					String.format("Could not add class %s to registry due to duplicate id %s. Conflicting class: %s",
							antMessageImplClass, messageId, existingClass));
		}
		registry.put(messageId, antMessageImplClass);
	}

	private static AntMessage instantiate(Class<? extends AbstractAntMessage> msgImplClass) {
		try {
			return msgImplClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalStateException("Initialization Error. Could not call default constructor on class ["
					+ msgImplClass.getName() + "]");
		}
	}

	public static class UnknownMessage extends AbstractAntMessage implements AntMessage {

		private byte[] bytes;

		private UnknownMessage(byte[] fullByteArray) {
			this.bytes = fullByteArray;
		}

		@Override
		public byte getMessageId() {
			return bytes[2];
		}

		@Override
		public byte[] getMessageContent() {
			return Arrays.copyOfRange(bytes, 4, 4 + bytes[1]);
		}

		@Override
		public void setMessageBytes(byte[] messageContentBytes) throws AntException {
			throw new UnsupportedOperationException("Unsupported, only constructor should be used");
		}

		@Override
		public String toString() {
			return "UnknownMessage [msgId=" + ByteUtils.hexString(getMessageId()) + ", bytes=" + ByteUtils.hexString(bytes) + "]";
		}

	}

}
