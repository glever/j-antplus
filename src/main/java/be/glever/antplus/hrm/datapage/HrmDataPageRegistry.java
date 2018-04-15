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
		add(HrmDataPage0Default.PAGE_NR, HrmDataPage0Default::new);
		add(HrmDataPage4PreviousHeartBeatEvent.PAGE_NR, HrmDataPage4PreviousHeartBeatEvent::new);
		add(HrmDataPage5SwimIntervalSummary.PAGE_NR, HrmDataPage5SwimIntervalSummary::new);

		add(HrmDataPage1CumulativeOperatingTime.PAGE_NR, HrmDataPage1CumulativeOperatingTime::new);
		add(HrmDataPage2ManufacturerInformation.PAGE_NR, HrmDataPage2ManufacturerInformation::new);
		add(HrmDataPage3ProductInformation.PAGE_NR, HrmDataPage3ProductInformation::new);
		add(HrmDataPage6Capabilities.PAGE_NR, HrmDataPage6Capabilities::new);
		add(HrmDataPage7BatteryStatus.PAGE_NR, HrmDataPage7BatteryStatus::new);
	}
}
