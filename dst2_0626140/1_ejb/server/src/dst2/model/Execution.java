package dst2.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name = "Execution")
@Table(name = "execution")
public class Execution {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start")
	private Date start;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end")
	private Date end;
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private JobStatus status;
	@ManyToMany(mappedBy = "running")
	private Set<Computer> runsOn = new HashSet<Computer>();
	@OneToOne(optional = false, mappedBy = "executesIn", fetch = FetchType.EAGER)
	private Job executes;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public JobStatus getStatus() {
		return status;
	}

	public void setStatus(JobStatus status) {
		this.status = status;
	}

	public void setRunsOn(Set<Computer> runsOn) {
		this.runsOn = runsOn;
	}

	public Set<Computer> getRunsOn() {
		return runsOn;
	}

	public void setExecutes(Job executes) {
		this.executes = executes;
	}

	public Job getExecutes() {
		return executes;
	}

	@Override
	public String toString() {
		return "Execution [id=" + id + ", start=" + start + ", end=" + end
				+ ", status=" + status + "]";
	}

}
