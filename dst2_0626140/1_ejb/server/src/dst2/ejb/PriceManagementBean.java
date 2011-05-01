package dst2.ejb;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import dst2.model.PriceStep;

@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@AccessTimeout(10000)
public class PriceManagementBean implements PriceManagement {

	@PersistenceContext
	private EntityManager em;
	private SortedSet<PriceStep> steps;

	@PostConstruct
	public void initialize() {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<PriceStep> query = criteriaBuilder
				.createQuery(PriceStep.class);
		Root<PriceStep> priceStep = query.from(PriceStep.class);
		query.select(priceStep);
		List<PriceStep> steps = em.createQuery(query).getResultList();
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
		Iterator<PriceStep> stepsIterator = this.steps.iterator();

		PriceStep currentStep = null;
		while (stepsIterator.hasNext()) {
			currentStep = stepsIterator.next();

			if (numberOfHistoricalJobs > currentStep
					.getNumberOfHistoricalJobs())
				break;
		}

		if (currentStep == null)
			return BigDecimal.ZERO;
		return currentStep.getPrice();
	}

}
