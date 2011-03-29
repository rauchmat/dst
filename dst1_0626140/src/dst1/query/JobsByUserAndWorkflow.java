package dst1.query;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import dst1.model.Job;

public class JobsByUserAndWorkflow {
	private Session session;

	public JobsByUserAndWorkflow(EntityManager em) {
		session = (Session) em.unwrap(Session.class);
	}

	@SuppressWarnings("unchecked")
	public List<Job> execute(String username, String workflow) {
		Criteria criteria = session.createCriteria(Job.class).createAlias(
				"createdBy", "cb").createAlias("needs", "n");

		if (username != null)
			criteria = criteria.add(Restrictions.like("cb.username", username));
		if (workflow != null)
			criteria = criteria.add(Restrictions.like("n.workflow", workflow));

		return criteria.list();
	}
}
