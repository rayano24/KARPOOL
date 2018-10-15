package ca.mcgill.ecse321.karpool.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import ca.mcgill.ecse321.karpool.model.UserRole;
import ca.mcgill.ecse321.karpool.model.Car;
import ca.mcgill.ecse321.karpool.model.Trip;

@Entity
public class Driver extends UserRole
{

	private Car car;

	public void setCar(Car value) {
		this.car = value;
	}

	public Car getCar() {
		return this.car;
	}

	private Trip trip;

	public void setTrip(Trip value) {
		this.trip = value;
	}

	public Trip getTrip() {
		return this.trip;
	}
	
	private int driverId;

	public void setDriverId(int value) {
		this.driverId = value;
	}

	@Id
	public int getDriverId() {
		return this.driverId;
	}
}
