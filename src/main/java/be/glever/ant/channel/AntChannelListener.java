package be.glever.ant.channel;

import be.glever.ant.message.AntMessage;

public interface AntChannelListener {
	void handle(AntMessage message);
}
