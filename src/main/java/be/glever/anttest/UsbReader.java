package be.glever.anttest;

import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;
import javax.usb.UsbNotActiveException;
import javax.usb.UsbNotOpenException;
import javax.usb.UsbPipe;
import javax.usb.event.UsbPipeDataEvent;
import javax.usb.event.UsbPipeErrorEvent;
import javax.usb.event.UsbPipeListener;

import be.glever.ant.util.ByteUtils;

public class UsbReader implements Runnable, UsbPipeListener {

	private UsbPipe inPipe;
	private boolean stop = false;

	public UsbReader(UsbPipe inPipe) {
		this.inPipe = inPipe;
		this.inPipe.addUsbPipeListener(this);
	}

	@Override
	public void run() {
		while (!stop) {
			try {
				inPipe.open();
				int read = inPipe.syncSubmit(new byte[128]);
				if (read == 0) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException ignored) {
						// ignore
					}
				}
				inPipe.close();
			} catch (UsbNotActiveException | UsbNotOpenException | IllegalArgumentException | UsbDisconnectedException
					| UsbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.stop = true;
			}
		}
	}

	public void stop() {
		this.stop = true;
	}

	@Override
	public void errorEventOccurred(UsbPipeErrorEvent event) {
		System.out.println("error event occurred: " + event.getUsbException());
	}

	@Override
	public void dataEventOccurred(UsbPipeDataEvent event) {
		System.out.println(ByteUtils.hexString(event.getUsbIrp().getData()));
		if (event.getUsbIrp().getActualLength() > 0) {
			System.out.println("data event occurred: " + event.getUsbIrp().getActualLength());
		}
	}

}
