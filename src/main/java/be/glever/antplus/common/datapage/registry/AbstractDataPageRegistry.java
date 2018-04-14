package be.glever.antplus.common.datapage.registry;

import be.glever.antplus.common.datapage.AbstractAntPlusDataPage;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * A DataPageRegistry holds Ant+ {@link AbstractAntPlusDataPage}s for a specific device.
 * As some datapages are specific for a given device and others are common ( defined in {@link CommonDataPageRegistry} ),
 * this base class allows grouping multiple registries together for a given Ant+ device type.
 */
public abstract class AbstractDataPageRegistry {

	private Map<Byte, Class<? extends AbstractAntPlusDataPage>> registry = new HashMap<>();

	public AbstractDataPageRegistry() {
	}

	public AbstractDataPageRegistry(AbstractDataPageRegistry... sourceRegistries) {
		for (AbstractDataPageRegistry src : sourceRegistries) {
			addAllFromRegistry(src);
		}
	}

	private void addAllFromRegistry(AbstractDataPageRegistry src) {
		for (Class<? extends AbstractAntPlusDataPage> registryClass : src.registry.values()) {
			add(registryClass);
		}
	}


	public AbstractAntPlusDataPage createDataPage(byte[] messageContentBytes) {
		try {
			return get(messageContentBytes[0]).getDeclaredConstructor().newInstance(messageContentBytes);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new IllegalStateException("Could not instantiate DataPage");
		}
	}

	public Class<? extends AbstractAntPlusDataPage> get(byte dataPageNumber) {
		return registry.get(dataPageNumber);
	}

	protected void add(Class<? extends AbstractAntPlusDataPage> dataPageClass) {
		try {
			byte pageNumber = dataPageClass.getDeclaredConstructor().newInstance().getPageNumber();
			if (this.registry.containsKey(pageNumber)) {
				throw new IllegalStateException("Duplicate DataPage defined in registry: " + pageNumber);
			}

			this.registry.put(pageNumber, dataPageClass);
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			throw new IllegalStateException("Initialization Error. Could not call default constructor on class ["
					+ dataPageClass.getName() + "]");
		}
	}
}
