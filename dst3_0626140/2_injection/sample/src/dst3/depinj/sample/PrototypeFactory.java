package dst3.depinj.sample;

import dst3.depinj.annotations.Component;
import dst3.depinj.annotations.ScopeType;

@Component(ScopeType.PROTOTYPE)
public class PrototypeFactory extends AbstractFactory {

	@Override
	public String create() {
		return "A";
	}

}
