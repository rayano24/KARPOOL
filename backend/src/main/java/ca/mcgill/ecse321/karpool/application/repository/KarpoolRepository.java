package ca.mcgill.ecse321.karpool.application.repository;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.karpool.application.model.*;

@Repository
public class KarpoolRepository 
{
	@Autowired
	private EntityManager entityManager;

	@Transactional
	public User createUser(String name, String email, String password, String phoneNumber, Rating rating, boolean criminalRecord){
		User user = new User(name, email, phoneNumber, password);
		entityManager.persist(user);
		return user;
	}

	@Transactional
	public User getUser(String name) {
		User user = entityManager.find(User.class, name);
		return user;
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
	public void closeTrip(Trip trip)
	{
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
