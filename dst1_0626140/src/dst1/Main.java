package dst1;

import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.exception.ConstraintViolationException;

import dst1.model.Address;
import dst1.model.Admin;
import dst1.model.Cluster;
import dst1.model.Computer;
import dst1.model.Environment;
import dst1.model.Grid;
import dst1.model.Job;
import dst1.model.Membership;
import dst1.model.User;

public class Main {

	private static final Logger logger = LogManager.getLogger(Main.class);
	private static EntityManagerFactory emf;
	private static EntityManager em;

	private static User max;
	private static User john;
	private static Admin homer;
	private static Computer pc;
	private static Computer macServer;
	private static Computer xeon1;
	private static Computer xeon2;
	private static Computer xeon3;
	private static Computer xeon4;
	private static Cluster clusterSalzburg;
	private static Cluster clusterWien1;
	private static Cluster clusterAustria;
	private static Cluster clusterWien2;
	private static Grid gridWien;
	private static Grid gridAustria;
	private static Membership maxToGridWien;
	private static Membership maxToGridAustria;
	private static Membership johnToGridAustria;
	private static Job job1;

	private Main() {
		super();
	}

	public static void main(String[] args) {
		emf = Persistence.createEntityManagerFactory("grid");
		em = emf.createEntityManager();

		dst01();
		dst02a();
		dst02b();
		dst02c();
		dst03();
		dst04a();
		dst04b();
		dst04c();
		dst04d();
	}

	public static void dst01() {
		createTestdata();
		violateConstraints();
	}

	private static void violateConstraints() {
		em.getTransaction().begin();
		User userWithExistingAccount = new User();
		userWithExistingAccount.setUsername("bs");
		userWithExistingAccount.setPassword("Test1");
		userWithExistingAccount.setFirstname("Bart");
		userWithExistingAccount.setLastname("Simpson");
		userWithExistingAccount.setAccountNo("12345678901");
		userWithExistingAccount.setBankCode("12345");
		userWithExistingAccount.setAddress(homer.getAddress());
		try {
			em.persist(userWithExistingAccount);
			em.flush();
			bailOut("ConstraintViolationException expected.");
		} catch (PersistenceException e) {
			if (e.getCause() instanceof ConstraintViolationException) {
				logger.info(userWithExistingAccount
						+ " cannot be inserted because account already exists");
			} else
				bailOut("ConstraintViolationException expected.");
		}
		em.getTransaction().rollback();

		em.getTransaction().begin();
		Computer pcWithExistingName = new Computer();
		pcWithExistingName.setName("PC");
		pcWithExistingName.setCPUs(2);
		pcWithExistingName.setCreation(Date.valueOf("2005-01-01"));
		pcWithExistingName.setLastUpdate(Date.valueOf("2011-03-15"));
		pcWithExistingName.setLocation("AUT-SBG");
		try {
			em.persist(pcWithExistingName);
			em.flush();
			bailOut("ConstraintViolationException expected.");
		} catch (PersistenceException e) {
			if (e.getCause() instanceof ConstraintViolationException) {
				logger.info(pcWithExistingName
						+ " cannot be inserted because name already exists");
			} else
				bailOut("ConstraintViolationException expected.");
		}
		em.getTransaction().rollback();

		Membership johnToGridAustria2 = new Membership(john, gridAustria);
		johnToGridAustria2.setRegistration(Date.valueOf("2010-10-03"));
		johnToGridAustria2.setDiscount(.1);
		try {
			em.persist(johnToGridAustria2);
			em.flush();
			bailOut("NonUniqueObjectException expected.");
		} catch (PersistenceException e) {
			if (e.getCause() instanceof NonUniqueObjectException) {
				logger.info(johnToGridAustria2
						+ " cannot be inserted because id already exists");
				em.getTransaction().rollback();
			} else
				bailOut("NonUniqueObjectException expected.");
		}
	}

	private static void createTestdata() {
		em.getTransaction().begin();
		max = new User();
		max.setUsername("mm");
		max.setPassword("Test1");
		max.setFirstname("Max");
		max.setLastname("Mustermann");
		max.setAccountNo("12345678901");
		max.setBankCode("12345");
		Address addressOfMax = new Address();
		addressOfMax.setCity("Wien");
		addressOfMax.setStreet("Stephansplatz");
		addressOfMax.setZipCode("1010");
		max.setAddress(addressOfMax);
		em.persist(max);
		logger.info("Created " + max);

		john = new User();
		john.setUsername("jd");
		john.setPassword("Test1");
		john.setFirstname("John");
		john.setLastname("Doe");
		john.setAccountNo("12345678901");
		john.setBankCode("23456");
		Address addressOfJohn = new Address();
		addressOfJohn.setCity("Salzburg");
		addressOfJohn.setStreet("Getreidegasse");
		addressOfJohn.setZipCode("5020");
		john.setAddress(addressOfJohn);
		em.persist(john);
		logger.info("Created " + john);
		logger.info("Updated address of " + max);

		homer = new Admin();
		homer.setFirstname("Homer");
		homer.setLastname("Simpson");
		Address addressOfHomer = new Address();
		addressOfHomer.setCity("Springfield");
		addressOfHomer.setStreet("Evergreen Terrace");
		addressOfHomer.setZipCode("12345");
		homer.setAddress(addressOfHomer);
		em.persist(homer);
		logger.info("Created " + homer);

		gridWien = new Grid();
		gridWien.setName("Wien");
		gridWien.setLocation("AUT-VIE");
		gridWien.setCostsPerCPUMinute(BigDecimal.valueOf(3.2));
		em.persist(gridWien);
		logger.info("Created " + gridWien);

		gridAustria = new Grid();
		gridAustria.setName("Austria");
		gridAustria.setLocation("AUT");
		gridAustria.setCostsPerCPUMinute(BigDecimal.valueOf(6.4));
		em.persist(gridAustria);
		logger.info("Created " + gridAustria);

		// clusterSalzburg = new Cluster();
		// clusterSalzburg.setName("Salzburg 1");
		// clusterSalzburg.setMaintainedBy(homer);
		// clusterSalzburg.setLastService(Date.valueOf("2010-12-01"));
		// clusterSalzburg.setNextService(Date.valueOf("2011-04-01"));
		// em.persist(clusterSalzburg);
		// logger.info("Created " + clusterSalzburg);

		clusterWien1 = new Cluster();
		clusterWien1.setName("Wien 1");
		clusterWien1.setMaintainedBy(homer);
		clusterWien1.setManagedBy(gridWien);
		clusterWien1.setLastService(Date.valueOf("2010-12-01"));
		clusterWien1.setNextService(Date.valueOf("2011-04-01"));
		clusterWien1.getControls().add(xeon1);
		clusterWien1.getControls().add(xeon2);
		em.persist(clusterWien1);
		logger.info("Created " + clusterWien1);

		clusterWien2 = new Cluster();
		clusterWien2.setName("Wien 2");
		clusterWien2.setMaintainedBy(homer);
		clusterWien2.setManagedBy(gridWien);
		clusterWien2.setLastService(Date.valueOf("2010-12-01"));
		clusterWien2.setNextService(Date.valueOf("2011-04-01"));
		clusterWien2.getControls().add(xeon3);
		em.persist(clusterWien2);
		logger.info("Created " + clusterWien2);

		clusterAustria = new Cluster();
		clusterAustria.setName("Austria 1");
		clusterAustria.setMaintainedBy(homer);
		clusterAustria.setManagedBy(gridAustria);
		clusterAustria.setLastService(Date.valueOf("2010-12-01"));
		clusterAustria.setNextService(Date.valueOf("2011-04-01"));
		clusterAustria.getConsistsOf().add(clusterWien1);
		clusterAustria.getConsistsOf().add(clusterSalzburg);
		em.persist(clusterAustria);
		logger.info("Created " + clusterAustria);

		pc = new Computer();
		pc.setName("PC");
		pc.setCPUs(2);
		pc.setCreation(Date.valueOf("2005-01-01"));
		pc.setLastUpdate(Date.valueOf("2011-03-15"));
		pc.setLocation("AUT-SBG");
		pc.setControlledBy(clusterAustria);
		em.persist(pc);
		logger.info("Created " + pc);

		macServer = new Computer();
		macServer.setName("Mac OS X Server");
		macServer.setCPUs(8);
		macServer.setCreation(Date.valueOf("2010-01-01"));
		macServer.setLastUpdate(Date.valueOf("2011-03-20"));
		macServer.setLocation("AUT-SBG");
		macServer.setControlledBy(clusterAustria);
		em.persist(macServer);
		logger.info("Created " + macServer);

		xeon1 = new Computer();
		xeon1.setName("Intel Xeon VM 1");
		xeon1.setCPUs(4);
		xeon1.setCreation(Date.valueOf("2011-01-01"));
		xeon1.setLastUpdate(Date.valueOf("2011-03-25"));
		xeon1.setLocation("AUT-VIE");
		xeon1.setControlledBy(clusterWien1);
		em.persist(xeon1);
		logger.info("Created " + xeon1);

		xeon2 = new Computer();
		xeon2.setName("Intel Xeon VM 2");
		xeon2.setCPUs(4);
		xeon2.setCreation(Date.valueOf("2011-01-01"));
		xeon2.setLastUpdate(Date.valueOf("2011-03-25"));
		xeon2.setLocation("AUT-VIE");
		xeon2.setControlledBy(clusterWien1);
		em.persist(xeon2);
		logger.info("Created " + xeon2);

		xeon3 = new Computer();
		xeon3.setName("Intel Xeon VM 3");
		xeon3.setCPUs(4);
		xeon3.setCreation(Date.valueOf("2011-01-01"));
		xeon3.setLastUpdate(Date.valueOf("2011-03-25"));
		xeon3.setLocation("AUT-VIE");
		xeon3.setControlledBy(clusterWien2);
		em.persist(xeon3);
		logger.info("Created " + xeon3);

		xeon4 = new Computer();
		xeon4.setName("Intel Xeon VM 4");
		xeon4.setCPUs(4);
		xeon4.setCreation(Date.valueOf("2011-01-01"));
		xeon4.setLastUpdate(Date.valueOf("2011-03-25"));
		xeon4.setLocation("AUT-VIE");
		xeon4.setControlledBy(clusterWien2);
		em.persist(xeon3);
		logger.info("Created " + xeon3);

		maxToGridWien = new Membership(max, gridWien);
		maxToGridWien.setRegistration(Date.valueOf("2008-05-22"));
		maxToGridWien.setDiscount(.1);
		em.persist(maxToGridWien);
		logger.info("Created " + maxToGridWien);

		maxToGridAustria = new Membership(max, gridAustria);
		maxToGridAustria.setRegistration(Date.valueOf("2007-03-15"));
		maxToGridAustria.setDiscount(0d);
		em.persist(maxToGridAustria);
		logger.info("Created " + maxToGridAustria);

		johnToGridAustria = new Membership(john, gridAustria);
		johnToGridAustria.setRegistration(Date.valueOf("2009-10-03"));
		johnToGridAustria.setDiscount(.25);
		em.persist(johnToGridAustria);
		logger.info("Created " + johnToGridAustria);

		job1 = new Job();
		job1.setCreatedBy(max);
		job1.setIsPaid(false);
		Environment env = new Environment();
		env.setWorkflow("Worfklow1");
		env.getParams().add("true");
		env.getParams().add("find");
		env.getParams().add("test");
		job1.setNeeds(env);
		em.getTransaction().commit();
	}

	private static void bailOut(String message) {
		logger.error(message);
		em.close();
		emf.close();
		System.exit(1);
	}

	public static void dst02a() {

	}

	public static void dst02b() {

	}

	public static void dst02c() {

	}

	public static void dst03() {

	}

	public static void dst04a() {

	}

	public static void dst04b() {

	}

	public static void dst04c() {

	}

	public static void dst04d() {

	}
}
