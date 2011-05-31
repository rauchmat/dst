package dst3.depinj.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.logging.Logger;

import javassist.ByteArrayClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import dst3.depinj.annotations.Component;

public class InjectionTransformer implements ClassFileTransformer {

	private static final Logger logger = Logger
			.getLogger(InjectionTransformer.class.getName());

	private ClassPool classPool;

	public InjectionTransformer() {
		classPool = ClassPool.getDefault();
		classPool.importPackage("dst3.depinj.annotations");
	}

	@Override
	public byte[] transform(ClassLoader loader, String className,
			Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
			byte[] classfileBuffer) throws IllegalClassFormatException {

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
		return className.startsWith("dst3"); //for performance at demo!
	}
}
