package ca.mcgill.ecse321.karpool.application;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import ca.mcgill.ecse321.karpool.model.UserRole;
import ca.mcgill.ecse321.karpool.model.Trip;

@Entity
@Table(name="PASSENGERS")
public class Passenger extends UserRole
{
	@Id
	@Column(name="TRIP")
	private Trip trip;

	public void setTrip(Trip value) {
		this.trip = value;
	}

	public Trip getTrip() {
		return this.trip;
	}
	
	@Id
	@Column(name="PASSENGER_ID")
	private int passengerId;

	public void setPassengerId(int value) {
		this.passengerId = value;
	}

	public int getPassengerId() {
		return this.passengerId;
	}
}
