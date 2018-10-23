package ca.mcgill.ecse321.karpool.application.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.karpool.application.model.UserRole;
import ca.mcgill.ecse321.karpool.application.model.Car;
import ca.mcgill.ecse321.karpool.application.model.Trip;

@Entity
public class Driver extends UserRole
{
	private Car car;

	public void setCar(Car value) {
		this.car = value;
	}

	@ManyToOne
	@JoinColumn(name="car")
	public Car getCar() {
		return this.car;
	}

	private Trip trip;

	public void setTrip(Trip value) {
		this.trip = value;
	}

	@ManyToOne
	@JoinColumn(name="trip")
	public Trip getTrip() {
		return this.trip;
	}
	
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private int driverId;

	public void setDriverId(int value) {
		this.driverId = value;
	}

	@Id
	@Column(name="driver_id")
	public int getDriverId() {
		return this.driverId;
	}
}
