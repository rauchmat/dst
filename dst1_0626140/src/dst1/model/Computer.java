package dst1.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Table(uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Computer {

	private Long id;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private Integer CPUs;
	@Column(nullable = false)
	private String location;
	@Column(nullable = false)
	private Date creation;
	private Date lastUpdate;
	private Set<Execution> running = new HashSet<Execution>();
	@ManyToOne(optional = false)
	private Cluster controlledBy;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCPUs() {
		return CPUs;
	}

	public void setCPUs(Integer cPUs) {
		CPUs = cPUs;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getCreation() {
		return creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public void setRunning(Set<Execution> running) {
		this.running = running;
	}

	public Set<Execution> getRunning() {
		return running;
	}

	public void setControlledBy(Cluster controlledBy) {
		this.controlledBy = controlledBy;
	}

	public Cluster getControlledBy() {
		return controlledBy;
	}

	@Override
	public String toString() {
		return "Computer [id=" + id + ", name=" + name + ", creation="
				+ creation + ", lastUpdate=" + lastUpdate + ", CPUs=" + CPUs
				+ ", location=" + location + ", controlledBy=" + controlledBy
				+ ", running=" + running + "]";
	}

}
