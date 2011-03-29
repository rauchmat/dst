package dst1.model;

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
import javax.persistence.OneToMany;

@Entity
public class Cluster {
	@Id
	@GeneratedValue
	private Long id;
	@Column(unique = true, nullable = false)
	private String name;
	private Date lastService;
	private Date nextService;
	@ManyToOne(optional = false)
	private Grid managedBy;
	@ManyToOne(optional = false)
	private Admin maintainedBy;
	@OneToMany(mappedBy = "controlledBy")
	private Set<Computer> controls = new HashSet<Computer>();
	@ManyToMany(cascade = { CascadeType.REMOVE })
	private Set<Cluster> consistsOf = new HashSet<Cluster>();
	@ManyToMany(mappedBy = "consistsOf")
	private Set<Cluster> partOf = new HashSet<Cluster>();

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

	public Date getLastService() {
		return lastService;
	}

	public void setLastService(Date lastService) {
		this.lastService = lastService;
	}

	public Date getNextService() {
		return nextService;
	}

	public void setNextService(Date nextService) {
		this.nextService = nextService;
	}

	public void setManagedBy(Grid managedBy) {
		this.managedBy = managedBy;
	}

	public Grid getManagedBy() {
		return managedBy;
	}

	public void setControls(Set<Computer> controls) {
		this.controls = controls;
	}

	public Set<Computer> getControls() {
		return controls;
	}

	public void setMaintainedBy(Admin maintainedBy) {
		this.maintainedBy = maintainedBy;
	}

	public Admin getMaintainedBy() {
		return maintainedBy;
	}

	public void setConsistsOf(Set<Cluster> consistsOf) {
		this.consistsOf = consistsOf;
	}

	public Set<Cluster> getConsistsOf() {
		return consistsOf;
	}

	public void setPartOf(Set<Cluster> partOf) {
		this.partOf = partOf;
	}

	public Set<Cluster> getPartOf() {
		return partOf;
	}

	@Override
	public String toString() {
		return "Cluster [id=" + id + ", name=" + name + ", lastService="
				+ lastService + ", nextService=" + nextService
				+ ", maintainedBy=" + maintainedBy + ", managedBy=" + managedBy
				+ ", consistsOf=" + consistsOf + "]";
	}

}
