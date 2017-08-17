package be.glever.ant.usb;

import javax.usb.UsbPipe;

public class UsbReader implements Runnable {

	private UsbPipe inPipe;
	private boolean stop = false;

	public UsbReader(UsbPipe inPipe) {
		this.inPipe = inPipe;
	}

	@Override
	public void run() {
		while (!stop) {
			try {
				int read = inPipe.syncSubmit(new byte[128]);
				if (read == 0) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException ie) {
						Thread.interrupted();
					}
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
