package dst2.ejb;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.AccessTimeout;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import dst2.model.PriceStep;

@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@AccessTimeout(10000)
public class PriceManagementBean implements PriceManagement {

	private static Logger logger = Logger.getLogger(PriceManagementBean.class
			.getName());

	@PersistenceContext
	private EntityManager em;
	private SortedSet<PriceStep> steps;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void initialize() {
		List<PriceStep> steps = em.createNamedQuery("allPriceSteps")
				.getResultList();
		this.steps = new TreeSet<PriceStep>(steps);
	}

	@Lock(LockType.WRITE)
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public void addStep(int numberOfHistoricalJobs, BigDecimal price) {
		PriceStep step = new PriceStep();
		step.setNumberOfHistoricalJobs(numberOfHistoricalJobs);
		step.setPrice(price);
		em.persist(step);
		this.steps.add(step);
	}

	@Lock(LockType.READ)
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	@Override
	public BigDecimal getPrice(int numberOfHistoricalJobs) {
		logger.info("getting schedule costs for # of jobs: "
				+ numberOfHistoricalJobs);
		Iterator<PriceStep> stepsIterator = this.steps.iterator();

		PriceStep lastStep = null;
		PriceStep currentStep = null;
		while (stepsIterator.hasNext()) {
			lastStep = currentStep;
			currentStep = stepsIterator.next();
			logger.info("currentStep # of jobs: "
					+ currentStep.getNumberOfHistoricalJobs());

			if (currentStep.getNumberOfHistoricalJobs() > numberOfHistoricalJobs) {
				break;
			}

		}
		
		BigDecimal price;
		if (lastStep == null)
			return BigDecimal.ZERO;
		else
			price = lastStep.getPrice();
		logger.info("price for # of jobs: "
				+ price);
		return price;
	}

}
