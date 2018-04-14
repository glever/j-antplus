package be.glever.antplus.hrm.datapage;

import be.glever.antplus.common.datapage.registry.AbstractDataPageRegistry;
import be.glever.antplus.common.datapage.registry.CommonDataPageRegistry;
import be.glever.antplus.hrm.datapage.background.*;
import be.glever.antplus.hrm.datapage.main.HrmDataPage0Default;
import be.glever.antplus.hrm.datapage.main.HrmDataPage4PreviousHeartBeatEvent;
import be.glever.antplus.hrm.datapage.main.HrmDataPage5SwimIntervalSummary;

public class HrmDataPageRegistry extends AbstractDataPageRegistry {
	public HrmDataPageRegistry() {
		super(new CommonDataPageRegistry());

		// add hrm specific datapages here
		add(HrmDataPage0Default.class);
		add(HrmDataPage4PreviousHeartBeatEvent.class);
		add(HrmDataPage5SwimIntervalSummary.class);

		add(HrmDataPage1CumulativeOperatingTime.class);
		add(HrmDataPage2ManufacturerInformation.class);
		add(HrmDataPage3ProductInformation.class);
		add(HrmDataPage6Capabilities.class);
		add(HrmDataPage7BatteryStatus.class);
	}
}
