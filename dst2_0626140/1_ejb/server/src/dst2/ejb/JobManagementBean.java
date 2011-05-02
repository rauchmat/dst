package dst2.ejb;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import dst2.ejb.exceptions.InvalidAssignmentException;
import dst2.ejb.exceptions.InvalidCredentialsException;
import dst2.ejb.exceptions.NotLoggedInException;
import dst2.model.Computer;
import dst2.model.Environment;
import dst2.model.Execution;
import dst2.model.Job;
import dst2.model.JobStatus;
import dst2.model.User;

@Stateful
public class JobManagementBean implements JobManagement {

	@PersistenceContext
	private EntityManager em;
	private User user;
	private Map<Long, TemporaryJobs> jobsByGrid;

	@SuppressWarnings("unchecked")
	@Override
	public void login(String username, String password)
			throws InvalidCredentialsException {
		Query query = em.createNamedQuery("usersByCredentials");
		query.setParameter("username", username);
		query.setParameter("password", password);
		List<User> users = query.getResultList();

		if (users.size() != 1) {
			throw new InvalidCredentialsException(
					"Username or password are incorrect.");
		}
		this.user = users.get(0);
		this.jobsByGrid = new HashMap<Long, TemporaryJobs>();
	}

	@SuppressWarnings("unchecked")
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void add(long gridId, int numberOfCPUs, String workflow,
			List<String> parameters) throws InvalidAssignmentException {
		Query query = em.createNamedQuery("freeComputersByGrid");
		query.setParameter("gridId", gridId);
		List<Computer> freeComputers = query.getResultList();
		int usedCPUs = 0;
		boolean isAssignmentValid = false;
		TemporaryJob tempJob = new TemporaryJob();
		tempJob.setWorkflow(workflow);
		tempJob.setParams(parameters);
		for (Computer freeComputer : freeComputers) {
			tempJob.getComputers().add(freeComputer);
			usedCPUs += freeComputer.getCPUs();

			if (usedCPUs >= numberOfCPUs) {
				isAssignmentValid = true;
				break;
			}
		}

		if (!isAssignmentValid)
			throw new InvalidAssignmentException();

		if (!jobsByGrid.containsKey(gridId))
			jobsByGrid.put(gridId, new TemporaryJobs());
		jobsByGrid.get(gridId).add(tempJob);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void clear(long gridId) {
		if (jobsByGrid.containsKey(gridId))
			jobsByGrid.remove(gridId);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public int get(long gridId) {
		if (!jobsByGrid.containsKey(gridId))
			return 0;
		return jobsByGrid.get(gridId).size();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void submit() throws NotLoggedInException,
			InvalidAssignmentException {
		if (user == null)
			throw new NotLoggedInException();

		for (Long gridId : jobsByGrid.keySet()) {
			for (TemporaryJob tempJob : jobsByGrid.get(gridId)) {
				// TODO: check if assignment still valid
				
				Job job = new Job();
				job.setNeeds(new Environment());
				job.getNeeds().setWorkflow(tempJob.getWorkflow());
				job.getNeeds().setParams(tempJob.getParams());
				job.setExecutesIn(new Execution());
				job.getExecutesIn().setStart(new Date());
				job.getExecutesIn().setStatus(JobStatus.SCHEDULED);
				for (Computer computer : tempJob.getComputers()) {
					computer = em.merge(computer);
					computer.getRunning().add(job.getExecutesIn());
				}
				job.setCreatedBy(user);
				job.setIsPaid(false);
	
				em.persist(job);
			}
		}

		discard();
	}

	@Remove
	public void discard() {
	}

}
