package dst2.ejb;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import dst2.ejb.exceptions.InvalidAssignmentException;
import dst2.ejb.exceptions.InvalidCredentialsException;
import dst2.ejb.exceptions.NotLoggedInException;
import dst2.ejb.interceptors.AuditInterceptor;
import dst2.model.Computer;
import dst2.model.Environment;
import dst2.model.Execution;
import dst2.model.Job;
import dst2.model.JobStatus;
import dst2.model.User;

@Stateful
@Interceptors(AuditInterceptor.class)
public class JobManagementBean implements JobManagement {

	private static Logger logger = Logger.getLogger(JobManagementBean.class
			.getName());

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
	public void add(long gridId, int numberOfCPUs, String workflow,
			List<String> parameters) throws InvalidAssignmentException {
		Query query = em.createNamedQuery("freeComputersByGrid");
		query.setParameter("gridId", gridId);
		List<Computer> freeComputers = query.getResultList();

		TemporaryJobs tempJobs = null;
		if (!jobsByGrid.containsKey(gridId))
			tempJobs = new TemporaryJobs();
		else
			tempJobs = jobsByGrid.get(gridId);
		TemporaryJob tempJob = new TemporaryJob();
		tempJob.setWorkflow(workflow);
		tempJob.setParams(parameters);
		logger.info("Assigning " + workflow + " to grid " + gridId);
		logger.info("tempJobs: " + tempJobs.size());
		List<Computer> reallyFreeComputers = new LinkedList<Computer>();
		for (Computer freeComputer : freeComputers) {
			boolean isReallyFree = true;
			for (TemporaryJob temporaryJob : tempJobs) {
				logger.info("temporaryJob.getComputers(): "
						+ temporaryJob.getComputers().size());
				if (temporaryJob.getComputers().contains(freeComputer)) {
					isReallyFree = false;
					break;
				}
			}

			if (isReallyFree)
				reallyFreeComputers.add(freeComputer);
		}

		boolean isAssignmentValid = false;
		int usedCPUs = 0;

		logger.info("new assignment...");
		logger.info("free: " + freeComputers.size());
		logger.info("really free: " + reallyFreeComputers.size());
		for (Computer freeComputer : reallyFreeComputers) {

			tempJob.getComputers().add(freeComputer);
			usedCPUs += freeComputer.getCPUs();

			if (usedCPUs >= numberOfCPUs) {
				isAssignmentValid = true;
				break;
			}
		}

		logger.info("assignment valid: " + isAssignmentValid);
		if (!isAssignmentValid)
			throw new InvalidAssignmentException("Not enough free computers.");

		tempJobs.add(tempJob);
		jobsByGrid.put(gridId, tempJobs);
	}

	@Override
	public void clear(long gridId) {
		jobsByGrid.remove(gridId);
	}

	@Override
	public int get(long gridId) {
		if (!jobsByGrid.containsKey(gridId))
			return 0;
		return jobsByGrid.get(gridId).size();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Remove(retainIfException = true)
	public void submit() throws NotLoggedInException,
			InvalidAssignmentException {
		if (user == null)
			throw new NotLoggedInException(
					"Must be logged in to submit temporary jobs.");

		for (Long gridId : jobsByGrid.keySet()) {
			for (TemporaryJob tempJob : jobsByGrid.get(gridId)) {
				if (!isStillValid(tempJob))
					throw new InvalidAssignmentException("Grid " + gridId
							+ " has invalid job assignments.");

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
	}

	private boolean isStillValid(TemporaryJob tempJob) {
		for (Computer freeComputer : tempJob.getComputers()) {
			for (Execution execution : freeComputer.getRunning()) {
				if (execution.getStart() != null && execution.getEnd() == null)
					return false;
			}
		}
		return true;
	}

}
