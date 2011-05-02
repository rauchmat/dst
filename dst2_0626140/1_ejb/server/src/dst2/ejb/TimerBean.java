package dst2.ejb;

import java.util.Date;
import java.util.List;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import dst2.model.Execution;
import dst2.model.JobStatus;

@Stateless
public class TimerBean {

	private static Logger logger = LogManager.getLogger(TimerBean.class);

	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")
	@Schedule(minute = "*/1", hour = "*", info = "every minute")
	public void automaticTimeout() {
		logger.info("Timer invoked");

		Query query = em.createNamedQuery("notFinishedExecutions");
		List<Execution> unfinishedExecutions = query.getResultList();
		for (Execution execution : unfinishedExecutions) {
			execution.setEnd(new Date());
			execution.setStatus(JobStatus.FINISHED);
			em.persist(execution);
		}
	}
}
