package be.glever.ant.usb;

import be.glever.ant.message.AntMessage;
import be.glever.ant.message.AntMessageRegistry;
import be.glever.ant.messagebus.MessageBus;
import be.glever.ant.messagebus.MessageBusListener;
import be.glever.ant.util.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.usb.UsbPipe;
import javax.usb.util.DefaultUsbIrp;

/**
 * Reads an {@link UsbPipe} and parses the bytestream to {@link AntMessage}s
 * which are then dispatched on a {@link MessageBus}.
 *
 * @author glen
 *
 */
public class AntUsbMessageReader implements Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(AntUsbMessageReader.class);
	public static final byte SYNC = (byte) 0xa4;
	private UsbPipe inPipe;
	private boolean stop = false;
	private MessageBus<AntMessage> messageBus;
	private Thread runningThread;
	private boolean process = false;

	public AntUsbMessageReader(UsbPipe inPipe) {
		this.inPipe = inPipe;
		this.messageBus = new MessageBus<>();
	}

	@Override
	public void run() {
		runningThread = Thread.currentThread();
		byte[] buffer = new byte[128];
		while (!stop) {
			try {
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
					if(process && buffer[0] ==  SYNC){
						messageBus.put(AntMessageRegistry.from(buffer));
					}else{
						// if this happens too much, treat buffer as 'sliding window' instead of only relying on first byte
						LOG.warn("Buffer{} didn't start with sync byte. Ignoring...", ByteUtils.hexString(buffer));
					}

				}
			} catch (Throwable t) {
				if (!stop) {
					LOG.error(t.getMessage(), t);
				}
			}
		}
	}

	public void stop() {
		this.stop = true;
		runningThread.interrupt();
	}

	public void addQueueListener(long timeout, int nrOfMessages, MessageBusListener lister) {
		this.messageBus.addQueueListener(timeout, nrOfMessages, lister);
	}

	public void close() {
		this.messageBus.close();
	}

	public void startProcessing() {
		this.process = true;
	}
}
