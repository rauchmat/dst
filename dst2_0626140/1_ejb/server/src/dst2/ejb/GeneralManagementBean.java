package dst2.ejb;

import java.math.BigDecimal;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class GeneralManagementBean implements GeneralManagement {

	@EJB
	private PriceManagement priceManagement;

	@Override
	public void addPriceStep(int numberOfHistoricalJobs, BigDecimal price) {
		priceManagement.addStep(numberOfHistoricalJobs, price);
	}

	@Override
	public String getTotalBill(String username) {
		// TODO Auto-generated method stub
		return null;
	}

}
