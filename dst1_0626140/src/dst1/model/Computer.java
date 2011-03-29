package dst1.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import dst1.listener.EntityListener;
import dst1.validator.CPUs;

@EntityListeners(EntityListener.class)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Computer {

	private Long id;
	@Column(nullable = false)
	@Size(min = 5, max = 25)
	private String name;
	@Column(nullable = false)
	@CPUs(min = 4, max = 8)
	private Integer CPUs;
	@Column(nullable = false)
	@Pattern(regexp = "[A-Z]{3}\\-[A-Z]{3}@\\d{4}")
	private String location;
	@Column(nullable = false)
	@Past
	private Date creation;
	@Past
	@Column(nullable = false)
	private Date lastUpdate;
	@ManyToMany(cascade = { CascadeType.ALL })
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
}
