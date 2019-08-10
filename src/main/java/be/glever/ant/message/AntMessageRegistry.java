package be.glever.ant.message;

import be.glever.ant.AntException;
import be.glever.ant.message.channel.ChannelEventOrResponseMessage;
import be.glever.ant.message.configuration.*;
import be.glever.ant.message.control.*;
import be.glever.ant.message.data.BroadcastDataMessage;
import be.glever.ant.message.notification.SerialErrorMessage;
import be.glever.ant.message.notification.StartupNotificationMessage;
import be.glever.ant.message.requestedresponse.AntVersionMessage;
import be.glever.ant.message.requestedresponse.CapabilitiesResponseMessage;
import be.glever.ant.message.requestedresponse.ChannelStatusMessage;
import be.glever.ant.message.requestedresponse.SerialNumberMessage;
import be.glever.ant.util.ByteUtils;
import be.glever.util.logging.Log;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

/**
 * Container and factory for all known antmessages.
 */
public class AntMessageRegistry {
    private static final Log LOG = Log.getLogger(AntMessageRegistry.class);
    private static Map<Byte, Class<? extends AbstractAntMessage>> registry = new HashMap<>();

    static {
        // configuration
        add(AssignChannelMessage.class);
        add(ChannelPeriodMessage.class);
        add(ChannelRfFrequencyMessage.class);
        add(SearchTimeoutMessage.class);
        add(UnassignChannelMessage.class);
        add(NetworkKeyMessage.class);
        add(ChannelIdMessage.class);

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
        add(BroadcastDataMessage.class);

        // requested response
        add(AntVersionMessage.class);
        add(ChannelStatusMessage.class);
        add(SerialNumberMessage.class);
        add(CapabilitiesResponseMessage.class);

        // Channel events
        add(ChannelEventOrResponseMessage.class);
    }

    /**
     * Parses the given bytes into the correct {@link AbstractAntMessage}
     * implementation. Precondition: the bytes represent 1 valid Ant Message (from
     * SYNC to checksum).
     *
     * @param bytes
     * @return
     */
    public static AntMessage from(final byte[] bytes) throws AntException {
        byte msgLength = (byte) (bytes[1] + 4);// sync, msglen, msgid and checksum excluded
        byte msgId = bytes[2];
        final byte[] msgBytes = Arrays.copyOf(bytes, msgLength);

        Class<? extends AbstractAntMessage> messageClass = registry.get(msgId);
        final AntMessage messageInstance = getMessageInstance(msgBytes, msgId, msgBytes, messageClass);

        LOG.debug(() -> format("Converted %s to %s", ByteUtils.hexString(msgBytes), messageInstance.getClass().getSimpleName()));
        return messageInstance;
    }

    private static AntMessage getMessageInstance(byte[] bytes, byte msgId, byte[] msgBytes, Class<? extends AbstractAntMessage> messageClass) {
        AntMessage messageInstance;
        if (messageClass == null) {
            LOG.error(() -> format("Could not convert %s to an AntMessage. Bytes received were %s", ByteUtils.hexString(msgId),
                    ByteUtils.hexString(msgBytes)));
            messageInstance = new UnknownMessage(bytes);
        } else {
            messageInstance = instantiate(messageClass);
            messageInstance.parse(bytes);
        }
        return messageInstance;
    }

    private static void add(Class<? extends AbstractAntMessage> antMessageImplClass) {
        byte messageId = instantiate(antMessageImplClass).getMessageId();
        if (registry.containsKey(messageId)) {
            Class<? extends AbstractAntMessage> existingClass = registry.get(messageId);
            throw new IllegalStateException(
                    format("Could not add class %s to registry due to duplicate id %s. Conflicting class: %s",
                            antMessageImplClass, messageId, existingClass));
        }
        registry.put(messageId, antMessageImplClass);
    }

    private static AntMessage instantiate(Class<? extends AbstractAntMessage> msgImplClass) {
        try {
            return msgImplClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
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
