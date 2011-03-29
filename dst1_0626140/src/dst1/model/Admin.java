package dst1.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Admin extends Person {

	@OneToMany(mappedBy = "maintainedBy")
	private Set<Cluster> maintains = new HashSet<Cluster>();

	public void setMaintains(Set<Cluster> maintains) {
		this.maintains = maintains;
	}

	public Set<Cluster> getMaintains() {
		return maintains;
	}

	@Override
	public String toString() {
		return "User [id=" + getId() + ", firstname=" + getFirstname()
				+ ", lastname=" + getLastname() + "]";
	}
}
