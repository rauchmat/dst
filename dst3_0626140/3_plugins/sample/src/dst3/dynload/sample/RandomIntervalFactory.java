package dst3.dynload.sample;

import java.util.Random;

import dst3.depinj.annotations.Component;
import dst3.depinj.annotations.ComponentId;

@Component
public class RandomIntervalFactory implements IntervalFactory {

	@ComponentId
	private Long id;

	private Random rand;

	public RandomIntervalFactory() {
		this.rand = new Random();
	}

	@Override
	public Long create() {
		return rand.nextLong() % 3000;
	}

}
