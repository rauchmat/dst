package dst2.ejb;

import java.util.List;

import javax.ejb.Remote;

import dst2.ejb.exceptions.InvalidAssignmentException;
import dst2.ejb.exceptions.InvalidCredentialsException;
import dst2.ejb.exceptions.NotLoggedInException;

@Remote
public interface JobManagement {
	void login(String username, String password)
			throws InvalidCredentialsException;

	void add(long gridId, int numberOfCPUs, String workflow,
			List<String> parameters) throws InvalidAssignmentException;

	int get(long gridId);

	void clear(long gridId);

	void submit() throws NotLoggedInException, InvalidAssignmentException;
}
