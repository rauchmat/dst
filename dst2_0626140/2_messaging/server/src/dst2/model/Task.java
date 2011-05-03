package dst2.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "task")
public class Task {
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	@Column(name = "jobId", nullable = false)
	private Long jobId;
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private TaskStatus status;
	@Column(name = "ratedBy")
	private String ratedBy;
	@Enumerated(EnumType.STRING)
	@Column(name = "complexity", nullable = false)
	private TaskComplexity complexity;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public String getRatedBy() {
		return ratedBy;
	}

	public void setRatedBy(String ratedBy) {
		this.ratedBy = ratedBy;
	}

	public TaskComplexity getComplexity() {
		return complexity;
	}

	public void setComplexity(TaskComplexity complexity) {
		this.complexity = complexity;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + "complexity=" + complexity + ", jobId="
				+ jobId + ", ratedBy=" + ratedBy + ", status=" + status + "]";
	}

}
