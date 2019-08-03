package be.glever.ant.usb;

import be.glever.ant.AntException;
import be.glever.ant.message.AntMessage;
import be.glever.ant.util.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.usb.UsbPipe;

public class AntUsbWriter {
    private static final Logger LOG = LoggerFactory.getLogger(AntUsbWriter.class);
    private UsbPipe outPipe;

    public AntUsbWriter(UsbPipe outPipe) {
        this.outPipe = outPipe;
    }

    public synchronized void write(AntMessage message) throws AntException {
        try {
            byte[] messageBytes = message.toByteArray();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Sending {} with bytes {}.", message.getClass().getSimpleName(), ByteUtils.hexString(messageBytes));
            }
            outPipe.syncSubmit(messageBytes);
        } catch (Throwable t) {
            throw new AntException(t);
        }
    }
}
