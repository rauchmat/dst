package dst3.depinj.sample;

import dst3.depinj.annotations.Component;
import dst3.depinj.annotations.ComponentId;

@Component
public class DefaultTransformer implements Transformer {

	@ComponentId
	private Long id;
	
	@Override
	public String transform(String value) {
		return value;
	}

	public Long getId() {
		return id;
	}

}
