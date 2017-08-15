package be.glever.ant;

/**
 * Taken from the "ANTUSB-m_Stick.pdf" datasheet.
 * @author glen
 *
 */
public class MUsbAntConstants implements AntConstants{

	@Override
	public int getMaxChannels() {
		return 8;
	}

	@Override
	public int getMaxNetworks() {
		return 8;
	}

	@Override
	public int getMaxBufferSize() {
		return 724;
	}

	@Override
	public int getMaxSelectiveDataUpdateMasks() {
		return 2;
	}

	@Override
	public int getMaxAddress() {
		return 500;
	}

}
