package dst3.controller;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import dst3.util.FacesUtil;
import dst3.ws.client.JobDto;
import dst3.ws.client.JobSearchBean;
import dst3.ws.client.JobSearchBeanService;

@ManagedBean
@SessionScoped
public class SearchController {

	private static final Logger logger = Logger
			.getLogger(SearchController.class.getName());

	private String gridName;
	private List<JobDto> result;

	public void setGridName(String gridName) {
		this.gridName = gridName;
	}

	public String getGridName() {
		return gridName;
	}

	public void setResult(List<JobDto> result) {
		this.result = result;
	}

	public List<JobDto> getResult() {
		return result;
	}

	public void search() {
		try {
			JobSearchBean jobSearchBean = new JobSearchBeanService()
					.getPort(JobSearchBean.class);
			setResult(jobSearchBean.find(gridName));
			gridName = null;
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("result.jsf");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unexpected error while searching jobs.",
					e);
			FacesUtil.displayResult("search.error", "home.jsf");
		}
	}
}
