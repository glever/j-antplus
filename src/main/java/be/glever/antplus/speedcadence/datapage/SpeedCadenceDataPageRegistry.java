package be.glever.antplus.speedcadence.datapage;

import be.glever.antplus.common.datapage.registry.AbstractDataPageRegistry;
import be.glever.antplus.common.datapage.registry.CommonDataPageRegistry;
import be.glever.antplus.speedcadence.datapage.background.*;
import be.glever.antplus.speedcadence.datapage.main.SpeedCadenceDataPage0Default;
import be.glever.antplus.speedcadence.datapage.main.SpeedCadenceDataPage5Motion;

public class SpeedCadenceDataPageRegistry extends AbstractDataPageRegistry {
    public SpeedCadenceDataPageRegistry() {
        super(new CommonDataPageRegistry());

        add(SpeedCadenceDataPage0Default.PAGE_NR, SpeedCadenceDataPage0Default::new);
        add(SpeedCadenceDataPage1CumulativeOperatingTime.PAGE_NR, SpeedCadenceDataPage1CumulativeOperatingTime::new);
        add(SpeedCadenceDataPage2ManufacturerInformation.PAGE_NR, SpeedCadenceDataPage2ManufacturerInformation::new);
        add(SpeedCadenceDataPage3ProductInformation.PAGE_NR, SpeedCadenceDataPage3ProductInformation::new);
        add(SpeedCadenceDataPage4BatteryStatus.PAGE_NR, SpeedCadenceDataPage4BatteryStatus::new);
        add(SpeedCadenceDataPage5Motion.PAGE_NR, SpeedCadenceDataPage5Motion::new);
    }
}
