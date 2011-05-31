package dst3.ws;

import java.io.Serializable;
import java.util.Date;

public class JobDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Date start;
	private Date end;
	private String username;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
