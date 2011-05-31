package dst3.dynload.sample;

import dst3.dynload.IPluginExecutable;

public class SampleExecutable implements IPluginExecutable {

	@Override
	public void execute() {
		System.out.println("SampleExecutable is executing!");
	}

}
