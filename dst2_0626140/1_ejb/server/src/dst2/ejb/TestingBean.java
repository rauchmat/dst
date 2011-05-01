package dst2.ejb;

import java.math.BigDecimal;
import java.sql.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import dst2.model.Address;
import dst2.model.Admin;
import dst2.model.Cluster;
import dst2.model.Computer;
import dst2.model.Grid;
import dst2.model.Membership;
import dst2.model.User;

@Stateless
public class TestingBean implements Testing {

	private static final Logger logger = LogManager
			.getLogger(TestingBean.class);

	@PersistenceContext
	private EntityManager em;

	public TestingBean() {
	}

	public void addGrid() {
		try {
			User max = new User();
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

			User john = new User();
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

			User ferdl = new User();
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

			User alice = new User();
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
			gridAustria.setName("Austria");
			gridAustria.setLocation("AUT");
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

			Cluster clusterAustria = new Cluster();
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

			Computer pc = new Computer();
			pc.setName("PC Windows 1");
			pc.setCPUs(2);
			pc.setCreation(Date.valueOf("2005-01-01"));
			pc.setLastUpdate(Date.valueOf("2011-03-15"));
			pc.setLocation("AUT-SBG@5020");
			pc.setControlledBy(clusterAustria);
			clusterAustria.getControls().add(pc);
			em.persist(pc);
			logger.info("Created " + pc);

			Computer macServer = new Computer();
			macServer.setName("Mac OS X Server");
			macServer.setCPUs(8);
			macServer.setCreation(Date.valueOf("2010-01-01"));
			macServer.setLastUpdate(Date.valueOf("2011-03-20"));
			macServer.setLocation("AUT-SBG@5020");
			macServer.setControlledBy(clusterAustria);
			clusterAustria.getControls().add(macServer);
			em.persist(macServer);
			logger.info("Created " + macServer);

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

			Computer xeon4 = new Computer();
			xeon4.setName("Intel Xeon VM 4");
			xeon4.setCPUs(4);
			xeon4.setCreation(Date.valueOf("2011-01-01"));
			xeon4.setLastUpdate(Date.valueOf("2011-03-25"));
			xeon4.setLocation("AUT-VIE@1050");
			xeon4.setControlledBy(clusterWien2);
			clusterWien2.getControls().add(xeon4);
			em.persist(xeon4);
			logger.info("Created " + xeon3);

			Membership maxToGridWien = new Membership(max, gridWien);
			maxToGridWien.setRegistration(Date.valueOf("2008-05-22"));
			maxToGridWien.setDiscount(.1);
			em.persist(maxToGridWien);
			logger.info("Created " + maxToGridWien);

			Membership maxToGridAustria = new Membership(max, gridAustria);
			maxToGridAustria.setRegistration(Date.valueOf("2007-03-15"));
			maxToGridAustria.setDiscount(0d);
			em.persist(maxToGridAustria);
			logger.info("Created " + maxToGridAustria);

			Membership johnToGridAustria = new Membership(john, gridAustria);
			johnToGridAustria.setRegistration(Date.valueOf("2009-10-03"));
			johnToGridAustria.setDiscount(.25);
			em.persist(johnToGridAustria);
			logger.info("Created " + johnToGridAustria);

			Membership ferdlToGridWien = new Membership(ferdl, gridWien);
			ferdlToGridWien.setRegistration(Date.valueOf("2009-02-25"));
			ferdlToGridWien.setDiscount(0d);
			em.persist(ferdlToGridWien);
			logger.info("Created " + ferdlToGridWien);

			Membership aliceToGridWien = new Membership(alice, gridWien);
			aliceToGridWien.setRegistration(Date.valueOf("2010-07-13"));
			aliceToGridWien.setDiscount(0d);
			em.persist(aliceToGridWien);
			logger.info("Created " + aliceToGridWien);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}
