package be.glever.ant.message;

import java.util.HashMap;
import java.util.Map;

import be.glever.ant.AntException;
import be.glever.ant.message.configuration.AssignChannelMessage;
import be.glever.ant.message.configuration.ChannelIdMessage;
import be.glever.ant.message.configuration.ChannelPeriodMessage;
import be.glever.ant.message.configuration.ChannelRfFrequencyMessage;
import be.glever.ant.message.configuration.SearchTimeoutMessage;
import be.glever.ant.message.configuration.UnassignChannelMessage;
import be.glever.ant.message.requestedresponse.CapabilitiesResponseMessage;

public class AntMessageRegistry {
	private Map<Byte, Class<? extends AbstractAntMessage>> registry = new HashMap<>();

	public AntMessageRegistry() throws AntException {
		add(AssignChannelMessage.class);
		add(ChannelIdMessage.class);
		add(ChannelPeriodMessage.class);
		add(ChannelRfFrequencyMessage.class);
		add(SearchTimeoutMessage.class);
		add(UnassignChannelMessage.class);
		add(CapabilitiesResponseMessage.class);
	}

	private void add(Class<? extends AbstractAntMessage> antMessageImplClass) throws AntException {		
		byte messageId = instantiate(antMessageImplClass).getMessageId();
		if(this.registry.containsKey(messageId)) {
			throw new AntException(String.format("Could not add class %s to registry due to duplicate id %s.", antMessageImplClass, messageId));
		}
		this.registry.put(messageId, antMessageImplClass);
	}

	/**
	 * Parses the given bytes into the correct {@link AbstractAntMessage}
	 * implementation. Precondition: the bytes represent 1 valid Ant Message (from
	 * SYNC to checksum).
	 * 
	 * @param bytes
	 * @return
	 */
	public AntMessage parse(byte[] bytes) throws AntException {
		byte msgId = bytes[2];
		Class<? extends AbstractAntMessage> msgImpl = this.registry.get(msgId);
		AntMessage messageInstance = instantiate(msgImpl);
		messageInstance.parse(bytes);
		return messageInstance;
	}

	private AntMessage instantiate(Class<? extends AbstractAntMessage> msgImplClass) throws AntException {
		try {
			return msgImplClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new AntException("Initialization Error. Could not call default constructor on class ["
					+ msgImplClass.getName() + "]");
		}
	}

}
