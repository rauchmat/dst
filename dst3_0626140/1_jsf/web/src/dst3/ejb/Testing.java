package dst3.ejb;

import javax.ejb.Remote;

@Remote
public interface Testing {
	void addTestdata();
	void deleteTestdata();
	boolean hasTestdata();
}
