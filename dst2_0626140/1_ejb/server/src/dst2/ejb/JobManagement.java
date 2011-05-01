package dst2.ejb;

import javax.ejb.Remote;

import dst2.ejb.exceptions.InvalidCredentialsException;

@Remote
public interface JobManagement {
	void login(String username, String password) throws InvalidCredentialsException;
}
