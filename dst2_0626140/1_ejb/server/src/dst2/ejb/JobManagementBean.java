package dst2.ejb;

import java.util.List;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import dst2.ejb.exceptions.InvalidCredentialsException;
import dst2.model.User;

@Stateful
public class JobManagementBean implements JobManagement {

	@PersistenceContext
	private EntityManager em;
	private User user;

	@Override
	public void login(String username, String password)
			throws InvalidCredentialsException {
		TypedQuery<User> query = em.createNamedQuery(User.USER_BY_CREDENTIALS,
				User.class);
		List<User> users = query.getResultList();
		
		if (users.size() != 1) {
			throw new InvalidCredentialsException(
					"Username or password are incorrect.");
		}
		this.user = users.get(0);
	}

}
