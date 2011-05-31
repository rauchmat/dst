package dst3.ejb;

import java.util.List;

import javax.ejb.Remote;

import dst3.ejb.exceptions.InvalidAssignmentException;
import dst3.ejb.exceptions.InvalidCredentialsException;
import dst3.ejb.exceptions.NotLoggedInException;

@Remote
public interface JobManagement {
	void login(String username, String password)
			throws InvalidCredentialsException;
	
	boolean isLoggedIn();

	void logout();

	void add(long gridId, int numberOfCPUs, String workflow,
			List<String> parameters) throws InvalidAssignmentException;

	int get(long gridId);

	void clear(long gridId);

	void submit() throws NotLoggedInException, InvalidAssignmentException;
}
