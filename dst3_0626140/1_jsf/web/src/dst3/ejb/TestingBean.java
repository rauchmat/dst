package dst3.ejb;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import dst3.model.Address;
import dst3.model.Admin;
import dst3.model.Cluster;
import dst3.model.Computer;
import dst3.model.Environment;
import dst3.model.Execution;
import dst3.model.Grid;
import dst3.model.Job;
import dst3.model.Membership;
import dst3.model.User;

@Stateless
public class TestingBean implements Testing {

	private static final Logger logger = Logger.getLogger(TestingBean.class
			.getName());

	@PersistenceContext
	private EntityManager em;

	public TestingBean() {
	}

	public void addTestdata() {
		try {
			User user = new User();
			user.setUsername("mm");
			user.setPassword("secret");
			user.setFirstname("Max");
			user.setLastname("Mustermann");
			user.setAccountNo("12345678901");
			user.setBankCode("55555");
			user.setAddress(new Address());
			user.getAddress().setStreet("Stephansplatz 1");
			user.getAddress().setZipCode("1010");
			user.getAddress().setCity("Wien");
			em.persist(user);
			logger.info("Created " + user);

			Admin homer = new Admin();
			homer.setFirstname("Homer");
			homer.setLastname("Simpson");
			Address addressOfHomer = new Address();
			addressOfHomer.setCity("Springfield");
			addressOfHomer.setStreet("Evergreen Terrace");
			addressOfHomer.setZipCode("12345");
			homer.setAddress(addressOfHomer);
			em.persist(homer);
			logger.info("Created " + homer);

			Grid gridWien = new Grid();
			gridWien.setName("Wien");
			gridWien.setLocation("AUT-VIE");
			gridWien.setCostsPerCPUMinute(BigDecimal.valueOf(3.2));
			em.persist(gridWien);
			logger.info("Created " + gridWien);

			Grid gridAustria = new Grid();
			gridAustria.setName("Salzburg");
			gridAustria.setLocation("AUT-SBG");
			gridAustria.setCostsPerCPUMinute(BigDecimal.valueOf(6.4));
			em.persist(gridAustria);
			logger.info("Created " + gridAustria);

			Cluster clusterWien1 = new Cluster();
			clusterWien1.setName("Wien 1");
			clusterWien1.setMaintainedBy(homer);
			homer.getMaintains().add(clusterWien1);
			clusterWien1.setManagedBy(gridWien);
			gridWien.getManages().add(clusterWien1);
			clusterWien1.setLastService(Date.valueOf("2010-12-01"));
			clusterWien1.setNextService(Date.valueOf("2011-04-01"));
			em.persist(clusterWien1);
			logger.info("Created " + clusterWien1);

			Cluster clusterWien2 = new Cluster();
			clusterWien2.setName("Wien 2");
			clusterWien2.setMaintainedBy(homer);
			homer.getMaintains().add(clusterWien2);
			clusterWien2.setManagedBy(gridWien);
			gridWien.getManages().add(clusterWien2);
			clusterWien2.setLastService(Date.valueOf("2010-12-01"));
			clusterWien2.setNextService(Date.valueOf("2011-04-01"));
			clusterWien1.getConsistsOf().add(clusterWien2);
			clusterWien2.getPartOf().add(clusterWien2);
			em.persist(clusterWien2);
			logger.info("Created " + clusterWien2);

			Cluster clusterSalzburg = new Cluster();
			clusterSalzburg.setName("Salzburg 1");
			clusterSalzburg.setMaintainedBy(homer);
			homer.getMaintains().add(clusterSalzburg);
			clusterSalzburg.setManagedBy(gridAustria);
			gridAustria.getManages().add(clusterSalzburg);
			clusterSalzburg.setLastService(Date.valueOf("2010-12-01"));
			clusterSalzburg.setNextService(Date.valueOf("2011-04-01"));
			clusterSalzburg.getConsistsOf().add(clusterWien1);
			clusterWien1.getPartOf().add(clusterSalzburg);
			// clusterAustria.getConsistsOf().add(clusterSalzburg);
			// clusterSalzburg.getPartOf().add(clusterAustria);
			em.persist(clusterSalzburg);
			logger.info("Created " + clusterSalzburg);

			Computer dualCoreSbg1 = new Computer();
			dualCoreSbg1.setName("Dual Core SBG 1");
			dualCoreSbg1.setCPUs(2);
			dualCoreSbg1.setCreation(Date.valueOf("2005-01-01"));
			dualCoreSbg1.setLastUpdate(Date.valueOf("2011-03-15"));
			dualCoreSbg1.setLocation("AUT-SBG@5020");
			dualCoreSbg1.setControlledBy(clusterSalzburg);
			clusterSalzburg.getControls().add(dualCoreSbg1);
			em.persist(dualCoreSbg1);
			logger.info("Created " + dualCoreSbg1);

			Computer dualCoreSbg2 = new Computer();
			dualCoreSbg2.setName("Dual Core SBG 2");
			dualCoreSbg2.setCPUs(2);
			dualCoreSbg2.setCreation(Date.valueOf("2010-01-01"));
			dualCoreSbg2.setLastUpdate(Date.valueOf("2011-03-20"));
			dualCoreSbg2.setLocation("AUT-SBG@5020");
			dualCoreSbg2.setControlledBy(clusterSalzburg);
			clusterSalzburg.getControls().add(dualCoreSbg2);
			em.persist(dualCoreSbg2);
			logger.info("Created " + dualCoreSbg2);

			Computer xeon1 = new Computer();
			xeon1.setName("Intel Xeon VM 1");
			xeon1.setCPUs(4);
			xeon1.setCreation(Date.valueOf("2011-01-01"));
			xeon1.setLastUpdate(Date.valueOf("2011-03-25"));
			xeon1.setLocation("AUT-VIE@1040");
			xeon1.setControlledBy(clusterWien1);
			clusterWien1.getControls().add(xeon1);
			em.persist(xeon1);
			logger.info("Created " + xeon1);

			Computer xeon2 = new Computer();
			xeon2.setName("Intel Xeon VM 2");
			xeon2.setCPUs(4);
			xeon2.setCreation(Date.valueOf("2011-01-01"));
			xeon2.setLastUpdate(Date.valueOf("2011-03-25"));
			xeon2.setLocation("AUT-VIE@1040");
			xeon2.setControlledBy(clusterWien1);
			clusterWien1.getControls().add(xeon2);
			em.persist(xeon2);
			logger.info("Created " + xeon2);

			Computer xeon3 = new Computer();
			xeon3.setName("Intel Xeon VM 3");
			xeon3.setCPUs(4);
			xeon3.setCreation(Date.valueOf("2011-01-01"));
			xeon3.setLastUpdate(Date.valueOf("2011-03-25"));
			xeon3.setLocation("AUT-VIE@1050");
			xeon3.setControlledBy(clusterWien2);
			clusterWien2.getControls().add(xeon3);
			em.persist(xeon3);
			logger.info("Created " + xeon3);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void deleteTestdata() {
		removeAll(Environment.class);
		removeAll(Execution.class);
		removeAll(Job.class);
		removeAll(Membership.class);
		removeAll(User.class);
		removeAll(Admin.class);
		removeAll(Computer.class);
		removeAll(Cluster.class);
		removeAll(Grid.class);

	}

	private void removeAll(Class<?> clazz) {
		Query query = em.createQuery("DELETE FROM " + clazz.getSimpleName());
		query.executeUpdate();
	}

	@Override
	public boolean hasTestdata() {
		return ((Number) em.createNamedQuery("hasGrids").getSingleResult())
				.intValue() > 0;
	}

}
