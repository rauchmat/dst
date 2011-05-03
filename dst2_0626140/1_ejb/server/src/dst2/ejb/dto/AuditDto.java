package dst2.ejb.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuditDto implements Serializable {

	private static final long serialVersionUID = 1L;

	public Date invocationTime;
	public String methodName;
	public String result;
	public List<AuditParameterDto> parameters = new ArrayList<AuditParameterDto>();

	public AuditDto() {

	}

	public AuditDto(Date invocationTime, String methodName, String result) {
		this.invocationTime = invocationTime;
		this.methodName = methodName;
		this.result = result;
	}

	@Override
	public String toString() {
		return "AuditDto [invocationTime=" + invocationTime + ", methodName="
				+ methodName + ", parameters=" + parameters + ", result="
				+ result + "]";
	}

}
