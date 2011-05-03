package dst2.ejb;

import java.math.BigDecimal;
import java.util.concurrent.Future;

import javax.ejb.Remote;

@Remote
public interface GeneralManagement {
	void addPriceStep(int numberOfHistoricalJobs, BigDecimal price);

	Future<String> getTotalBill(String username);
}
