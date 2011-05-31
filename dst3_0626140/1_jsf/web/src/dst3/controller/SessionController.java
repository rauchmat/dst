package dst3.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import dst3.ejb.exceptions.InvalidCredentialsException;
import dst3.util.FacesUtil;

@ManagedBean
@SessionScoped
public class SessionController {

	private static final Logger logger = Logger
			.getLogger(SessionController.class.getName());

	private String username;
	private String password;
	private boolean loggedIn;

	@ManagedProperty("#{jobController}")
	private JobController jobController;

	public SessionController() {
	}

	public void setJobController(JobController jobController) {
		this.jobController = jobController;
	}

	public JobController getJobController() {
		return jobController;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void login() {
		FacesContext context = FacesContext.getCurrentInstance();

		try {
			jobController.getJobManagement().login(username, password);
			loggedIn = true;
			FacesUtil.displayResult("login.success", "home.jsf");
		} catch (InvalidCredentialsException e) {
			logger.log(Level.WARNING, "Invalid credentials.", e);
			context.addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, FacesUtil
							.getMessage("login.failed"), null));

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unexpected error while logging in.", e);
			FacesUtil.displayResult("login.loginError", "home.jsf");
		}

	}

	public void logout() {
		try {
			loggedIn = false;
			jobController.getJobManagement().logout();
			FacesUtil.displayResult("home.loggedOut", "home.jsf");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unexpected error while logging out.", e);
			FacesUtil.displayResult("login.logoutError", "home.jsf");
		}
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

}
