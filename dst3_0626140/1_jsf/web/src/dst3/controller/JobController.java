package dst3.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import dst3.ejb.JobManagement;
import dst3.ejb.exceptions.InvalidAssignmentException;
import dst3.ejb.exceptions.NotLoggedInException;
import dst3.model.Grid;
import dst3.util.FacesUtil;

@ManagedBean
@SessionScoped
public class JobController {

	private static final Logger logger = Logger.getLogger(JobController.class
			.getName());

	@EJB
	private JobManagement jobManagement;

	@PersistenceContext
	private EntityManager em;

	private Integer numberOfCPUs;
	private String workflow;
	private String param1;
	private String param2;
	private String param3;
	private String param4;
	private String param5;
	private Long gridId;

	public JobController() {

	}

	public JobManagement getJobManagement() {
		return jobManagement;
	}

	public Long getGridId() {
		return gridId;
	}

	public void setGridId(Long gridId) {
		this.gridId = gridId;
	}

	public void setNumberOfCPUs(Integer numberOfCPUs) {
		this.numberOfCPUs = numberOfCPUs;
	}

	public Integer getNumberOfCPUs() {
		return numberOfCPUs;
	}

	public void setWorkflow(String workflow) {
		this.workflow = workflow;
	}

	public String getWorkflow() {
		return workflow;
	}

	public String getParam1() {
		return param1;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}

	public String getParam2() {
		return param2;
	}

	public void setParam2(String param2) {
		this.param2 = param2;
	}

	public String getParam3() {
		return param3;
	}

	public void setParam3(String param3) {
		this.param3 = param3;
	}

	public String getParam4() {
		return param4;
	}

	public void setParam4(String param4) {
		this.param4 = param4;
	}

	public String getParam5() {
		return param5;
	}

	public void setParam5(String param5) {
		this.param5 = param5;
	}

	@SuppressWarnings("unchecked")
	public List<Grid> getAllGrids() {
		Query query = em.createNamedQuery("allGrids");
		return query.getResultList();
	}

	public void add() {
		List<String> parameters = new ArrayList<String>();
		if (param1 != "")
			parameters.add(param1);
		if (param2 != "")
			parameters.add(param2);
		if (param3 != "")
			parameters.add(param3);
		if (param4 != "")
			parameters.add(param4);
		if (param5 != "")
			parameters.add(param5);

		try {
			getJobManagement().add(getGridId(), numberOfCPUs, workflow,
					parameters);
			clearParameters();
			FacesUtil.setResult("job.success");
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("overview.jsf");
		} catch (InvalidAssignmentException e) {
			logger.log(Level.WARNING, "Error while assigning jobs", e);
			FacesUtil.displayResult("job.invalid", "overview.jsf");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unexpected error while assigning jobs", e);
			FacesUtil.displayResult("overview.jsf");
		}
	}

	private void clearParameters() {
		workflow = null;
		numberOfCPUs = null;
		param1 = null;
		param2 = null;
		param3 = null;
		param4 = null;
		param5 = null;
	}

	public int getJobCount(Long gridId) {
		return getJobManagement().get(gridId);
	}

	public void clearJobs(Long gridId) {
		try {
			getJobManagement().clear(gridId);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unexpected error while removing jobs", e);
			FacesUtil.displayResult("overview.jsf");
		}
	}

	public void submitJobs() {
		try {
			getJobManagement().submit();
			FacesUtil.displayResult("job.submitSuccess", "home.jsf");
		} catch (InvalidAssignmentException e) {
			logger.log(Level.WARNING, "Error while submitting jobs", e);
			FacesUtil.displayResult("job.invalid", "overview.jsf");
		} catch (NotLoggedInException e) {
			logger.log(Level.WARNING, "Error while submitting jobs", e);
			FacesUtil.displayResult("job.notLoggedIn", "overview.jsf");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unexpected error while removing jobs", e);
			FacesUtil.displayResult("overview.jsf");
		}

	}
}
