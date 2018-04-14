package be.glever.antplus.common.datapage.registry;

import be.glever.antplus.common.datapage.*;

import java.util.HashMap;
import java.util.Map;

public class CommonDataPageRegistry extends AbstractDataPageRegistry {

	private Map<Byte, Class<? extends AbstractAntPlusDataPage>> registry = new HashMap<>();

	public CommonDataPageRegistry() {
		add(DataPage67AntFsClientBeacon.class);
		add(DataPage68AntFsHostCommandResponse.class);
		add(DataPage70Request.class);
		add(DataPage71CommandStatus.class);
		add(DataPage73GenericCommand.class);
		add(DataPage74OpenChannelCommand.class);
		add(DataPage76ModeSettingsCommand.class);
		add(DataPage78MultiComponentSystemManufacturersInformation.class);
		add(DataPage79MultiComponentSystemProductInformation.class);
		add(DataPage80ManufacturersInformation.class);
		add(DataPage81ProductInformation.class);
		add(DataPage82BatteryStatus.class);
		add(DataPage83TimeAndDate.class);
		add(DataPage84SubfieldData.class);
		add(DataPage85MemoryLevel.class);
		add(DataPage86GetPairedDevices.class);
		add(DataPage87ErrorDescription.class);
	}
}
