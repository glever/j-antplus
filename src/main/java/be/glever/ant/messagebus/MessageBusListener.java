package be.glever.ant.messagebus;

public interface MessageBusListener<T> {

	/**
	 * Handle the message. Afterwards, the listener answers if it is still
	 * interested in treating more messages.
	 * 
	 * @return true if listener is interested in treating any more messages. False
	 *         if otherwise.
	 */
	public boolean handle(T t);

}
