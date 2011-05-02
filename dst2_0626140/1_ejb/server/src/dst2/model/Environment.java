package dst2.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

@Entity(name = "Environment")
@Table(name = "environment")
public class Environment {
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	@Column(name = "workflow", nullable = false)
	private String workflow;
	@ElementCollection
	@OrderColumn(name = "position")
	@CollectionTable(name = "environment_params", joinColumns = @JoinColumn(name = "environment_id"))
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
