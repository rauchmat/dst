package dst2.ejb;

import java.util.ArrayList;

public class TemporaryJobs extends ArrayList<TemporaryJob> {

	private static final long serialVersionUID = 1L;
	
	private int numberOfCPUsUsed = 0;

	public void addNumberOfCPUsUsed(int additionalCPUsUsed) {
		this.numberOfCPUsUsed += additionalCPUsUsed;
	}

	public int getNumberOfCPUsUsed() {
		return numberOfCPUsUsed;
	}

}
