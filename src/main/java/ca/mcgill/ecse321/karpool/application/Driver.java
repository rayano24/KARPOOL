package ca.mcgill.ecse321.karpool.application;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import ca.mcgill.ecse321.karpool.model.UserRole;
import ca.mcgill.ecse321.karpool.model.Car;
import ca.mcgill.ecse321.karpool.model.Trip;

@Entity
@Table(name="DRIVERS")
public class Driver extends UserRole
{
	@Id
	@Column(name="CAR")
	private Car car;

	public void setCar(Car value) {
		this.car = value;
	}

	public Car getCar() {
		return this.car;
	}
	
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
	@Column(name="DRIVER_ID")
	private int driverId;

	public void setDriverId(int value) {
		this.driverId = value;
	}

	public int getDriverId() {
		return this.driverId;
	}
}
