package dst2.ejb;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Future;

import javax.ejb.Remote;

import dst2.ejb.dto.AuditDto;

@Remote
public interface GeneralManagement {
	void addPriceStep(int numberOfHistoricalJobs, BigDecimal price);

	Future<String> getTotalBill(String username);
	
	List<AuditDto> getAudits();
}
