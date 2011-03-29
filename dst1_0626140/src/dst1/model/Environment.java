package dst1.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.IndexColumn;

@Entity
public class Environment {
	@Id
	@GeneratedValue
	private Long id;
	@Column(nullable = false)
	private String workflow;
	@ElementCollection
	@IndexColumn(name="position")
	private List<String> params = new ArrayList<String>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWorkflow() {
		return workflow;
	}

	public void setWorkflow(String workflow) {
		this.workflow = workflow;
	}

	public List<String> getParams() {
		return params;
	}

	public void setParams(List<String> params) {
		this.params = params;
	}

	@Override
	public String toString() {
		return "Environment [id=" + id + ", workflow=" + workflow + ", params="
				+ params + "]";
	}

}
