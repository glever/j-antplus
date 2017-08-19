package be.glever.ant.usb;

import javax.usb.UsbPipe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.glever.ant.message.AntMessage;
import be.glever.ant.message.AntMessageRegistry;
import be.glever.ant.messagebus.MessageBus;

/**
 * Reads an {@link UsbPipe} and parses the bytestream to {@link AntMessage}s
 * which are then dispatched on a {@link MessageBus}.
 * 
 * @author glen
 *
 */
public class AntUsbMessageReader implements Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(AntUsbMessageReader.class);
	private static final int ANT_MSG_SIZE = 8;
	private UsbPipe inPipe;
	private boolean stop = false;
	private MessageBus<AntMessage> messageBus;

	public AntUsbMessageReader(UsbPipe inPipe, MessageBus<AntMessage> messageBus) {
		this.inPipe = inPipe;
		this.messageBus = messageBus;
	}

	@Override
	public void run() {
		while (!stop) {
			try {
				byte[] buffer = new byte[ANT_MSG_SIZE];
				int read = inPipe.syncSubmit(buffer);
				if (read == 0) {
					try {
						// TODO validate if the syncsubmit is blocking or not. if it is blocking, no
						// need for polling
						Thread.sleep(10);
					} catch (InterruptedException ie) {
						Thread.interrupted();
					}
				} else {
					// TODO validate each message is in fact 8 bytes. if not, create some kind of
					// byte scanner class that searches for SYNC and MSG_LEN in order to obtain the
					// msg bytes
					// AntMessageRegistry
					LOG.debug("Read {} bytes", buffer);
					messageBus.put(AntMessageRegistry.from(buffer));
				}
			} catch (Throwable t) {
				this.stop = true;
			}
		}
	}

	public void stop() {
		this.stop = true;
	}

}
