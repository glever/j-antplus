package be.glever.ant.usb;

import be.glever.ant.message.AntMessage;
import be.glever.ant.message.AntMessageRegistry;
import be.glever.ant.util.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.FluxSink;

import javax.usb.UsbPipe;
import javax.usb.util.DefaultUsbIrp;

/**
 * Reads an {@link UsbPipe} and parses the bytestream to {@link AntMessage}s
 * which can then be retrieved through {@link #antMessages()}().
 *
 * @author glen
 */
public class AntUsbReader implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(AntUsbReader.class);
    public static final byte SYNC = (byte) 0xa4;
    private UsbPipe inPipe;
    private boolean stop = false;
    private final FluxProcessor<AntMessage, AntMessage> usbMessageProcessor = DirectProcessor.<AntMessage>create().serialize();
    private final FluxSink<AntMessage> usbMessageSink = usbMessageProcessor.sink();

    public AntUsbReader(UsbPipe inPipe) {
        this.inPipe = inPipe;
    }

    @Override
    public void run() {
        byte[] buffer = new byte[128];
        try {
            while (!stop) {
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
                    if (buffer[0] == SYNC) {
                        usbMessageSink.next(AntMessageRegistry.from(buffer));
                    } else {
                        // if this happens too much, treat buffer as 'sliding window' instead of only relying on first byte
                        LOG.warn("Buffer {} didn't start with sync byte. Ignoring...", ByteUtils.hexString(buffer));
                    }
                }
            }
        } catch (Throwable t) {
            LOG.error("Fatal error reading from usb device " + t.getMessage(), t);
        }
        usbMessageSink.complete();
    }

    public void stop() {
        this.stop = true;
        this.usbMessageSink.complete();
    }

    public Flux<AntMessage> antMessages() {
        return this.usbMessageProcessor;
    }

}
