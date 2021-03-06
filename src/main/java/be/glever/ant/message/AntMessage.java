package be.glever.ant.message;

import be.glever.ant.AntException;

public interface AntMessage {

    byte getMessageId();

    void parse(byte[] bytes) throws AntException;

    byte[] toByteArray();

}
