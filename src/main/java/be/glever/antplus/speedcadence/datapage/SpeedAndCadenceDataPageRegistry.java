package be.glever.antplus.speedcadence.datapage;

import be.glever.antplus.common.datapage.registry.AbstractDataPageRegistry;
import be.glever.antplus.common.datapage.registry.CommonDataPageRegistry;
import be.glever.antplus.speedcadence.datapage.background.SpeedCadenceDataPage1CumulativeOperatingTime;
import be.glever.antplus.speedcadence.datapage.background.SpeedCadenceDataPage2ManufacturerInformation;
import be.glever.antplus.speedcadence.datapage.background.SpeedCadenceDataPage3ProductInformation;
import be.glever.antplus.speedcadence.datapage.background.SpeedCadenceDataPage4BatteryStatus;
import be.glever.antplus.speedcadence.datapage.main.SpeedAndCadenceDataPage0Default;
import be.glever.antplus.speedcadence.datapage.main.SpeedCadenceDataPage0Default;
import be.glever.antplus.speedcadence.datapage.main.SpeedCadenceDataPage5Motion;

public class SpeedAndCadenceDataPageRegistry extends AbstractDataPageRegistry {
    public SpeedAndCadenceDataPageRegistry() {
        super(new CommonDataPageRegistry());

        add(SpeedAndCadenceDataPage0Default.PAGE_NR, SpeedAndCadenceDataPage0Default::new);
    }
}
