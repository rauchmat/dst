package dst2.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "invocationParameter")
public class InvocationParameter {
	@Embeddable
	public static class Id implements Serializable {

		private static final long serialVersionUID = 1L;
		@Column(name = "invocation")
		private Long invocation;
		@Column(name = "idx")
		private Integer index;

		public Id() {
			super();
		}

		public Id(long invocation, int index) {
			super();
			this.invocation = invocation;
			this.index = index;
		}

		public boolean equals(Object obj) {
			if (obj != null && obj instanceof Id) {
				return this.invocation.equals(((Id) obj).invocation)
						&& this.index.equals(((Id) obj).index);
			} else {
				return false;
			}
		}

		public int hashCode() {
			return invocation.hashCode() + index.hashCode();
		}

		@Override
		public String toString() {
			return "Id [invocation=" + invocation + ", invocation="
					+ invocation + "]";
		}
	}
	
	public InvocationParameter() {
	}
	
	public InvocationParameter(Invocation invocation, int index) {
		this.invocation = invocation;
		this.id.invocation = invocation.getId();
		this.id.index = index;
		
		invocation.getParameters().add(this);
	}

	@EmbeddedId
	private Id id = new Id();
	@ManyToOne
	@JoinColumn(name = "invocation", insertable = false, updatable = false)
	private Invocation invocation;

	@Column(name = "className", nullable = false)
	private String className;

	@Column(name = "value", nullable = false)
	private String value;
	
	public Id getId() {
		return id;
	}

	public Invocation getInvocation() {
		return invocation;
	}
	
	public int getIndex() {
		return this.id.index;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
