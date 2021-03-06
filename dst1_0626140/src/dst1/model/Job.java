package dst1.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import javax.persistence.CascadeType;

@Entity
public class Job {
	@Id
	@GeneratedValue
	private Long id;
	@Column(nullable = false)
	private Boolean isPaid;
	@OneToOne(optional = false, cascade = { CascadeType.ALL })
	private Execution executesIn;
	@OneToOne(optional = false, cascade = { CascadeType.ALL })
	private Environment needs;
	@ManyToOne(optional = false)
	private User createdBy;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getIsPaid() {
		return isPaid;
	}

	public void setIsPaid(Boolean isPaid) {
		this.isPaid = isPaid;
	}

	public Integer getNumCPUs() {
		return null;
	}

	public Integer getExecutionTime() {
		return null;
	}

	public void setExecutesIn(Execution executesIn) {
		this.executesIn = executesIn;
	}

	public Execution getExecutesIn() {
		return executesIn;
	}

	public void setNeeds(Environment needs) {
		this.needs = needs;
	}

	public Environment getNeeds() {
		return needs;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	@Override
	public String toString() {
		return "Job [id=" + id + ", isPaid=" + isPaid + ", createdBy="
				+ createdBy + ", needs=" + needs + ", executesIn=" + executesIn
				+ "]";
	}
}
