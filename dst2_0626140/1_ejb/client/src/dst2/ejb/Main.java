package dst2.ejb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import dst2.ejb.dto.AuditDto;
import dst2.ejb.exceptions.InvalidAssignmentException;
import dst2.ejb.exceptions.InvalidCredentialsException;
import dst2.ejb.exceptions.NotLoggedInException;

public class Main {

	private static final Logger logger = LogManager.getLogger(Main.class);

	/**
	 * @param args
	 * @throws NamingException
	 * @throws InterruptedException 
	 * @throws ExecutionException 
	 */
	public static void main(String[] args) throws NamingException, InterruptedException, ExecutionException {

		logger.info("Looking up TestingBean...");
		Testing testing = (Testing) InitialContext
				.doLookup("java:global/dst2_1/TestingBean");
		logger.info("Found and wired TestingBean.");
		logger.info("Adding grid for tests...");
		testing.addGrid();
		logger.info("Added grid for tests.");

		logger.info("Looking up GeneralManagementBean...");
		GeneralManagement generalManagement = (GeneralManagement) InitialContext
				.doLookup("java:global/dst2_1/GeneralManagementBean");
		logger.info("Found and wired GeneralManagementBean.");
		logger.info("Adding some price steps...");
		generalManagement.addPriceStep(0, BigDecimal.valueOf(5));
		generalManagement.addPriceStep(1, BigDecimal.valueOf(3));
		generalManagement.addPriceStep(2, BigDecimal.valueOf(2));
		generalManagement.addPriceStep(3, BigDecimal.valueOf(1));
		logger.info("Price steps added.");

		logger.info("Looking up JobManagementBean...");
		JobManagement jobManagement = (JobManagement) InitialContext
				.doLookup("java:global/dst2_1/JobManagementBean");
		logger.info("Found and wired JobManagementBean.");
		logger.info("Logging in with unknown user...");
		try {
			jobManagement.login("unknownUser", "Test1");
			bailOut("InvalidCredentialsException expected.");
		} catch (InvalidCredentialsException e) {
			logger.info("Login failed as expected.");
		}
		logger.info("Logging in with invalid password...");
		try {
			jobManagement.login("mm", "invalidPassword");
			bailOut("InvalidCredentialsException expected.");
		} catch (InvalidCredentialsException e) {
			logger.info("Login failed as expected.");
		}
		logger.info("Logging in with correct credentials...");
		try {
			jobManagement.login("mm", "Test1");
		} catch (InvalidCredentialsException e) {
			bailOut("InvalidCredentialsException occured unexpectedly.");
		}

		logger.info("Creating valid job assignment with 3 jobs..");
		try {
			jobManagement.add(1, 4, "map-reduce", new ArrayList<String>());
			jobManagement.add(1, 2, "tree-search", Arrays
					.asList(new String[] { "breadth" }));
			jobManagement.add(1, 2, "tree-search", Arrays
					.asList(new String[] { "depth" }));
		} catch (InvalidAssignmentException e) {
			bailOut("InvalidAssignmentException occured unexpectedly.");
		}
		logger.info("Assigned " + jobManagement.get(1) + " jobs to grid 1.");
		logger.info("Submitting jobs...");
		try {
			jobManagement.submit();
		} catch (NotLoggedInException e) {
			bailOut("NotLoggedInException occured unexpectedly.");
		} catch (InvalidAssignmentException e) {
			bailOut("InvalidAssignmentException occured unexpectedly.");
		}

		logger.info("Logging in with other credentials...");
		try {
			jobManagement.login("jd", "Test1");
		} catch (InvalidCredentialsException e) {
			bailOut("InvalidCredentialsException occured unexpectedly.");
		}

		logger.info("Creating invalid job assignment with 2 jobs...");
		try {
			jobManagement.add(2, 8, "map-reduce", new ArrayList<String>());
			jobManagement.add(2, 8, "map-reduce", new ArrayList<String>());
			bailOut("InvalidAssignmentException expected.");
		} catch (InvalidAssignmentException e) {
			logger.info("Assignment failed as expected.");
		}
		logger.info("Assigned " + jobManagement.get(2) + " jobs to grid 2.");

		logger.info("Clearing jobs of grid 2..");
		jobManagement.clear(2);
		logger.info("Assigned " + jobManagement.get(2) + " jobs to grid 2.");
		
		logger.info("Creating valid job assignment with 1 job...");
		try {
			jobManagement.add(2, 4, "map-reduce", new ArrayList<String>());
		} catch (InvalidAssignmentException e) {
			bailOut("InvalidAssignmentException occured unexpectedly.");
		}
		logger.info("Assigned " + jobManagement.get(2) + " jobs to grid 2.");
		logger.info("Submitting jobs...");
		try {
			jobManagement.submit();
		} catch (NotLoggedInException e) {
			bailOut("NotLoggedInException occured unexpectedly.");
		} catch (InvalidAssignmentException e) {
			bailOut("InvalidAssignmentException occured unexpectedly.");
		}
		
		logger.info("Waiting 6 seconds for jobs to finish..");
		Thread.sleep(6000);

		Future<String> billForMax = generalManagement.getTotalBill("mm");
		logger.info("Waiting for bill to be printed..");
		Thread.sleep(1000);
		logger.info(billForMax.get());
		Future<String> billForJohn = generalManagement.getTotalBill("jd");
		logger.info("Waiting for bill to be printed..");
		Thread.sleep(1000);
		logger.info(billForJohn.get());
		
		logger.info("Getting all audits..");
		List<AuditDto> audits = generalManagement.getAudits();
		for (AuditDto auditDto : audits) {
			logger.info(auditDto.toString());
		}
	}

	private static void bailOut(String message) {
		logger.error(message);
		System.exit(1);
	}

}
