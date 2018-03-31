package be.glever.antplus.hrm.datapage.background;

import be.glever.antplus.hrm.datapage.AbstractHRMDataPage;

/**
 * Lists supported capabilities for running/cycling/swimming and if they are enabled.
 * In case this capability is enabled its' specific datapage is transmitted as main datapage instead of the generic {@link be.glever.antplus.hrm.datapage.main.DataPage4PreviousHeartBeatEvent}
 * Currently only swimming seems to be possible ({@link be.glever.antplus.hrm.datapage.main.DataPage5SwimIntervalSummary}).
 */
public class DataPage6Capabilities extends AbstractHRMDataPage {

	public DataPage6Capabilities(byte[] dataPageBytes) {
		super(dataPageBytes);
	}

	@Override
	public int getPageNumber() {
		return 6;
	}

	public boolean isExtendedRunningFeatureSupported() {
		return isBitSet(0b1, getSupportedFeaturesByte());
	}

	public boolean isExtendedCyclingFeaturesSupported() {
		return isBitSet(0b01, getSupportedFeaturesByte());
	}

	public boolean isExtendedSwimmingFeaturesSupported() {
		return isBitSet(0b001, getSupportedFeaturesByte());
	}


	public boolean isExtendedRunningFeaturesEnabled() {
		return isBitSet(0b1, getEnabledFeaturesByte());
	}

	public boolean isExtendedCyclingFeaturesEnabled() {
		return isBitSet(0b01, getEnabledFeaturesByte());
	}

	public boolean isExtendedSwimmingFeaturesEnabled() {
		return isBitSet(0b001, getEnabledFeaturesByte());
	}

	private boolean isBitSet(int mask, byte b) {
		return 1 == (mask & b);
	}

	private byte getSupportedFeaturesByte() {
		return getPageSpecificBytes()[1];
	}

	private byte getEnabledFeaturesByte() {
		return getPageSpecificBytes()[2];
	}
}
