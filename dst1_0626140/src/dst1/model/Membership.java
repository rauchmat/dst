package dst1.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Membership {
	@Embeddable
	public static class Id implements Serializable {

		private static final long serialVersionUID = 1L;
		private Long user;
		private Long grid;

		public Id() {
			super();
		}

		public Id(long user, long grid) {
			super();
			this.user = user;
			this.grid = grid;
		}

		public boolean equals(Object obj) {
			if (obj != null && obj instanceof Id) {
				return this.user.equals(((Id) obj).user)
						&& this.grid.equals(((Id) obj).grid);
			} else {
				return false;
			}
		}

		public int hashCode() {
			return user.hashCode() + grid.hashCode();
		}

		@Override
		public String toString() {
			return "Id [grid=" + grid + ", user=" + user + "]";
		}

	}

	@EmbeddedId
	private Id id = new Id();
	@ManyToOne
	@JoinColumn(name = "user", insertable = false, updatable = false)
	private User user;
	@ManyToOne
	@JoinColumn(name = "grid", insertable = false, updatable = false)
	private Grid grid;
	@Column(nullable = false)
	private Date registration;
	@Column(nullable = false)
	private Double discount;

	public Membership() {
	}

	public Membership(User user, Grid grid) {
		super();
		this.user = user;
		this.grid = grid;
		this.id.grid = grid.getId();
		this.id.user = user.getId();

		user.getMemberships().add(this);
		grid.getMemberships().add(this);

	}

	public Id getId() {
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}

	public Date getRegistration() {
		return registration;
	}

	public void setRegistration(Date registration) {
		this.registration = registration;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public User getUser() {
		return user;
	}

	public Grid getGrid() {
		return grid;
	}

	@Override
	public String toString() {
		return "Membership [id=" + id + ", registration=" + registration
				+ ", discount=" + discount + ", grid=" + grid.getId()
				+ ", user=" + user.getId() + "]";
	}
}
