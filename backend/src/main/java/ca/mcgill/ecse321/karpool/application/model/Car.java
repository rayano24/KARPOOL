package ca.mcgill.ecse321.karpool.application.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
public class Car 
{

	private String make;

	public void setMake(String value) {
		this.make = value;
	}

	@Column(name="make")
	public String getMake() {
		return this.make;
	}

	private int seat;

	public void setSeat(int value) {
		this.seat = value;
	}

	@Column(name="seat")
	public int getSeat() {
		return this.seat;
	}

	private String licensePlate;

	public void setLicensePlate(String value) {
		this.licensePlate = value;
	}

	@Id
	@Column(name="license_plate")
	public String getLicensePlate() {
		return this.licensePlate;
	}

	private String fuelEfficiency;

	public void setFuelEfficiency(String value) {
		this.fuelEfficiency = value;
	}

	@Column(name="fuel_efficiency")
	public String getFuelEfficiency() {
		return this.fuelEfficiency;
	}

	/**
	 * <pre>
	 *           1..1     1..1
	 * Car ------------------------- Driver
	 *           car        &gt;       driver
	 * </pre>
	 */
	private Driver driver;

	public void setDriver(Driver value) {
		this.driver = value;
	}
	
	@ManyToOne
	@Column(name="driver")
	public Driver getDriver() {
		return this.driver;
	}

}
