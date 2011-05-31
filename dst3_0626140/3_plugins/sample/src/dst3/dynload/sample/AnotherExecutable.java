package dst3.dynload.sample;

import dst3.dynload.IPluginExecutable;

public class AnotherExecutable implements IPluginExecutable {

	@Override
	public void execute() {
		System.out.println("AnotherExecutable is executing! But this might take a bit...");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("AnotherExecutable has finally finished!");
	}

}
