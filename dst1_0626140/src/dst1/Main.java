package dst1;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;

import dst1.interceptor.SQLInterceptor;
import dst1.listener.DefaultListener;
import dst1.model.Address;
import dst1.model.Admin;
import dst1.model.Cluster;
import dst1.model.Computer;
import dst1.model.Environment;
import dst1.model.Execution;
import dst1.model.Grid;
import dst1.model.Job;
import dst1.model.JobStatus;
import dst1.model.Membership;
import dst1.model.User;
import dst1.query.FinishedJobsByStartAndEnd;
import dst1.query.JobsByUserAndWorkflow;

public class Main {

	private static final Logger logger = LogManager.getLogger(Main.class);
	private static final DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss");
	private static final NumberFormat bigNumberFormat = new DecimalFormat(
			"###,###");
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
	// private static Cluster clusterSalzburg;
	private static Cluster clusterWien1;
	private static Cluster clusterAustria;
	private static Cluster clusterWien2;
	private static Grid gridWien;
	private static Grid gridAustria;
	private static Membership maxToGridWien;
	private static Membership maxToGridAustria;
	private static Membership johnToGridAustria;
	private static Job maxJob1;
	private static Job maxJob2;
	private static Job maxJob3;
	private static User alice;
	private static Membership aliceToGridWien;
	private static Job aliceJob1;
	private static Job johnJob1;
	private static Job johnJob2;
	private static Job johnJob3;
	private static Job johnJob4;
	private static Job johnJob5;
	private static User ferdl;
	private static Membership ferdlToGridWien;

	private Main() {
		super();
	}

	public static void main(String[] args) throws ParseException {
		emf = Persistence.createEntityManagerFactory("grid");
		em = emf.createEntityManager();

		logger.info("STARTING dst01()");
		dst01();
		logger.info("STARTING dst02a()");
		dst02a();
		logger.info("STARTING dst02b()");
		dst02b();
		logger.info("STARTING dst02c()");
		dst02c();
		logger.info("STARTING dst03()");
		dst03();
		logger.info("STARTING dst04a()");
		dst04a();
		logger.info("STARTING dst04b()");
		dst04b();
		logger.info("STARTING dst04c()");
		dst04c();
		logger.info("STARTING dst04d()");
		dst04d();
		em.close();
		emf.close();
	}

	public static void dst01() throws ParseException {
		createTestdata();
		updateSomeTestdata();
		removeSomeTestdata();
		violateConstraints();
	}

	private static void updateSomeTestdata() {
		em.getTransaction().begin();

		pc = em.find(Computer.class, pc.getId());
		pc.setName("Windows Server 2008 1");
		em.flush();
		logger.info("Updated name of " + pc);

		alice = em.find(User.class, alice.getId());
		alice.setLastname("Dorferer");
		em.flush();
		logger.info("Updated lastname of " + pc);

		maxJob3 = em.find(Job.class, maxJob3.getId());
		maxJob3.setIsPaid(true);
		em.flush();
		logger.info("Updated isPaid of " + maxJob3);
		em.getTransaction().commit();
	}

	private static void removeSomeTestdata() {
		em.getTransaction().begin();
		em.remove(alice);
		logger.info("Removing " + alice);
		em.flush();
		em.getTransaction().commit();

		em.getTransaction().begin();
		Job job = em.find(Job.class, aliceJob1.getId());
		if (job != null)
			logger.error("Remove of " + aliceJob1 + "did not work!");

		User user = em.find(User.class, alice.getId());
		if (user != null)
			logger.error("Cascade delete of user -> creates did not work!");

		Membership membership = em.find(Membership.class, aliceToGridWien
				.getId());
		if (membership != null)
			logger.error("Cascade delete of user -> memberships did not work!");
		em.getTransaction().rollback();
	}

	private static void createTestdata() throws ParseException {
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

		ferdl = new User();
		ferdl.setUsername("fp");
		ferdl.setPassword("Cayenne01");
		ferdl.setFirstname("Ferdinand");
		ferdl.setLastname("Porsche");
		ferdl.setAccountNo("99999999999");
		ferdl.setBankCode("99999");
		Address addressOfFerdl = new Address();
		addressOfFerdl.setCity("Wien");
		addressOfFerdl.setStreet("Schlachthausgasse");
		addressOfFerdl.setZipCode("1030");
		ferdl.setAddress(addressOfFerdl);
		em.persist(ferdl);
		logger.info("Created " + ferdl);

		alice = new User();
		alice.setUsername("ad");
		alice.setPassword("s3cr3t");
		alice.setFirstname("Alice");
		alice.setLastname("Dorfer");
		alice.setAccountNo("12345678902");
		alice.setBankCode("23456");
		Address addressOfAlice = new Address();
		addressOfAlice.setCity("Wien");
		addressOfAlice.setStreet("Favoritenstraße");
		addressOfAlice.setZipCode("1040");
		alice.setAddress(addressOfJohn);
		em.persist(alice);
		logger.info("Created " + alice);

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
		homer.getMaintains().add(clusterWien1);
		clusterWien1.setManagedBy(gridWien);
		gridWien.getManages().add(clusterWien1);
		clusterWien1.setLastService(Date.valueOf("2010-12-01"));
		clusterWien1.setNextService(Date.valueOf("2011-04-01"));
		em.persist(clusterWien1);
		logger.info("Created " + clusterWien1);

		clusterWien2 = new Cluster();
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

		clusterAustria = new Cluster();
		clusterAustria.setName("Austria 1");
		clusterAustria.setMaintainedBy(homer);
		homer.getMaintains().add(clusterAustria);
		clusterAustria.setManagedBy(gridAustria);
		gridAustria.getManages().add(clusterAustria);
		clusterAustria.setLastService(Date.valueOf("2010-12-01"));
		clusterAustria.setNextService(Date.valueOf("2011-04-01"));
		clusterAustria.getConsistsOf().add(clusterWien1);
		clusterWien1.getPartOf().add(clusterAustria);
		// clusterAustria.getConsistsOf().add(clusterSalzburg);
		// clusterSalzburg.getPartOf().add(clusterAustria);
		em.persist(clusterAustria);
		logger.info("Created " + clusterAustria);

		pc = new Computer();
		pc.setName("PC Windows 1");
		pc.setCPUs(2);
		pc.setCreation(Date.valueOf("2005-01-01"));
		pc.setLastUpdate(Date.valueOf("2011-03-15"));
		pc.setLocation("AUT-SBG@5020");
		pc.setControlledBy(clusterAustria);
		clusterAustria.getControls().add(pc);
		em.persist(pc);
		logger.info("Created " + pc);

		macServer = new Computer();
		macServer.setName("Mac OS X Server");
		macServer.setCPUs(8);
		macServer.setCreation(Date.valueOf("2010-01-01"));
		macServer.setLastUpdate(Date.valueOf("2011-03-20"));
		macServer.setLocation("AUT-SBG@5020");
		macServer.setControlledBy(clusterAustria);
		clusterAustria.getControls().add(macServer);
		em.persist(macServer);
		logger.info("Created " + macServer);

		xeon1 = new Computer();
		xeon1.setName("Intel Xeon VM 1");
		xeon1.setCPUs(4);
		xeon1.setCreation(Date.valueOf("2011-01-01"));
		xeon1.setLastUpdate(Date.valueOf("2011-03-25"));
		xeon1.setLocation("AUT-VIE@1040");
		xeon1.setControlledBy(clusterWien1);
		clusterWien1.getControls().add(xeon1);
		em.persist(xeon1);
		logger.info("Created " + xeon1);

		xeon2 = new Computer();
		xeon2.setName("Intel Xeon VM 2");
		xeon2.setCPUs(4);
		xeon2.setCreation(Date.valueOf("2011-01-01"));
		xeon2.setLastUpdate(Date.valueOf("2011-03-25"));
		xeon2.setLocation("AUT-VIE@1040");
		xeon2.setControlledBy(clusterWien1);
		clusterWien1.getControls().add(xeon2);
		em.persist(xeon2);
		logger.info("Created " + xeon2);

		xeon3 = new Computer();
		xeon3.setName("Intel Xeon VM 3");
		xeon3.setCPUs(4);
		xeon3.setCreation(Date.valueOf("2011-01-01"));
		xeon3.setLastUpdate(Date.valueOf("2011-03-25"));
		xeon3.setLocation("AUT-VIE@1050");
		xeon3.setControlledBy(clusterWien2);
		clusterWien2.getControls().add(xeon3);
		em.persist(xeon3);
		logger.info("Created " + xeon3);

		xeon4 = new Computer();
		xeon4.setName("Intel Xeon VM 4");
		xeon4.setCPUs(4);
		xeon4.setCreation(Date.valueOf("2011-01-01"));
		xeon4.setLastUpdate(Date.valueOf("2011-03-25"));
		xeon4.setLocation("AUT-VIE@1050");
		xeon4.setControlledBy(clusterWien2);
		clusterWien2.getControls().add(xeon4);
		em.persist(xeon4);
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

		ferdlToGridWien = new Membership(ferdl, gridWien);
		ferdlToGridWien.setRegistration(Date.valueOf("2009-02-25"));
		ferdlToGridWien.setDiscount(0d);
		em.persist(ferdlToGridWien);
		logger.info("Created " + ferdlToGridWien);

		aliceToGridWien = new Membership(alice, gridWien);
		aliceToGridWien.setRegistration(Date.valueOf("2010-07-13"));
		aliceToGridWien.setDiscount(0d);
		em.persist(aliceToGridWien);
		logger.info("Created " + aliceToGridWien);

		maxJob1 = new Job();
		maxJob1.setCreatedBy(max);
		maxJob1.setIsPaid(false);
		Environment env1 = new Environment();
		env1.setWorkflow("Workflow1");
		env1.getParams().add("true");
		env1.getParams().add("find");
		env1.getParams().add("test");
		Execution exec1 = new Execution();
		exec1.setStart(dateFormat.parse("2011-04-12 10:21:00"));
		exec1.setEnd(dateFormat.parse("2011-04-12 10:24:01"));
		exec1.setStatus(JobStatus.FINISHED);
		exec1.setExecutes(maxJob1);
		exec1.getRunsOn().add(xeon1);
		xeon1.getRunning().add(exec1);
		exec1.getRunsOn().add(xeon2);
		xeon2.getRunning().add(exec1);
		maxJob1.setExecutesIn(exec1);
		maxJob1.setNeeds(env1);
		em.persist(maxJob1);
		logger.info("Created " + maxJob1);

		maxJob2 = new Job();
		maxJob2.setCreatedBy(max);
		maxJob2.setIsPaid(false);
		Environment env2 = new Environment();
		env2.setWorkflow("Workflow2");
		env2.getParams().add("false");
		Execution exec2 = new Execution();
		exec2.setStart(dateFormat.parse("2010-02-10 08:34:15"));
		exec2.setEnd(dateFormat.parse("2010-02-10 11:42:18"));
		exec2.setStatus(JobStatus.FINISHED);
		exec2.setExecutes(maxJob2);
		maxJob2.setExecutesIn(exec2);
		maxJob2.setNeeds(env2);
		em.persist(maxJob2);
		logger.info("Created " + maxJob2);

		maxJob3 = new Job();
		maxJob3.setCreatedBy(max);
		maxJob3.setIsPaid(false);
		Environment env3 = new Environment();
		env3.setWorkflow("Workflow3");
		maxJob3.setNeeds(env3);
		Execution exec3 = new Execution();
		exec3.setStart(dateFormat.parse("2011-01-14 11:01:32"));
		exec3.setEnd(dateFormat.parse("2011-01-14 11:01:35"));
		exec3.setStatus(JobStatus.FAILED);
		exec2.getRunsOn().add(xeon3);
		xeon3.getRunning().add(exec2);
		exec3.setExecutes(maxJob3);
		maxJob3.setExecutesIn(exec3);
		em.persist(maxJob3);
		logger.info("Created " + maxJob3);

		aliceJob1 = new Job();
		aliceJob1.setCreatedBy(alice);
		alice.getCreates().add(aliceJob1);
		aliceJob1.setIsPaid(false);
		Environment env4 = new Environment();
		env4.setWorkflow("Workflow1");
		aliceJob1.setNeeds(env4);
		Execution exec4 = new Execution();
		exec4.setStatus(JobStatus.SCHEDULED);
		exec4.setExecutes(aliceJob1);
		aliceJob1.setExecutesIn(exec4);
		em.persist(aliceJob1);
		logger.info("Created " + aliceJob1);

		johnJob1 = new Job();
		johnJob1.setCreatedBy(john);
		johnJob1.setIsPaid(false);
		Environment env5 = new Environment();
		env5.setWorkflow("Workflow1");
		johnJob1.setNeeds(env5);
		Execution exec5 = new Execution();
		exec5.setStatus(JobStatus.SCHEDULED);
		exec4.setExecutes(johnJob1);
		johnJob1.setExecutesIn(exec5);
		em.persist(johnJob1);
		logger.info("Created " + johnJob1);

		johnJob2 = new Job();
		johnJob2.setCreatedBy(john);
		johnJob2.setIsPaid(false);
		johnJob2.setNeeds(new Environment());
		johnJob2.getNeeds().setWorkflow("Workflow1");
		johnJob2.setExecutesIn(new Execution());
		johnJob2.getExecutesIn().setStatus(JobStatus.FINISHED);
		johnJob2.getExecutesIn().setStart(
				dateFormat.parse("2011-02-13 15:23:35"));
		johnJob2.getExecutesIn()
				.setEnd(dateFormat.parse("2011-02-13 15:42:58"));
		johnJob2.getExecutesIn().getRunsOn().add(xeon3);
		johnJob2.getExecutesIn().getRunsOn().add(xeon4);
		em.persist(johnJob2);
		logger.info("Created " + johnJob2);

		johnJob3 = new Job();
		johnJob3.setCreatedBy(john);
		johnJob3.setIsPaid(false);
		johnJob3.setNeeds(new Environment());
		johnJob3.getNeeds().setWorkflow("Workflow4");
		johnJob3.getNeeds().setParams(
				Arrays.asList(new String[] { "Produce", "100" }));
		johnJob3.setExecutesIn(new Execution());
		johnJob3.getExecutesIn().setStatus(JobStatus.FINISHED);
		johnJob3.getExecutesIn().setStart(
				dateFormat.parse("2010-11-30 21:45:35"));
		johnJob3.getExecutesIn()
				.setEnd(dateFormat.parse("2011-11-30 21:46:58"));
		johnJob3.getExecutesIn().getRunsOn().add(pc);
		johnJob3.getExecutesIn().getRunsOn().add(macServer);
		em.persist(johnJob3);
		logger.info("Created " + johnJob3);

		johnJob4 = new Job();
		johnJob4.setCreatedBy(john);
		johnJob4.setIsPaid(false);
		johnJob4.setNeeds(new Environment());
		johnJob4.getNeeds().setWorkflow("Workflow4");
		johnJob4.getNeeds().setParams(
				Arrays.asList(new String[] { "Consume", "*" }));
		johnJob4.setExecutesIn(new Execution());
		johnJob4.getExecutesIn().setStatus(JobStatus.FINISHED);
		johnJob4.getExecutesIn().setStart(
				dateFormat.parse("2010-10-14 18:22:12"));
		johnJob4.getExecutesIn()
				.setEnd(dateFormat.parse("2011-10-14 19:32:44"));
		johnJob4.getExecutesIn().getRunsOn().add(pc);
		johnJob4.getExecutesIn().getRunsOn().add(macServer);
		em.persist(johnJob4);
		logger.info("Created " + johnJob4);

		johnJob5 = new Job();
		johnJob5.setCreatedBy(john);
		johnJob5.setIsPaid(false);
		johnJob5.setNeeds(new Environment());
		johnJob5.getNeeds().setWorkflow("Workflow4");
		johnJob5.getNeeds().setParams(
				Arrays.asList(new String[] { "Consume", "*" }));
		johnJob5.setExecutesIn(new Execution());
		johnJob5.getExecutesIn().setStatus(JobStatus.FAILED);
		johnJob5.getExecutesIn().setStart(
				dateFormat.parse("2011-02-15 19:19:33"));
		johnJob5.getExecutesIn()
				.setEnd(dateFormat.parse("2011-02-15 19:25:02"));
		johnJob5.getExecutesIn().getRunsOn().add(pc);
		johnJob5.getExecutesIn().getRunsOn().add(macServer);
		em.persist(johnJob5);
		logger.info("Created " + johnJob5);

		em.getTransaction().commit();
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
		pcWithExistingName.setName("Windows Server 2008 1");
		pcWithExistingName.setCPUs(2);
		pcWithExistingName.setCreation(Date.valueOf("2005-01-01"));
		pcWithExistingName.setLastUpdate(Date.valueOf("2011-03-15"));
		pcWithExistingName.setLocation("AUT-SBG@5020");
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

		em.getTransaction().begin();
		Membership johnToGridAustria2 = new Membership(john, gridAustria);
		johnToGridAustria2.setRegistration(Date.valueOf("2010-10-03"));
		johnToGridAustria2.setDiscount(.1);
		try {
			em.persist(johnToGridAustria2);
			em.flush();
			bailOut("ConstraintViolationException expected.");
		} catch (PersistenceException e) {
			if (e.getCause() instanceof ConstraintViolationException) {
				logger.info(johnToGridAustria2
						+ " cannot be inserted because id already exists");
				em.getTransaction().rollback();
			} else
				bailOut("ConstraintViolationException expected.");
		}
	}

	private static void bailOut(String message) {
		logger.error(message);
		em.close();
		emf.close();
		System.exit(1);
	}

	@SuppressWarnings("unchecked")
	public static void dst02a() {
		Query query = em.createNamedQuery("usersByGridAndJobCount");
		query.setParameter("name", "Austria");
		query.setParameter("jobs", 0L);
		List<User> result = (List<User>) query.getResultList();
		logger
				.info("Users with membership in grid 'Austria' and at least 0 jobs:");
		printUserlist(result);

		query = em.createNamedQuery("usersByGridAndJobCount");
		query.setParameter("name", "Austria");
		query.setParameter("jobs", 4L);
		result = (List<User>) query.getResultList();
		logger
				.info("Users with membership in grid 'Austria' and at least 4 jobs:");
		printUserlist(result);

		query = em.createNamedQuery("usersByGridAndJobCount");
		query.setParameter("name", "Austria");
		query.setParameter("jobs", 10L);
		result = (List<User>) query.getResultList();
		logger
				.info("Users with membership in grid 'Austria' and at least 10 jobs:");
		printUserlist(result);

		query = em.createNamedQuery("usersByGridAndJobCount");
		query.setParameter("name", "Wien");
		query.setParameter("jobs", 1L);
		result = (List<User>) query.getResultList();
		logger
				.info("Users with membership in grid 'Wien' and at least 1 jobs:");
		printUserlist(result);

		query = em.createNamedQuery("mostActiveUser");
		result = (List<User>) query.getResultList();
		logger.info("The most active users are:");
		printUserlist(result);
	}

	private static void printUserlist(List<User> result) {
		for (User user : result) {
			logger.info(" * " + user.getUsername());
		}
	}

	@SuppressWarnings("unchecked")
	public static void dst02b() {
		Query query = em.createNamedQuery("allComputersInVienna");
		List<Computer> result = (List<Computer>) query.getResultList();
		for (Computer computer : result) {
			long executionSum = 0;
			for (Execution execution : computer.getRunning()) {
				if (execution.getStart() != null && execution.getEnd() != null) {
					executionSum += (execution.getEnd().getTime() - execution
							.getStart().getTime());
				}
				logger.info("Execution sum for computer '" + computer.getName()
						+ "': " + bigNumberFormat.format(executionSum) + " ms.");
			}
		}
	}

	public static void dst02c() {
		JobsByUserAndWorkflow jobsByUserAndWorkflow = new JobsByUserAndWorkflow(
				em);
		logger.info("Find jobs (no criteria):");
		printJobs(jobsByUserAndWorkflow.execute(null, null));
		logger.info("Find jobs by user 'mm' and workflow 'Workflow1':");
		printJobs(jobsByUserAndWorkflow.execute("mm", "Workflow1"));
		logger.info("Find jobs by user 'mm':");
		printJobs(jobsByUserAndWorkflow.execute("mm", null));
		logger.info("Find jobs by user 'jd' :");
		printJobs(jobsByUserAndWorkflow.execute("jd", null));
		logger.info("Find jobs by workflow 'Workflow1' :");
		printJobs(jobsByUserAndWorkflow.execute(null, "Workflow1"));
		logger.info("Find jobs by user 'jd' and 'Workflow5' :");
		printJobs(jobsByUserAndWorkflow.execute("jd", "Workflow5"));

		FinishedJobsByStartAndEnd finishedJobsByStartAndEnd = new FinishedJobsByStartAndEnd(
				em);
		logger.info("Find finished jobs (no criteria):");
		printJobs(finishedJobsByStartAndEnd.execute(null, null));
		logger
				.info("Find finished jobs by start date '"
						+ dateFormat
								.format(johnJob2.getExecutesIn().getStart())
						+ "':");
		printJobs(finishedJobsByStartAndEnd.execute(johnJob2.getExecutesIn()
				.getStart(), null));
		logger.info("Find finished jobs by end date '"
				+ dateFormat.format(johnJob2.getExecutesIn().getEnd()) + "':");
		printJobs(finishedJobsByStartAndEnd.execute(null, johnJob2
				.getExecutesIn().getEnd()));
		logger.info("Find finished jobs by start date '"
				+ dateFormat.format(johnJob3.getExecutesIn().getStart())
				+ "' and end date '"
				+ dateFormat.format(johnJob3.getExecutesIn().getEnd()) + "':");
		printJobs(finishedJobsByStartAndEnd.execute(johnJob3.getExecutesIn()
				.getStart(), johnJob3.getExecutesIn().getEnd()));
		logger.info("Find finished jobs by start date '"
				+ dateFormat.format(new java.util.Date()) + "' and end date '"
				+ dateFormat.format(new java.util.Date()) + "':");
		printJobs(finishedJobsByStartAndEnd.execute(new java.util.Date(),
				new java.util.Date()));
	}

	private static void printJobs(List<Job> jobs) {
		for (Job job : jobs) {
			logger.info(" * " + job);
		}
	}

	public static void dst03() {
		ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
		Validator validator = vf.getValidator();

		logger.info("Validation test with valid name:");
		Computer validName = new Computer();
		validName.setName("Valid Computer");
		printViolations(validator.validateProperty(validName, "name"));

		logger.info("Validation test with too short name:");
		Computer nameTooShort = new Computer();
		nameTooShort.setName("Foo");
		printViolations(validator.validateProperty(nameTooShort, "name"));

		logger.info("Validation test with too long name:");
		Computer nameTooLong = new Computer();
		nameTooLong
				.setName("Das ist ein sehr sehr sehr langer Name für einen Computer");
		printViolations(validator.validateProperty(nameTooShort, "name"));

		logger.info("Validation test valid creation date:");
		Computer validCreation = new Computer();
		validCreation.setCreation(Date.valueOf("2009-12-03"));
		printViolations(validator.validateProperty(validCreation, "creation"));

		logger.info("Validation test invalid creation date:");
		Computer invalidCreation = new Computer();
		invalidCreation.setCreation(Date.valueOf("2012-12-03"));
		printViolations(validator.validateProperty(invalidCreation, "creation"));

		logger.info("Validation test valid lastUpdate date:");
		Computer validLastUpdated = new Computer();
		validLastUpdated.setLastUpdate(Date.valueOf("2009-12-03"));
		printViolations(validator.validateProperty(validLastUpdated,
				"lastUpdate"));

		logger.info("Validation test invalid lastUpdate date:");
		Computer invalidLastUpdated = new Computer();
		invalidLastUpdated.setLastUpdate(Date.valueOf("2012-12-03"));
		printViolations(validator.validateProperty(invalidLastUpdated,
				"lastUpdate"));

		logger.info("Validation test valid cpu count:");
		Computer validCPUs = new Computer();
		validCPUs.setCPUs(6);
		printViolations(validator.validateProperty(validCPUs, "CPUs"));

		logger.info("Validation test valid too low cpu count:");
		Computer tooFewCPUs = new Computer();
		tooFewCPUs.setCPUs(3);
		printViolations(validator.validateProperty(tooFewCPUs, "CPUs"));

		logger.info("Validation test valid too high cpu count:");
		Computer tooManyCPUs = new Computer();
		tooManyCPUs.setCPUs(9);
		printViolations(validator.validateProperty(tooManyCPUs, "CPUs"));
	}

	private static void printViolations(
			Set<ConstraintViolation<Computer>> violations) {
		logger.info(violations.size() + " violations:");
		for (ConstraintViolation<Computer> constraintViolation : violations) {
			logger.info(" * " + constraintViolation.getMessage());
		}
	}

	public static void dst04a() {
		em.getTransaction().begin();
		// transient
		Job job = new Job();
		job.setCreatedBy(john);
		job.setIsPaid(true);
		job.setNeeds(new Environment());
		job.getNeeds().setWorkflow("Workflow1");
		job.setExecutesIn(new Execution());
		job.getExecutesIn().setStatus(JobStatus.SCHEDULED);
		// persistent
		em.persist(job);
		// persistent
		job = em.find(Job.class, job.getId());
		// detached
		em.detach(job);
		// persistent
		job = em.merge(job);
		// transient
		em.remove(job);
		em.getTransaction().rollback();
	}

	public static void dst04b() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

		em.getTransaction().begin();
		Computer comp = new Computer();
		comp.setName("Neuer Computer");
		comp.setCPUs(4);
		comp.setLocation("AUT-VIE@1040");
		comp.setControlledBy(clusterWien1);
		logger.info("Persisting new computer");
		em.persist(comp);
		logger.info("creates date: " + dateFormat.format(comp.getCreation()));
		logger.info("lastUpdate date: "
				+ dateFormat.format(comp.getLastUpdate()));

		logger.info("Loading computer " + pc);
		comp = em.find(Computer.class, pc.getId());
		comp.setCPUs(6);
		logger.info("lastUpdate date: "
				+ dateFormat.format(comp.getLastUpdate()));
		logger.info("Updating loaded computer");
		em.flush();
		logger.info("lastUpdate date: "
				+ dateFormat.format(comp.getLastUpdate()));
		em.getTransaction().rollback();
	}

	public static void dst04c() {
		logger.info("Load operations:\t\t\t" + DefaultListener.getLoadCount());
		logger
				.info("Update operations:\t\t"
						+ DefaultListener.getUpdateCount());
		logger
				.info("Remove operations:\t\t"
						+ DefaultListener.getRemoveCount());
		logger.info("Persist operations:\t\t"
				+ DefaultListener.getPersistCount());
		logger.info("Overall time to persist:\t\t"
				+ bigNumberFormat.format(DefaultListener.getPersistTotalDuration()) + " ms");
		logger.info("Average time to persist:\t\t"
				+ bigNumberFormat.format(DefaultListener.getPersistAvgDuration()) + " ms");
	}

	public static void dst04d() {
		SQLInterceptor.resetSelectCounter();
		dst02b();
		logger.info("Count of select statements for dst02b: "
				+ bigNumberFormat.format(SQLInterceptor.getSelectCount()));
	}
}
