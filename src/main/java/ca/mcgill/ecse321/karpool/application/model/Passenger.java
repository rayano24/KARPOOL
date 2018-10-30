package ca.mcgill.ecse321.karpool.application.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ca.mcgill.ecse321.karpool.application.model.UserRole;
import ca.mcgill.ecse321.karpool.application.model.Trip;

@Entity
@Table(name="passenger")
public class Passenger extends UserRole
{
	private Trip trip;

	public void setTrip(Trip value) {
		this.trip = value;
	}
	
	@ManyToOne
	@JoinColumn //(name="trip")
	public Trip getTrip() {
		return this.trip;
	}

	private int passengerId;

	public void setPassengerId(int value) {
		this.passengerId = value;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
//	@Column(name="passenger_id")
	public int getPassengerId() {
		return this.passengerId;
	}
}
