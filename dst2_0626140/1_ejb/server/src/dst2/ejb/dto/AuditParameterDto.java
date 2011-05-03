package dst2.ejb.dto;

import java.io.Serializable;

public class AuditParameterDto implements Serializable {

	private static final long serialVersionUID = 1L;

	public Integer index;
	public String className;
	public String value;

	public AuditParameterDto() {

	}

	public AuditParameterDto(int index, String className, String value) {
		this.index = index;
		this.className = className;
		this.value = value;
	}

	@Override
	public String toString() {
		return "AuditParameterDto [className=" + className + ", index=" + index
				+ ", value=" + value + "]";
	}

}
