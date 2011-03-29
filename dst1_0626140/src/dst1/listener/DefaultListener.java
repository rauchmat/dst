package dst1.listener;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;

public class DefaultListener {
	private static int loadCount = 0;
	private static int updateCount = 0;
	private static int removeCount = 0;
	private static int persistCount = 0;
	private static long persistTotalDuration = 0;
	private static long persistAvgDuration = 0;
	private static Map<Object, Long> persistTimer = new HashMap<Object, Long>();

	@PostLoad
	public synchronized void onPostLoad(Object obj) {
		loadCount++;
	}

	@PostUpdate
	public synchronized void onPostUpdate(Object obj) {
		updateCount++;
	}

	@PostRemove
	public synchronized void onPostRemove(Object obj) {
		removeCount++;
	}

	@PrePersist
	public synchronized void onPrePersist(Object obj) {
		persistTimer.put(obj, System.currentTimeMillis());
	}

	@PostPersist
	public synchronized void onPostPersist(Object obj) {
		Long duration = System.currentTimeMillis() - persistTimer.get(obj);
		persistTotalDuration += duration;
		persistCount++;
		persistAvgDuration = persistTotalDuration / persistCount;
	}

	public static int getLoadCount() {
		return loadCount;
	}

	public static int getUpdateCount() {
		return updateCount;
	}

	public static int getRemoveCount() {
		return removeCount;
	}

	public static int getPersistCount() {
		return persistCount;
	}

	public static long getPersistTotalDuration() {
		return persistTotalDuration;
	}

	public static long getPersistAvgDuration() {
		return persistAvgDuration;
	}
	
	

}
