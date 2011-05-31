package dst3.dynload;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Program {
	
	public static void main(String[] args) throws IOException {
		IPluginExecutor executor = new PluginExecutorImpl(1000L);
		File plugins = new File("plugins");
		System.out.println(plugins.getAbsoluteFile());
		executor.monitor(plugins);
		executor.start();
		
		System.out.println("Press ENTER to shut down...");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		reader.readLine();
		
		executor.stop();
		System.out.println("Successfully stopped executor.");
	}
}
