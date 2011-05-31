package dst3.depinj.sample;

import java.util.logging.Logger;

import dst3.depinj.InjectionControllerImpl;

public class WithoutAgent {

	private static final Logger logger = Logger.getLogger(WithoutAgent.class
			.getName());

	public static void main(String[] args) {
		SampleWorker worker = new SampleWorker();
		InjectionControllerImpl.getInstance().initialize(worker);

		logger.info("worker.id: " + worker.getId());
		logger.info("worker.factory.id: " + worker.getFactory().getId());
		logger.info("worker.transformer.id: " + worker.getTransformer().getId());
		logger.info("worker.logger: " + worker.getLogger());
		logger.info("worker.doWork(): " + worker.doWork());

	}

}
