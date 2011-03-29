package dst1.listener;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import dst1.model.Computer;

public class EntityListener {
	@PrePersist
	public void onPrePersist(Computer computer) {
		Date now = new Date();
		computer.setCreation(now);
		computer.setLastUpdate(now);
	}

	@PreUpdate
	public void onPreUpdate(Computer computer) {
		computer.setLastUpdate(new Date());
	}

}
