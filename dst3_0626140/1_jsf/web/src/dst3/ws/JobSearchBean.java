package dst3.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import dst3.model.Job;

@Stateless
@WebService
public class JobSearchBean {

	private static final Logger logger = Logger.getLogger(JobSearchBean.class
			.getName());

	@PersistenceContext
	private EntityManager em;

	@WebMethod
	public List<JobDto> find(String gridName) {
		Query query = em.createNamedQuery("unfinishedJobsByGridname");
		query.setParameter("gridname", gridName);
		@SuppressWarnings("unchecked")
		List<Job> jobs = query.getResultList();

		List<JobDto> result = new ArrayList<JobDto>();
		for (Job job : jobs) {
			JobDto dto = new JobDto();
			dto.setId(job.getId());
			dto.setStart(job.getExecutesIn().getStart());
			dto.setEnd(job.getExecutesIn().getEnd());
			dto.setUsername(job.getCreatedBy().getUsername());
			result.add(dto);
		}

		logger.info("Found " + result.size() + " jobs.");

		return result;
	}

}
