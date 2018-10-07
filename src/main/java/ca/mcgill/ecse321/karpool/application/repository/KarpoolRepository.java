package ca.mcgill.ecse321.karpool.application.repository;

import java.util.HashSet;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.karpool.application.Passenger;
import ca.mcgill.ecse321.karpool.application.Rating;
import ca.mcgill.ecse321.karpool.model.Trip;
import ca.mcgill.ecse321.karpool.model.User;

public class KarpoolRepository {
	
	@Autowired 
	EntityManager entityManager;
	
	@Transactional
	public User createUser(String name, String email, String password, String phoneNumber, Rating rating, Boolean criminalRecord) {
		User user = new User();
		user.setName(name);
		user.setEmail(email);
		user.setPassword(password);
		user.setPhoneNumber(phoneNumber);
		user.setRating(null);
		entityManager.persist(user);
		return user;
	}
	
	@Transactional
	public User getUser(String email) {
		User user = entityManager.find(User.class, email);
		return user;
	}
	
	
	@Transactional
	public Trip createTrip(String destination, String departureTime, String departureLocation, int seatAvailable) {
		Trip trip = new Trip();
		trip.setDestination(destination);
		trip.setDepartureTime(departureTime);
		trip.setDepartureLocation(departureLocation);
		trip.setSeatAvailable(seatAvailable);
		return trip; 	
	}
	@Transactional
	public void closeTrip(Trip trip) {
		
		trip.setDestination(null);
		trip.setDepartureTime(null);
		trip.setDepartureLocation(null);
		trip.setSeatAvailable(0);
	
		 	
	}
	
//	@Transactional
//	public boolean addPassenger(Passenger passenger, Trip trip) {
//		
//		boolean wasAdded = false;
//		HashSet <Passenger> passengers;
//		if (passengers.contains(passenger)) {
//			return false;
//		}
//		
//		
//		else {
//			trip.getPassenger();
//		}
//		
//		wasAdded = true;
//		return wasAdded;
//		
//	}
	

}
