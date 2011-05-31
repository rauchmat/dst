package dst3.controller;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import dst3.model.Computer;
import dst3.model.Grid;
import dst3.util.FacesUtil;

@ManagedBean
@ViewScoped
public class OverviewController {

	private static final Logger logger = Logger
			.getLogger(OverviewController.class.getName());

	@PersistenceContext
	private EntityManager em;

	@ManagedProperty("#{jobController}")
	private JobController jobController;

	public void setJobController(JobController jobController) {
		this.jobController = jobController;
	}

	public JobController getJobController() {
		return jobController;
	}

	@SuppressWarnings("unchecked")
	public List<Grid> getAllGrids() {
		Query query = em.createNamedQuery("allGrids");
		return query.getResultList();
	}

	public String getFreeCPUsByGrid(Long gridId) {
		Query query = em.createNamedQuery("freeComputersByGrid");
		query.setParameter("gridId", gridId);
		Integer freeCPUs = 0;
		for (Object obj : query.getResultList()) {
			Computer comp = (Computer) obj;
			freeCPUs += comp.getCPUs();
		}
		return freeCPUs.toString();
	}

	public void addJob(Long gridId) {
		try {
			getJobController().setGridId(gridId);
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("job.jsf");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unexpected error while adding job.", e);
			FacesUtil.displayResult("overview.addError", "home.jsf");
		}
	}

}
