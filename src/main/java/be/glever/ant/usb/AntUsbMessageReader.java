package be.glever.ant.usb;

import javax.usb.UsbPipe;
import javax.usb.util.DefaultUsbIrp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.glever.ant.message.AntMessage;
import be.glever.ant.message.AntMessageRegistry;
import be.glever.ant.messagebus.MessageBus;
import be.glever.ant.util.ByteUtils;

/**
 * Reads an {@link UsbPipe} and parses the bytestream to {@link AntMessage}s
 * which are then dispatched on a {@link MessageBus}.
 * 
 * @author glen
 *
 */
public class AntUsbMessageReader implements Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(AntUsbMessageReader.class);
	private UsbPipe inPipe;
	private boolean stop = false;
	private MessageBus<AntMessage> messageBus;
	private Thread runningThread;

	public AntUsbMessageReader(UsbPipe inPipe, MessageBus<AntMessage> messageBus) {
		this.inPipe = inPipe;
		this.messageBus = messageBus;
	}

	@Override
	public void run() {
		runningThread = Thread.currentThread();
		while (!stop) {
			try {
				byte[] buffer = new byte[128];
				DefaultUsbIrp irp = new DefaultUsbIrp(buffer);
				irp.waitUntilComplete(100);
				inPipe.syncSubmit(irp);
				if (irp.getData().length == 0) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException ie) {
						Thread.interrupted();
					}
				} else {
					if (LOG.isDebugEnabled()) {
						LOG.debug("Read {} bytes", ByteUtils.hexString(buffer));						
					}
					
					messageBus.put(AntMessageRegistry.from(buffer));
				}
			} catch (Throwable t) {
				if(! stop) {
					LOG.error(t.getMessage(), t);					
				}
			}
		}
	}

	public void stop() {
		this.stop = true;
		runningThread.interrupt();
	}

}
