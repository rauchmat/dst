package dst3.dynload.sample;

import java.util.logging.Logger;

import dst3.depinj.annotations.Component;
import dst3.depinj.annotations.ComponentId;
import dst3.depinj.annotations.Inject;
import dst3.dynload.IPluginExecutable;

@Component
public class AnotherExecutable implements IPluginExecutable {

	@ComponentId
	private Long id;
	
	@Inject(specificType = RandomIntervalFactory.class)
	private IntervalFactory intervalFactory;
	
	private static final Logger logger = Logger
	.getLogger(AnotherExecutable.class.getName());
	
	@Override
	public void execute() {
		try {
			logger.info("Started to execute. Hmmm, there is so much work to do :(");
			
			Thread.sleep(intervalFactory.create());
			
			logger.info("I need a coffee break. Can't concentrate any longer...");
			
			Thread.sleep(intervalFactory.create());
			logger.info("Back from coffee break. Work didn't get less in the mean time.");
			
			Thread.sleep(intervalFactory.create());
			logger.info("Finally finished!");
		} catch (InterruptedException e) {
		}
	}

}
