package ca.mcgill.ecse321.karpool.application.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import java.util.Set;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.util.HashSet;


@Entity
@Table(name="trip")
public class Trip {

	
	private int tripId;

	public void setTripId(int value) {
		this.tripId = value;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	public int getTripId() {
		return this.tripId;
	}

	private int seatAvailable;

	public void setSeatAvailable(int value) {
		this.seatAvailable = value;
	}

	public int getSeatAvailable() {
		return this.seatAvailable;
	}

	private String destination;

	public void setDestination(String value) {
		this.destination = value;
	}

	public String getDestination() {
		return this.destination;
	}

	private String departureTime;

	public void setDepartureTime(String value) {
		this.departureTime = value;
	}

	public String getDepartureTime() {
		return this.departureTime;
	}

	private String departureLocation;

	public void setDepartureLocation(String value) {
		this.departureLocation = value;
	}

	public String getDepartureLocation() {
		return this.departureLocation;
	}

	private int distance;

	public void setDistance(int value) {
		this.distance = value;
	}

	public int getDistance() {
		return this.distance;
	}

	private Driver driver;

	public void setDriver(Driver value) 
	{
		this.driver = value;
	}

	@ManyToOne
	@JoinColumn
	public Driver getDriver() {
		return this.driver;
	}
	
	/**
	 * <pre>
	 *           1..1     0..*
	 * Trip ------------------------- Passenger
	 *           trip        &lt;       passenger
	 * </pre>
	 */

	private Set<Passenger> passenger;

	@Transient
	@JoinColumn 
	@OneToMany 
	public Set<Passenger> getPassenger() {
		if (this.passenger == null) {
			this.passenger = new HashSet<Passenger>();
		}
		return this.passenger;
	}

}