package be.glever.antplus.common.datapage.registry;

import be.glever.antplus.common.datapage.*;

import java.util.HashMap;
import java.util.Map;

public class CommonDataPageRegistry extends AbstractDataPageRegistry {

    private Map<Byte, Class<? extends AbstractAntPlusDataPage>> registry = new HashMap<>();

    public CommonDataPageRegistry() {
        add(DataPage67AntFsClientBeacon.PAGE_NR, DataPage67AntFsClientBeacon::new);
        add(DataPage68AntFsHostCommandResponse.PAGE_NR, DataPage68AntFsHostCommandResponse::new);
        add(DataPage70Request.PAGE_NR, DataPage70Request::new);
        add(DataPage71CommandStatus.PAGE_NR, DataPage71CommandStatus::new);
        add(DataPage73GenericCommand.PAGE_NR, DataPage73GenericCommand::new);
        add(DataPage74OpenChannelCommand.PAGE_NR, DataPage74OpenChannelCommand::new);
        add(DataPage76ModeSettingsCommand.PAGE_NR, DataPage76ModeSettingsCommand::new);
        add(DataPage78MultiComponentSystemManufacturersInformation.PAGE_NR, DataPage78MultiComponentSystemManufacturersInformation::new);
        add(DataPage79MultiComponentSystemProductInformation.PAGE_NR, DataPage79MultiComponentSystemProductInformation::new);
        add(DataPage80ManufacturersInformation.PAGE_NR, DataPage80ManufacturersInformation::new);
        add(DataPage81ProductInformation.PAGE_NR, DataPage81ProductInformation::new);
        add(DataPage82BatteryStatus.PAGE_NR, DataPage82BatteryStatus::new);
        add(DataPage83TimeAndDate.PAGE_NR, DataPage83TimeAndDate::new);
        add(DataPage84SubfieldData.PAGE_NR, DataPage84SubfieldData::new);
        add(DataPage85MemoryLevel.PAGE_NR, DataPage85MemoryLevel::new);
        add(DataPage86GetPairedDevices.PAGE_NR, DataPage86GetPairedDevices::new);
        add(DataPage87ErrorDescription.PAGE_NR, DataPage87ErrorDescription::new);
    }
}
