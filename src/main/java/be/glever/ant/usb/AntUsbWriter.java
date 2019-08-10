package be.glever.ant.usb;

import be.glever.ant.AntException;
import be.glever.ant.message.AntMessage;
import be.glever.ant.util.ByteUtils;
import be.glever.util.logging.Log;

import javax.usb.UsbPipe;

import static java.lang.String.format;

public class AntUsbWriter {
    private static final Log LOG = Log.getLogger(AntUsbWriter.class);
    private UsbPipe outPipe;

    public AntUsbWriter(UsbPipe outPipe) {
        this.outPipe = outPipe;
    }

    public synchronized void write(AntMessage message) throws AntException {
        try {
            byte[] messageBytes = message.toByteArray();
            LOG.debug(() -> format("Sending %s with bytes %s.", message.getClass().getSimpleName(), ByteUtils.hexString(messageBytes)));
            outPipe.syncSubmit(messageBytes);
        } catch (Throwable t) {
            throw new AntException(t);
        }
    }
}
