package dst2.ejb;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Main {

	private static final Logger logger = LogManager.getLogger(Main.class);
	
	/**
	 * @param args
	 * @throws NamingException 
	 */
	public static void main(String[] args) throws NamingException {
		
		logger.info("Looking up TestingBean...");
		Testing testing = (Testing) InitialContext.doLookup("java:global/dst2_1/TestingBean");
		logger.info("Found and wired TestingBean.");
		
		logger.info("Adding grid for tests...");
		testing.addGrid();
		logger.info("Added grid for tests.");
	}

}
