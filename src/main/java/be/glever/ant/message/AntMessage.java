package be.glever.ant.message;

import be.glever.ant.AntException;

public interface AntMessage {

	byte getMessageId();

	byte[] getMessageContent();

	void parse(byte[] bytes) throws AntException;

}