package dst3.depinj.sample;

import java.util.logging.Logger;

public class WithAgent {

	private static final Logger logger = Logger.getLogger(WithAgent.class
			.getName());

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SampleWorker worker = new SampleWorker();

		logger.info("worker.id: " + worker.getId());
		logger.info("worker.factory.id: " + worker.getFactory().getId());
		logger.info("worker.transformer.id: " + worker.getTransformer().getId());
		logger.info("worker.logger: " + worker.getLogger());
		logger.info("worker.doWork(): " + worker.doWork());
	}

}
