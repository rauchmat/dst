package dst3.dynload.sample;

import dst3.depinj.annotations.Component;
import dst3.depinj.annotations.ComponentId;

@Component
public class StaticIntervalFactory implements IntervalFactory {

	@ComponentId
	private Long id;
	
	@Override
	public Long create() {
		return 500L;
	}

}
