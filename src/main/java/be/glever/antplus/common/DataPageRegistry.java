package be.glever.antplus.common;

import be.glever.antplus.common.datapage.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class DataPageRegistry {

	private static DataPageRegistry instance = new DataPageRegistry();

	private Map<Byte, Class<? extends AbstractAntPlusDataPage>> registry = new HashMap<>();

	private DataPageRegistry() {
		addCommonDataPages();
		addHrmDataPages();
	}

	public static AbstractAntPlusDataPage createDataPage(byte[] messageContentBytes) {
		try {
			return get(messageContentBytes[0]).getDeclaredConstructor().newInstance(messageContentBytes);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new IllegalStateException("Could not instantiate DataPage");
		}
	}

	private void addHrmDataPages() {
	}

	private void addCommonDataPages() {
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

	public static Class<? extends AbstractAntPlusDataPage> get(byte dataPageNumber) {
		return instance.registry.get(dataPageNumber);
	}

	private void add(Class<? extends AbstractAntPlusDataPage> dataPageClass) {
		try {
			AbstractAntPlusDataPage theInstance = dataPageClass.getDeclaredConstructor().newInstance();
			if (this.registry.containsKey(theInstance.getPageNumber())) {
				throw new IllegalStateException("Duplicate DataPage defined in registry: " + theInstance.getPageNumber());
			}

			this.registry.put(theInstance.getPageNumber(), dataPageClass);
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			throw new IllegalStateException("Initialization Error. Could not call default constructor on class ["
					+ dataPageClass.getName() + "]");
		}
	}
}
