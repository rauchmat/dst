package dst2.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import dst2.model.Grid;
import dst2.model.User;

@Entity(name = "Membership")
@Table(name = "membership")
public class Membership {

	@Embeddable
	public static class Id implements Serializable {

		private static final long serialVersionUID = 1L;
		@Column(name = "user")
		private Long user;
		@Column(name = "grid")
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
	@Column(name = "registration", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date registration;
	@Column(name = "discount", nullable = false)
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
				+ ", discount=" + discount + "]";
	}
}
