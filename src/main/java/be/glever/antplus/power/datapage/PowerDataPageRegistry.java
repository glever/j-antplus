package be.glever.antplus.power.datapage;

import be.glever.antplus.common.datapage.registry.AbstractDataPageRegistry;
import be.glever.antplus.common.datapage.registry.CommonDataPageRegistry;
import be.glever.antplus.power.datapage.main.*;

public class PowerDataPageRegistry extends AbstractDataPageRegistry {
    public PowerDataPageRegistry() {
        super(new CommonDataPageRegistry());

        add(PowerDataPage10PowerOnly.PAGE_NR, PowerDataPage10PowerOnly::new);
        add(PowerDataPage11WheelTorque.PAGE_NR, PowerDataPage11WheelTorque::new);
        add(PowerDataPage12CrankTorque.PAGE_NR, PowerDataPage12CrankTorque::new);
        add(PowerDataPage13TorqueEffectivenessPedalSmoothness.PAGE_NR, PowerDataPage13TorqueEffectivenessPedalSmoothness::new);
        add(PowerDataPage20CrankTorqueFrequency.PAGE_NR, PowerDataPage20CrankTorqueFrequency::new);
    }
}
