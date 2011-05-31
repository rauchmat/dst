package dst3.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import dst3.model.Address;
import dst3.model.User;
import dst3.util.FacesUtil;

@ManagedBean
@ViewScoped
public class RegisterController {

	private static final Logger logger = Logger
			.getLogger(RegisterController.class.getName());

	private User user;
	private String passwordConfirmation;

	@PersistenceContext
	private EntityManager em;

	@Resource
	private UserTransaction tx;

	public RegisterController() {
		user = new User();
		user.setAddress(new Address());
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}

	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void register() {
		FacesContext context = FacesContext.getCurrentInstance();

		if (!user.getPassword().equals(passwordConfirmation)) {
			context.addMessage(
					"registerForm:confirmPassword",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, FacesUtil
							.getMessage("register.passwordsNotMatch"), null));
			return;
		}

		try {
			tx.begin();
			Query query = em.createNamedQuery("usersByUsername");
			query.setParameter("username", user.getUsername());
			if (query.getResultList().size() > 0) {
				context.addMessage(
						"registerForm:username",
						new FacesMessage(FacesMessage.SEVERITY_ERROR, FacesUtil
								.getMessage("register.userAlreadyExisting"),
								null));
				return;
			}

			em.persist(user);
			tx.commit();

			FacesUtil.displayResult("register.success", "home.jsf");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unexepected error while registering user.", e);
			try {
				tx.rollback();
			} catch (Exception e1) {
			}

			FacesUtil.displayResult("register.error", "overview.jsf");
		}

	}

}
