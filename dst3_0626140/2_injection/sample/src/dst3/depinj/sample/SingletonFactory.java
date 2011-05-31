package dst3.depinj.sample;

import dst3.depinj.annotations.Component;
import dst3.depinj.annotations.ScopeType;

@Component(ScopeType.SINGLETON)
public class SingletonFactory extends AbstractFactory {

	@Override
	public String create() {
		return "B";
	}

}
