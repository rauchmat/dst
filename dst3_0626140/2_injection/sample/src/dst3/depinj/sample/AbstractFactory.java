package dst3.depinj.sample;

import dst3.depinj.annotations.ComponentId;

public abstract class AbstractFactory implements Factory {

	@ComponentId
	private Long id;

	public Long getId() {
		return id;
	}
}
