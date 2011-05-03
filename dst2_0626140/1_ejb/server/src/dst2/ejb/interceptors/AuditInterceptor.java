package dst2.ejb.interceptors;

import java.util.Date;
import java.util.logging.Logger;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import dst2.model.Invocation;
import dst2.model.InvocationParameter;

public class AuditInterceptor {
	@PersistenceContext
	private EntityManager em;

	private static final Logger logger = Logger
			.getLogger(AuditInterceptor.class.getName());

	@AroundInvoke
	public Object audit(InvocationContext ctx) throws Exception {
		try {
			Object result = ctx.proceed();
			String resultString = getStringRepresentation(result);
			persistInvocation(ctx, resultString);
			return result;
		} catch (Exception e) {
			persistInvocation(ctx, getStringRepresentation(e.getMessage()));
			throw e;
		}

	}

	private String getStringRepresentation(Object obj) {
		String resultString;
		if (obj == null)
			resultString = "{null}";
		else
			resultString = obj.toString();
		
		logger.info("resultString: " + resultString);
		return resultString;
	}

	private void persistInvocation(InvocationContext ctx, String resultString) {
		Invocation invocation = new Invocation();
		invocation.setInvocationTime(new Date());
		invocation.setMethodName(ctx.getMethod().getName());
		invocation.setResult(resultString);
		em.persist(invocation);

		Object[] parameters = ctx.getParameters();
		if (parameters != null) {
			logger.info("parameters.length: " + parameters.length);
			for (int i = 0; i < parameters.length; i++) {
				Object param = parameters[i];
				if (param != null) {
					InvocationParameter parameter = new InvocationParameter(
							invocation, i);
					parameter.setClassName(param.getClass().getName());
					parameter.setValue(param.toString());
					em.persist(parameter);
				}
			}
		}
	}
}
