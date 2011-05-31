package dst3.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity(name = "User")
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = {
		"accountNo", "bankCode" }))
public class User extends Person {

	@Column(name = "username", nullable = false)
	private String username;
	@Column(name = "password", nullable = false)
	private String password;
	@Column(name = "accountNo", length = 11)
	private String accountNo;
	@Column(name = "bankCode", length = 5)
	private String bankCode;
	@OneToMany(mappedBy = "createdBy", cascade = { CascadeType.REMOVE })
	private Set<Job> creates = new HashSet<Job>();
	@OneToMany(mappedBy = "user", cascade = { CascadeType.REMOVE })
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
				+ ", password=" + password + ", address=" + getAddress() + "]";
	}

}
