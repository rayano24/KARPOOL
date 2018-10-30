package ca.mcgill.ecse321.karpool.application.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.karpool.application.model.UserRole;
import ca.mcgill.ecse321.karpool.application.model.Car;
import ca.mcgill.ecse321.karpool.application.model.Trip;

@Entity
@Table(name="driver")
public class Driver extends UserRole
{
	private Car car;

	public void setCar(Car value) {
		this.car = value;
	}

	@OneToOne
	public Car getCar() {
		return this.car;
	}

	private Trip trip;

	public void setTrip(Trip value) {
		this.trip = value;
	}

	@ManyToOne
	@JoinColumn //(name="trip")
	public Trip getTrip() {
		return this.trip;
	}
	
	private int driverId;

	public void setDriverId(int value) {
		this.driverId = value;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
//	@Column(name="driver_id")
	public int getDriverId() {
		return this.driverId;
	}
}
