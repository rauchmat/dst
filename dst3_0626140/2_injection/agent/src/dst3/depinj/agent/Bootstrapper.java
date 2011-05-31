package dst3.depinj.agent;

import java.lang.instrument.Instrumentation;

public class Bootstrapper {
	public static void premain(String agentArguments, Instrumentation instrumentation) {	
		instrumentation.addTransformer(new InjectionTransformer());
	}
}
