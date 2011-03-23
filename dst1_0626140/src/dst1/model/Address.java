package dst1.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Address {

	@Column(nullable=false)
	private String street;
	private String city;
	@Column(nullable=false)
	private String zipCode;

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Override
	public String toString() {
		return "Address [city=" + city + ", street=" + street + ", zipCode="
				+ zipCode + "]";
	}
}
