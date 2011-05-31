package dst3.dynload;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.IllegalClassFormatException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import javassist.ByteArrayClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import dst3.depinj.annotations.Component;

public class InjectionAwareClassLoader extends ClassLoader {

	private static final int BUFFER_SIZE = 4096;
	private static final Logger logger = Logger
			.getLogger(InjectionAwareClassLoader.class.getName());
	private JarFile jarFile;
	private ClassPool classPool;

	public InjectionAwareClassLoader(JarFile jarFile, ClassLoader parent) {
		super(parent);
		this.jarFile = jarFile;
		this.classPool = ClassPool.getDefault();
		classPool.importPackage("dst3.depinj.annotations");
	}

	@Override
	protected synchronized Class<?> loadClass(String className, boolean resolve)
			throws ClassNotFoundException {
		// logger.info("Loading class '" + className + "'");

		Class<?> clazz = findLoadedClass(className);
		if (clazz != null) {
			// logger.info("Class '" + className + "' already loaded.");
			return clazz;
		}

		if (getParent() != null) {
			try {
				// needed since IPluginExecutable is loaded twice and not seen
				// as same type
				clazz = getParent().loadClass(className);
				if (clazz != null) {
					// logger.info("Class '" + className + "' already loaded.");
					return clazz;
				}
			} catch (Exception e) {
			}

		}

		String classFile = className.replace('.', File.separatorChar)
				+ ".class";
		byte[] classBytes = null;

		try {
			JarEntry entry = jarFile.getJarEntry(classFile);
			InputStream in = jarFile.getInputStream(entry);
			byte[] buffer = new byte[BUFFER_SIZE];
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int n = -1;
			while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
				out.write(buffer, 0, n);
			}
			classBytes = out.toByteArray();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error while loading class file.", e);
		}

		if (classBytes == null) {
			throw new ClassNotFoundException("Unable to load class: '"
					+ className + "'");
		}

		try {
			classBytes = transform(className, classBytes);
		} catch (IllegalClassFormatException e1) {
			logger.log(Level.WARNING, "Error while manipulation byte code.", e1);
		}

		try {
			clazz = defineClass(className, classBytes, 0, classBytes.length);
			if (resolve) {
				resolveClass(clazz);
			}
		} catch (SecurityException e) {
			// core classes throw a SecurityException and must be loaded via
			// base.
			clazz = super.loadClass(className, resolve);
		}

		// logger.info("Class '" + className + "' successfully loaded.");
		return clazz;
	}

	public byte[] transform(String className, byte[] classfileBuffer)
			throws IllegalClassFormatException {

		if (shouldTransform(className)) {
			logger.info("Analyzing " + className);

			try {

				classPool.insertClassPath(new ByteArrayClassPath(className,
						classfileBuffer));
				CtClass cc = classPool.get(className.replace("/", "."));

				if (cc.hasAnnotation(Component.class)) {
					logger.info("Transforming " + className);

					for (CtConstructor constructor : cc.getConstructors()) {
						String src = "{ dst3.depinj.InjectionControllerImpl.getInstance().initialize($0); }";
						constructor.insertBeforeBody(src);
					}

				}

				return cc.toBytecode();

			} catch (Exception e) {
				e.printStackTrace();

			}
		}

		return classfileBuffer;

	}

	private boolean shouldTransform(String className) {
		return className.startsWith("dst3"); // for performance at demo!
	}
}
