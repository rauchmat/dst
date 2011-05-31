package dst3.dynload;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginExecutorImpl extends TimerTask implements IPluginExecutor {

	private List<File> dirs = new Vector<File>();
	private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 5, 10,
			TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10));
	private Long lastScan;

	private Long interval;
	private Timer timer;

	public PluginExecutorImpl(Long interval) {
		this.interval = interval;
	}

	@Override
	public void monitor(File dir) {
		if (!dir.isDirectory())
			throw new RuntimeException("Directory expected.");

		dirs.add(dir);
	}

	@Override
	public void stopMonitoring(File dir) {
		dirs.remove(dir);
	}

	@Override
	public void start() {
		lastScan = 0L;
		timer = new Timer();
		timer.schedule(this, 0, interval);
	}

	@Override
	public void stop() {
		timer.cancel();
		threadPool.shutdown();
	}

	@Override
	public synchronized void run() {
		Long thisScan = new Date().getTime();

		for (File dir : dirs) {
			scanDir(dir);
		}

		lastScan = thisScan;
	}

	private void scanDir(File dir) {
		for (File file : dir.listFiles()) {
			if (file.isDirectory())
				scanDir(file);
			else if (file.getName().endsWith(".jar")) {
				try {
					scanJar(file);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	private void scanJar(File file) throws IOException {
		if (file.lastModified() > lastScan) {
			JarFile jarFile = new JarFile(file);
			ClassLoader cl = new InjectionAwareClassLoader(jarFile, getClass().getClassLoader());

			for (Enumeration<JarEntry> e = jarFile.entries(); e
					.hasMoreElements();) {
				JarEntry entry = e.nextElement();
				if (!entry.getName().endsWith(".class"))
					continue;
				
				try {
					
					String className = entry.getName().replace("/", ".");
					className = className.substring(0, className.length() - 6); //trim .class at the end
					Class<?> clazz = cl.loadClass(className);
					if (IPluginExecutable.class.isAssignableFrom(clazz)) {
						startThread(clazz);
					}
				} catch (Exception ex) {
				}

			}
		}
	}

	private void startThread(Class<?> clazz) throws InstantiationException,
			IllegalAccessException {
		final IPluginExecutable executable = (IPluginExecutable) clazz.newInstance();
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				executable.execute();
			}
		};
		threadPool.execute(runnable);
	}
}
