package dst1.query;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;

import dst1.model.Execution;
import dst1.model.Job;
import dst1.model.JobStatus;

public class FinishedJobsByStartAndEnd {
	private Session session;

	public FinishedJobsByStartAndEnd(EntityManager em) {
		session = (Session) em.unwrap(Session.class);
	}

	@SuppressWarnings("unchecked")
	public List<Job> execute(Date start, Date end) {
		Execution exec = new Execution();
		exec.setStatus(JobStatus.FINISHED);
		exec.setStart(start);
		exec.setEnd(end);
		Criteria criteria = session.createCriteria(Job.class).createCriteria(
				"executesIn").add(Example.create(exec));
		return criteria.list();
	}
}
