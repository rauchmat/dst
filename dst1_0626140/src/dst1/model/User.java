package dst1.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class User extends Person {

//	@Column(nullable = false)
	private String username;
//	@Column(nullable = false)
	private String password;
	@Column(length = 11)
	private String accountNo;
	@Column(length = 5)
	private String bankCode;
	@OneToMany(mappedBy = "createdBy")
	private Set<Job> creates = new HashSet<Job>();
	@OneToMany(mappedBy = "user")
	private Set<Membership> memberships = new HashSet<Membership>();

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankCode() {
		return bankCode;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setCreates(Set<Job> creates) {
		this.creates = creates;
	}

	public Set<Job> getCreates() {
		return creates;
	}

	public void setMemberships(Set<Membership> memberships) {
		this.memberships = memberships;
	}

	public Set<Membership> getMemberships() {
		return memberships;
	}

	@Override
	public String toString() {
		return "User [id=" + getId() + ", firstname=" + getFirstname()
				+ ", lastname=" + getLastname() + ", accountNo=" + accountNo
				+ ", bankCode=" + bankCode + ", username=" + username
				+ ", password=" + password + ", address=" + getAddress()
				+ ", creates=" + creates + ", memberships=" + memberships + "]";
	}

}
