package dst1.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Grid {
	@Id
	@GeneratedValue
	private Long id;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private String location;
	@Column(nullable = false)
	private BigDecimal costsPerCPUMinute;
	@OneToMany(mappedBy = "grid", cascade = { CascadeType.REMOVE })
	private Set<Membership> memberships = new HashSet<Membership>();
	@OneToMany(mappedBy = "managedBy")
	private Set<Cluster> manages = new HashSet<Cluster>();

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

	public String getLocation() {
		return location;
	}

	public void setLocation(String loacation) {
		this.location = loacation;
	}

	public BigDecimal getCostsPerCPUMinute() {
		return costsPerCPUMinute;
	}

	public void setCostsPerCPUMinute(BigDecimal costsPerCPUMinute) {
		this.costsPerCPUMinute = costsPerCPUMinute;
	}

	public void setMemberships(Set<Membership> memberships) {
		this.memberships = memberships;
	}

	public Set<Membership> getMemberships() {
		return memberships;
	}

	public void setManages(Set<Cluster> manages) {
		this.manages = manages;
	}

	public Set<Cluster> getManages() {
		return manages;
	}

	@Override
	public String toString() {
		return "Grid [id=" + id + ", name=" + name + ", location=" + location
				+ ", costsPerCPUMinute=" + costsPerCPUMinute + ", memberships="
				+ memberships + "]";
	}
}
