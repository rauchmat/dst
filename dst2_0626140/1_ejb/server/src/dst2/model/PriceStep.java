package dst2.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pricestep")
public class PriceStep implements Comparable<PriceStep> {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(name = "numberOfHistoricalJobs", nullable = false, unique = true)
	private Integer numberOfHistoricalJobs;

	@Column(name = "price", nullable = false)
	private BigDecimal price;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNumberOfHistoricalJobs() {
		return numberOfHistoricalJobs;
	}

	public void setNumberOfHistoricalJobs(Integer numberOfHistoricalJobs) {
		this.numberOfHistoricalJobs = numberOfHistoricalJobs;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "PriceStep [id=" + id + ", numberOfHistoricalJobs="
				+ numberOfHistoricalJobs + ", price=" + price + "]";
	}

	@Override
	public int compareTo(PriceStep other) {
		return getNumberOfHistoricalJobs().compareTo(other.getNumberOfHistoricalJobs());
	}

}
