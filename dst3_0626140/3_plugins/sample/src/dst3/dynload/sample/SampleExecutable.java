package dst3.dynload.sample;

import java.util.logging.Logger;

import dst3.dynload.IPluginExecutable;

public class SampleExecutable implements IPluginExecutable {

	private static final Logger logger = Logger
			.getLogger(SampleExecutable.class.getName());

	@Override
	public void execute() {

		try {
			logger.info("Started to execute. I am a fast task!");
			
			Thread.sleep(200);
			
			logger.info("Oh cool! There's more work to do...");
			
			Thread.sleep(2000);
			
			logger.info("Finished! Yippie!");
		} catch (InterruptedException e) {
		}
	}

}
