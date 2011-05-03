package dst2.ejb;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import dst2.ejb.dto.AuditDto;
import dst2.ejb.dto.AuditParameterDto;
import dst2.model.Computer;
import dst2.model.Grid;
import dst2.model.Invocation;
import dst2.model.InvocationParameter;
import dst2.model.Job;
import dst2.model.Membership;
import dst2.model.User;

@Stateless
public class GeneralManagementBean implements GeneralManagement {

	private static Logger logger = Logger.getLogger(GeneralManagementBean.class
			.getName());

	@PersistenceContext
	private EntityManager em;

	@EJB
	private PriceManagement priceManagement;

	@Override
	public void addPriceStep(int numberOfHistoricalJobs, BigDecimal price) {
		priceManagement.addStep(numberOfHistoricalJobs, price);
	}

	@SuppressWarnings("unchecked")
	@Asynchronous
	@Override
	public Future<String> getTotalBill(String username) {
		logger.info("getting bill for " + username);
		Query query = em.createNamedQuery("finishedJobsByUser");
		query.setParameter("username", username);
		List<Job> unpaidJobs = query.getResultList();

		StringBuffer bill = new StringBuffer();
		bill.append("\n");
		bill.append("job id\t");
		bill.append("computers\t");
		bill.append("schedule\t");
		bill.append("execution\t");
		bill.append("discount\t");
		bill.append("total");
		bill.append("\n");

		BigDecimal totalCosts = BigDecimal.ZERO;
		logger.info("unpaid job count: " + unpaidJobs.size());
		for (int i = 0; i < unpaidJobs.size(); i++) {
			Job job = unpaidJobs.get(i);
			logger.info("calculating job cost for: " + job.getId());
			em.refresh(job); // WORKAROUND needed since
			// job.getExecutesIn().getRunsOn() is not
			// initialized and left join fetch is not working with toplink
			User user = job.getCreatedBy();
			em.refresh(user);
			int numberOfHistoricalJobs = user.getCreates().size()
					- (unpaidJobs.size() - i);
			logger.info("numberOfHistoricalJobs: " + numberOfHistoricalJobs);
			BigDecimal schedulingCost = priceManagement
					.getPrice(numberOfHistoricalJobs);

			bill.append(job.getId());
			bill.append("\t\t");
			bill.append(job.getExecutesIn().getRunsOn().size());
			bill.append("\t\t");
			bill.append(schedulingCost);
			bill.append("\t\t");

			BigDecimal sumOfCostsForExecution = BigDecimal.ZERO;
			BigDecimal sumOfDiscount = BigDecimal.ZERO;
			for (Computer computer : job.getExecutesIn().getRunsOn()) {
				BigDecimal executionTimeInMinutes = BigDecimal.valueOf(
						job.getExecutesIn().getEnd().getTime()
								- job.getExecutesIn().getStart().getTime())
						.divide(BigDecimal.valueOf(60000), RoundingMode.UP);
				logger.info("executionTimeInMinutes for " + job.getId()
						+ " on " + computer.getId() + ": "
						+ executionTimeInMinutes);
				Grid grid = computer.getControlledBy().getManagedBy();
				BigDecimal costsPerExecution = executionTimeInMinutes
						.multiply(grid.getCostsPerCPUMinute());

				BigDecimal discount = getDiscount(user, grid);
				logger.info("discount for grid " + grid.getId() + ": "
						+ discount);
				sumOfDiscount = sumOfDiscount.add(costsPerExecution
						.multiply(discount));
				sumOfCostsForExecution = sumOfCostsForExecution
						.add(costsPerExecution);
			}
			BigDecimal totalCostsPerJob = schedulingCost
					.add(sumOfCostsForExecution.subtract(sumOfDiscount));
			totalCosts = totalCosts.add(totalCostsPerJob);

			bill.append(sumOfCostsForExecution);
			bill.append("\t\t");
			bill.append(sumOfDiscount);
			bill.append("\t\t");
			bill.append(totalCostsPerJob);
			bill.append("\n");
			job.setIsPaid(true);
		}
		bill.append("\n");
		bill.append("Total:\t\t\t\t\t\t\t\t\t");
		bill.append(totalCosts);
		return new AsyncResult<String>(bill.toString());
	}

	private BigDecimal getDiscount(User user, Grid grid) {
		logger.info("user.getMemberships().size():"
				+ user.getMemberships().size());
		for (Membership membership : user.getMemberships()) {
			logger.info("membership.getGrid().getId(): "
					+ membership.getGrid().getId());
			logger.info("grid.getId(): " + grid.getId());
			logger.info("membership.getGrid().getId() == grid.getId(): "
					+ (membership.getGrid().getId() == grid.getId()));
			if (membership.getGrid().getId() == grid.getId()) {
				logger.info("membership.getDiscount(): "
						+ membership.getDiscount());
				logger.info("BigDecimal.valueOf(membership.getDiscount()): "
						+ BigDecimal.valueOf(membership.getDiscount()));
				return BigDecimal.valueOf(membership.getDiscount());
			}
		}
		return BigDecimal.ZERO;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AuditDto> getAudits() {
		List<AuditDto> auditDtos = new LinkedList<AuditDto>();
		Query query = em.createNamedQuery("allInvocations");
		List<Invocation> invocations = query.getResultList();

		for (Invocation invocation : invocations) {
			AuditDto auditDto = new AuditDto(invocation.getInvocationTime(),
					invocation.getMethodName(), invocation.getResult());
			for (InvocationParameter parameter : invocation.getParameters()) {
				AuditParameterDto auditParameterDto = new AuditParameterDto(
						parameter.getIndex(), parameter.getClassName(),
						parameter.getValue());
				auditDto.parameters.add(auditParameterDto);
			}
			auditDtos.add(auditDto);
		}

		return auditDtos;
	}

}
