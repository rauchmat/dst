package dst3.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import dst3.ejb.Testing;
import dst3.util.FacesUtil;

@ManagedBean
@ViewScoped
public class HomeController {

	private static final Logger logger = Logger.getLogger(HomeController.class
			.getName());

	@EJB
	private Testing testing;

	public boolean isTestdataExisting() {
		return testing.hasTestdata();
	}

	public void createTestdata() {
		try {
			testing.addTestdata();
			FacesUtil.setResult("home.created");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unexepected error creating test data.", e);
			FacesUtil.displayResult("home.error");
		}
	}

	public void deleteTestdata() {
		try {
			testing.deleteTestdata();
			FacesUtil.setResult("home.deleted");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unexepected error while deleting data.",
					e);
			FacesUtil.displayResult("home.error");
		}
	}

}
