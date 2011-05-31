package dst3.depinj.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.logging.Logger;

import javassist.ByteArrayClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.bytecode.Descriptor;
import dst3.depinj.annotations.Component;

public class InjectionTransformer implements ClassFileTransformer {

	private static final Logger logger = Logger.getLogger(InjectionTransformer.class
			.getName());

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
				CtClass cc = classPool.get(className);
				logger.info(Boolean.toString(cc.getClass().isAnnotationPresent(Component.class)));
				if (cc.getClass().getAnnotation(Component.class) != null) {
					logger.info("Transforming " + className);
					
					CtConstructor constructor = cc.getConstructor(Descriptor
							.ofConstructor(new CtClass[0]));
					String src = "dst3.depinj.InjectionControllerImpl.getInstance().initialize(this);";
					constructor.insertBefore(src);
				}
				return cc.toBytecode();

			} catch (Exception e) {
				e.printStackTrace();

			}
		}

		return null;

	}

	private boolean shouldTransform(String className) {
		return className.startsWith("dst3");
	}
}
