package dst3.depinj.sample;

import dst3.depinj.annotations.Inject;

public abstract class AbstractWorker {
	@Inject(required = false)
	private Logger logger;

	public Logger getLogger() {
		return logger;
	}
	
	public String work() {
		if(logger != null)
			logger.log("begin work");
		
		String result = doWork();
		
		if(logger != null)
			logger.log("end work");
		
		return result;
	}

	protected abstract String doWork();
	
	
}
