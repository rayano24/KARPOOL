package ca.mcgill.ecse321.karpool.application.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.karpool.application.model.*;

@Repository
public class KarpoolRepository 
{
	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public EndUser createUser(String name, String email, String password, String phoneNumber, Rating rating, boolean criminalRecord){
		EndUser user = new EndUser();
		user.setName(name);
		user.setEmail(email);
		user.setPassword(password);
		user.setPhoneNumber(phoneNumber);
		user.setRating(rating);
		user.setRecord(criminalRecord);
		entityManager.persist(user);
		return user;
	}

	@Transactional
	public EndUser getUser(String name) {
		EndUser user = entityManager.find(EndUser.class, name);
		return user;
	}
	
	@Transactional
	public List<String> getAllUsers() 
	{
		Query q = entityManager.createNativeQuery("SELECT name FROM end_user");
		@SuppressWarnings("unchecked")
		List<String> users = q.getResultList();
		return users;
	}

	@Transactional
	public Trip createTrip(String destination, String departureTime, String departureLocation, int seatAvailable) {
		Trip trip = new Trip();
		trip.setDestination(destination);
		trip.setDepartureTime(departureTime);
		trip.setDepartureLocation(departureLocation);
		trip.setSeatAvailable(seatAvailable);
		entityManager.persist(trip);
		return trip;
	}
	
	@Transactional
	public List<Integer> getTrips(String depart, String dest)
	{
		Query q = entityManager.createNativeQuery("SELECT trip_id FROM trip WHERE departure_location= :departure AND destination= :destination");
		q.setParameter("departure", depart);
		q.setParameter("destination", dest);
		@SuppressWarnings("unchecked")
		List<Integer> trips = q.getResultList();
		return trips;
	}
	
	@Transactional
	public List<Integer> getAllTrips() 
	{
		Query q = entityManager.createNativeQuery("SELECT trip_id FROM trip");
		@SuppressWarnings("unchecked")
		List<Integer> trips = q.getResultList();
		return trips;
	}
	
	@Transactional
	public Trip getSpecificTrip(int id)
	{
		Trip t = entityManager.find(Trip.class, id);
		return t;
	}
	
	@Transactional
	public List<Integer> getSortedTripsTime(String start, String finish) 
	{
		Query q = entityManager.createNativeQuery("SELECT trip_id FROM trip WHERE departure_location= :departure AND destination= :destination ORDER BY departure_time");
		q.setParameter("departure", start);
		q.setParameter("destination", finish);
		@SuppressWarnings("unchecked")
		List<Integer> trips = q.getResultList();
		return trips;
	}
	
	@Transactional
	public void closeTrip(int tripID)
	{
		Trip trip = entityManager.find(Trip.class, tripID);
		trip.setDestination(null);
		trip.setDepartureTime(null);
		trip.setDepartureLocation(null);
		trip.setSeatAvailable(0);
		entityManager.persist(trip);
	}
	
	@Transactional
	public Driver createDriver(Car car, Trip trip)
	{
		Driver d = new Driver();
		d.setCar(car);
		d.setTrip(trip);
		entityManager.persist(d);
		return d;
	}
	
	@Transactional
	public Trip addPassenger(Passenger passenger, Trip trip)
	{
		trip.getPassenger().add(passenger);
		trip.setSeatAvailable(trip.getSeatAvailable()-1);
		entityManager.persist(trip);
		return trip;
	}

}
