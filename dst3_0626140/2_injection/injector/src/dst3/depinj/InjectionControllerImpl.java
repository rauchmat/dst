package dst3.depinj;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import dst3.depinj.annotations.Component;
import dst3.depinj.annotations.ComponentId;
import dst3.depinj.annotations.Inject;
import dst3.depinj.annotations.ScopeType;

public class InjectionControllerImpl implements IInjectionController {

	private static IInjectionController instance = null;

	private Map<Class<?>, Object> singletons = new HashMap<Class<?>, Object>();
	private long idCounter = 0;

	public synchronized static IInjectionController getInstance() {
		if (instance == null) {
			instance = new InjectionControllerImpl();
		}
		return instance;
	}

	@Override
	public void initialize(Object obj) throws InjectionException {
		Class<? extends Object> clazz = obj.getClass();
		Component component = clazz.getAnnotation(Component.class);

		if (component == null) {
			throw new InjectionException("The object to initialize of type '"
					+ clazz.getName() + "' is no component.");
		}

		if (component.value() == ScopeType.SINGLETON
				&& singletons.containsKey(clazz)) {
			throw new InjectionException("The singleton '" + clazz.getName()
					+ "' was already initialized.");
		}

		Long componentId = null;
		Class<? extends Object> declaringClass = clazz;
		while (declaringClass != null) {
			for (Field field : declaringClass.getDeclaredFields()) {

				if (field.isAnnotationPresent(ComponentId.class)) {
					if (componentId == null)
						componentId = createComponentId();
					if (!Long.class.isAssignableFrom(field.getType())) {
						throw new InjectionException(
								"The field "
										+ declaringClass.getName()
										+ "."
										+ field.getName()
										+ "annotated with @ComponentId must be of type java.util.Long");
					}

					try {
						setField(obj, field, componentId);
					} catch (IllegalAccessException e) {
						throw new InjectionException(e);
					}
				}

				if (field.isAnnotationPresent(Inject.class)) {
					Inject inject = field.getAnnotation(Inject.class);
					Class<?> typeToInject = inject.specificType();
					if (typeToInject == Object.class)
						typeToInject = field.getType();

					try {
						Object objToInject = getOrCreateManagedObject(typeToInject);
						setField(obj, field, objToInject);
					} catch (Exception e) {
						if (inject.required()) {
							throw new InjectionException(e);
						}
					}
				}
			}
			
			declaringClass = declaringClass.getSuperclass();
		}

		if (componentId == null) {
			throw new InjectionException("The class '" + clazz.getName()
					+ "' has no field annotated with @ComponentId.");
		}

	}

	private void setField(Object obj, Field field, Object value)
			throws IllegalAccessException {
		boolean isAccessible = field.isAccessible();
		field.setAccessible(true);
		field.set(obj, value);
		field.setAccessible(isAccessible);
	}

	private synchronized Long createComponentId() {
		return ++idCounter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized <T> T getSingletonInstance(Class<T> clazz)
			throws InjectionException {
		if (!singletons.containsKey(clazz)) {
			try {
				Object instance = createManagedObject(clazz);
				singletons.put(clazz, instance);
			} catch (Exception e) {
				throw new InjectionException(e);
			}
		}

		return (T) singletons.get(clazz);
	}

	private synchronized <T> Object getOrCreateManagedObject(Class<T> clazz)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		if (singletons.containsKey(clazz))
			return singletons.get(clazz);

		return createManagedObject(clazz);
	}

	private <T> Object createManagedObject(Class<T> clazz)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		@SuppressWarnings("unchecked")
		T instance = (T) Class.forName(clazz.getName()).newInstance();
		initialize(instance);
		return instance;
	}

}
