package dst3.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import dst3.model.Cluster;
import dst3.model.Execution;

@Entity
@Table(name = "computer")
public class Computer {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	@Column(name = "name", nullable = false, unique = true)
	private String name;
	@Column(name = "cpus", nullable = false)
	private Integer CPUs;
	@Column(name = "location", nullable = false)
	private String location;
	@Temporal(TemporalType.DATE)
	@Column(name = "creation", nullable = false)
	private Date creation;
	@Temporal(TemporalType.DATE)
	@Column(name = "lastUpdate", nullable = false)
	private Date lastUpdate;
	@ManyToMany(cascade = { CascadeType.REMOVE })
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
				+ ", location=" + location + ", running=" + running
				+ ", controlledBy=" + controlledBy + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Computer other = (Computer) obj;
		if (id == null) {
			if (other.id != null)
				return false;
			else
				return this == obj;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
