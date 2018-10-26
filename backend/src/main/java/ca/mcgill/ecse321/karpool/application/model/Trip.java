package ca.mcgill.ecse321.karpool.application.model;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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

	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private int tripId;

	public void setTripId(int value) {
		this.tripId = value;
	}

	@Id
//	@Column(name="trip_id")
	public int getTripId() {
		return this.tripId;
	}

	private int seatAvailable;

	public void setSeatAvailable(int value) {
		this.seatAvailable = value;
	}

//	@Column(name="seat_available")
	public int getSeatAvailable() {
		return this.seatAvailable;
	}

	private String destination;

	public void setDestination(String value) {
		this.destination = value;
	}

//	@Column(name="destination")
	public String getDestination() {
		return this.destination;
	}

	private String departureTime;

	public void setDepartureTime(String value) {
		this.departureTime = value;
	}

//	@Column(name="departure_time")
	public String getDepartureTime() {
		return this.departureTime;
	}

	private String departureLocation;

	public void setDepartureLocation(String value) {
		this.departureLocation = value;
	}

//	@Column(name="departure_location")
	public String getDepartureLocation() {
		return this.departureLocation;
	}

	private int distance;

	public void setDistance(int value) {
		this.distance = value;
	}

//	@Column(name="distance")
	public int getDistance() {
		return this.distance;
	}

	private Driver driver;

	public void setDriver(Driver value) 
	{
		this.driver = value;
	}

	@ManyToOne
	@JoinColumn //(name="driver")
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
	@JoinColumn //(name = "passenger")
	@OneToMany //(targetEntity=Passenger.class, mappedBy="trip")
	public Set<Passenger> getPassenger() {
		if (this.passenger == null) {
			this.passenger = new HashSet<Passenger>();
		}
		return this.passenger;
	}

}
