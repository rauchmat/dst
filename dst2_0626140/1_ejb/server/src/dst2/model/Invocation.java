package dst2.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "invocation")
public class Invocation {
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "invocationTime", nullable = false)
	private Date invocationTime;
	@Column(name = "methodName", nullable = false)
	private String methodName;
	@OneToMany(mappedBy = "invocation")
	private Set<InvocationParameter> parameters = new HashSet<InvocationParameter>();
	@Column(name = "result", nullable = false)
	private String result;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getInvocationTime() {
		return invocationTime;
	}

	public void setInvocationTime(Date invocationTime) {
		this.invocationTime = invocationTime;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Set<InvocationParameter> getParameters() {
		return parameters;
	}

	public void setParameters(Set<InvocationParameter> parameters) {
		this.parameters = parameters;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getResult() {
		return result;
	}

}
