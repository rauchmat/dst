package dst3.depinj.sample;

import dst3.depinj.annotations.Component;
import dst3.depinj.annotations.ComponentId;
import dst3.depinj.annotations.Inject;

@Component
public class SampleWorker extends AbstractWorker {

	@ComponentId
	private Long id;
	@Inject(required = true, specificType = SingletonFactory.class)
	private AbstractFactory factory;
	@Inject(required = true)
	private DefaultTransformer transformer;

	public Long getId() {
		return id;
	}

	public DefaultTransformer getTransformer() {
		return transformer;
	}

	public AbstractFactory getFactory() {
		return factory;
	}

	@Override
	protected String doWork() {
		return getTransformer().transform(getFactory().create());
	}

}
