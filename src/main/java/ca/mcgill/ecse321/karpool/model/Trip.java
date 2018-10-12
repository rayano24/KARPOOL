package ca.mcgill.ecse321.karpool.model;
import javax.persistence.Column;
import javax.persistence.Entity;

import ca.mcgill.ecse321.karpool.application.Driver;
import java.util.Set;

import javax.persistence.Id;
import javax.persistence.Table;

import java.util.HashSet;

import ca.mcgill.ecse321.karpool.application.Passenger;

@Entity
@Table(name = "TRIPS")
public class Trip {

	@Id
	@Column(name = "TRIP_ID")
	private int tripId;

	public void setTripId(int value) {
		this.tripId = value;
	}

	public int getTripId() {
		return this.tripId;
	}
	@Id
	@Column(name = "AVAILABLE_SEATS")
	private int seatAvailable;

	public void setSeatAvailable(int value) {
		this.seatAvailable = value;
	}


	public int getSeatAvailable() {
		return this.seatAvailable;
	}
	@Id
	@Column(name = "DESTINATION")
	private String destination;

	public void setDestination(String value) {
		this.destination = value;
	}


	public String getDestination() {
		return this.destination;
	}

	@Id
	@Column(name = "DEPARTURE_TIME")
	private String departureTime;

	public void setDepartureTime(String value) {
		this.departureTime = value;
	}


	public String getDepartureTime() {
		return this.departureTime;
	}

	@Id
	@Column(name = "DEPARTURE_LOCATION")
	private String departureLocation;

	public void setDepartureLocation(String value) {
		this.departureLocation = value;
	}


	public String getDepartureLocation() {
		return this.departureLocation;
	}

	@Id
	@Column(name = "DISTANCE")
	private int distance;

	public void setDistance(int value) {
		this.distance = value;
	}


	public int getDistance() {
		return this.distance;
	}

	/**
	 * <pre>
	 *           1..1     0..*
	 * Trip ------------------------- Driver
	 *           trip        &lt;       driver
	 * </pre>
	 */
	@Id
	@Column(name = "DRIVER")
	private Set<Driver> driver;


	public Set<Driver> getDriver() {
		if (this.driver == null) {
			this.driver = new HashSet<Driver>();
		}
		return this.driver;
	}

	/**
	 * <pre>
	 *           1..1     0..*
	 * Trip ------------------------- Passenger
	 *           trip        &lt;       passenger
	 * </pre>
	 */
	@Id
	@Column(name = "PASSENGER")
	private Set<Passenger> passenger;


	public Set<Passenger> getPassenger() {
		if (this.passenger == null) {
			this.passenger = new HashSet<Passenger>();
		}
		return this.passenger;
	}

}
