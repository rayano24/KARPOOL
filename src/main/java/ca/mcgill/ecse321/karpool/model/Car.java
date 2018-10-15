package ca.mcgill.ecse321.karpool.model;
import javax.persistence.Column;
import javax.persistence.Entity;


import ca.mcgill.ecse321.karpool.application.Driver;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="CARS")
public class Car 
{
	@Id
	@Column(name="MAKE")
	private String make;

	public void setMake(String value) {
		this.make = value;
	}

	public String getMake() {
		return this.make;
	}

	@Id
	@Column(name="SEATS")
	private int seat;

	public void setSeat(int value) {
		this.seat = value;
	}

	public int getSeat() {
		return this.seat;
	}

	@Id
	@Column(name="LICENSE_PLATE")
	private String licensePlate;

	public void setLicensePlate(String value) {
		this.licensePlate = value;
	}

	public String getLicensePlate() {
		return this.licensePlate;
	}

	@Id
	@Column(name="FUEL_EFFICIENCY")
	private String fuelEfficiency;

	public void setFuelEfficiency(String value) {
		this.fuelEfficiency = value;
	}

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
	@Id
	@Column(name="DRIVER")
	private Driver driver;

	public void setDriver(Driver value) {
		this.driver = value;
	}

	public Driver getDriver() {
		return this.driver;
	}

}
