package dst2.ejb;

import java.math.BigDecimal;

import javax.ejb.Local;

@Local
public interface PriceManagement {
	void addStep(int numberOfHistoricalJobs, BigDecimal price);
	BigDecimal getPrice(int numberOfHistoricalJobs);
}
