package ca.mcgill.ecse321.karpool.application.controller;

import java.util.*;
import ca.mcgill.ecse321.karpool.application.model.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.karpool.application.repository.*;

@RestController
//@CrossOrigin
public class KarpoolController {

	public static final String ERROR_NOT_FOUND_MESSAGE = "NOT FOUND";

	@Autowired
	KarpoolRepository repository;

	//TODO: Password can't just be read as a regular String. NEEDS some form of encryption.

	@RequestMapping("/")
	public String greeting()
	{
		return "Hello world!";
	}

	@PostMapping("/{name}")
	public String greeting(@PathVariable("name")String name)
	{
		if(name == null)
		{
			return "Hello world!";
		}
		else
		{
			return "Hello, " + name + "!";
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////                                                                   /////////////////
	/////////////////                     DRIVER CONTROLLER                             /////////////////
	/////////////////                                                                   /////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Creates a driver via the createDriver method from KarpoolRepository. Performs parameter validation
	 * 
	 * @param name
	 * @param email
	 * @param password
	 * @param phone
	 * @return the driver that is created, null if there was an error
	 */
	@PostMapping("/drivers/{name}/{email}/{password}/{phone}/{record}")
	public Driver createDriver(@PathVariable("name") String name, @PathVariable("email") String email, @PathVariable("password") String password, 
			@PathVariable("phone") String phone, @PathVariable("record") boolean criminalRecord)
	{
		Driver d = null;
		try 
		{
			if(phone.length() == 10) 
			{
				Long.parseLong(phone);
				try 
				{
					if(email.indexOf("@")>=0 && email.indexOf(".")>=0)
					{
						try
						{
							if(password.length() >= 6) 
							{
								try
								{
									if(name.length()>=3)
									{
										d = repository.createDriver(name, email, password, phone, criminalRecord);
									}
									else 
									{
										System.out.println("Your username must have 3 characters or over");
										return null;
									}
								}
								catch (NullPointerException e)
								{
									System.out.println("Please enter a name ");
									return null;
								}
							}
							else 
							{
								System.out.println("Your password must have over 8 characters");
								return null;
							}
						}
						catch (NullPointerException e)
						{
							System.out.print("Please enter a password");
							return null;
						}
					}
					else
					{
						System.out.println("Oups , this is not a valid email");
					}
				}
				catch(NullPointerException e)
				{
					System.out.println("Oups, this is not a valid email");
					return null;
				}
			}
			else 
			{
				System.out.println("Oups, this is not a valid phone number");
				return null;
			}
		}
		catch(NullPointerException e1) 
		{
			System.out.println("Exception - Null pointer");
			return null;
		}
		catch(NumberFormatException e2)
		{
			System.out.println("Exception - Number format");
			return null;
		}
		return d;
	}

	/**
	 * This method authenticates the driver user on login page
	 *
	 * @param username
	 * @param password
	 * @return TRUE if the account is authenticated
	 */
	@GetMapping("/drivers/auth/{username}/{password}")
	public Response authenticateDriver(@PathVariable("username")String username, @PathVariable("password")String password)
	{
		Response r = new Response();
		try {
			Driver driver = repository.getDriver(username);
			if(driver.getPassword().equals(password))
			{
				r.setResponse(true);
				r.setError(null);
			}
			else
			{
				r.setResponse(false);
				r.setError("Wrong username or password");
			}	
		}
		catch(NullPointerException e) {
			r.setResponse(false);
			r.setError("Error - Attempted to authenticate null driver");
		}
		return r;
	}

	/**
	 * searches for a driver with given name
	 *
	 * @param name
	 * @return the queried driver
	 */
	@GetMapping("/drivers/{name}")
	public Driver queryDriver(@PathVariable("name")String name)
	{
		Driver driver = repository.getDriver(name);
		if(driver == null)
		{
			System.out.println("No driver with that name in the database");
			return null;
		}
		return driver;
	}

	/**
	 * lists all drivers in the database
	 * 
	 * @return list of drivers
	 */
	@GetMapping("/drivers/all")
	public List<Driver> listAllDrivers()
	{
		List<String> drivers = repository.getAllDrivers();
		List<Driver> fullDriver = new ArrayList<Driver>();
		for(String d: drivers)
		{
			fullDriver.add(repository.getDriver(d));
		}
		if(fullDriver.isEmpty())
		{
			System.out.println("There are no drivers in the database");
			return null;
		}
		return fullDriver;
	}

	@GetMapping("/trips/drivers/{name}")
	public List<Trip> tripsForDriver(@PathVariable("name") String name)
	{
		List<Integer> trip = repository.getTripForDriver(name);
		List<Trip> fullTrip = new ArrayList<Trip>();
		for(int t: trip)
		{
			fullTrip.add(repository.getSpecificTrip(t));
		}

		if(fullTrip.isEmpty())
		{
			System.out.println("There are no trips for this driver");
			return null;
		}		
		return fullTrip;
	}

	/**
	 * Rate the driver
	 * 
	 * @param name
	 * @param rating
	 */
	@PostMapping("/drivers/rate/{name}/{rating}")
	public List<Double> rateDriver(@PathVariable("name") String name,@PathVariable("rating") double rating)
	{
		//need to check if rating is a valid rating
		try {
			
			Driver d = repository.getDriver(name);
			repository.addDriverRating(d, rating);
			return d.getRatings();
		}
		catch (NullPointerException e) {
			System.out.println(ERROR_NOT_FOUND_MESSAGE);
		}
		return null;
	}
	
	/**
	 * 
	 * @param name
	 */
	@GetMapping("/drivers/rate/{name}")
	public Response getAvgRating(@PathVariable("name") String name)
	{ 
		double r = 0.0;
		Response resp = new Response();
		try {
			Driver d = repository.getDriver(name);
			List<Double> ratings = d.getRatings();
			int rNum = ratings.size();
			double rSum = 0.0;
			for(double rate: ratings)
			{
				rSum+=rate;
			}
			r = rSum/rNum;
			resp.setRating(r);
		} catch(NullPointerException e1) {
			resp.setError("Attempted to get rating of nonexistent driver");
			resp.setRating(-1.0);
		} catch(NumberFormatException e2) {
			resp.setError("Divided by zero rating");
			resp.setRating(-1.0);
		}
		return resp;
	}
	
	@GetMapping("/drivers/active/all")
	public List<Driver> getActiveDrivers()
	{
		List<Driver> allDrivers = listAllDrivers();
		List<Driver> actDrivers = new ArrayList<Driver>();
		boolean active = true;
		for(Driver d: allDrivers)
		{
			Set<Trip> trips = d.getTrips();
			if(trips.isEmpty())
			{
				active = false;
			}
			else
			{
				for(Trip t: trips)
				{
					if(t.isTripComplete())
					{
						active = false;
					}
				}
			}
			if(active)
			{
				actDrivers.add(d);
			}
			active = true;
		}
		return actDrivers;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////                                                                   /////////////////
	/////////////////                    PASSENGER CONTROLLER                           /////////////////
	/////////////////                                                                   /////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Creates a passenger via the createPassenger method from KarpoolRepository. Performs parameter validation
	 * 
	 * @param name
	 * @param email
	 * @param password
	 * @param phone
	 * @param criminalRecord
	 * @return the created passenger, or null if there was an error
	 */
	@PostMapping("/passengers/{name}/{email}/{password}/{phone}/{record}")
	public Passenger createPassenger(@PathVariable("name") String name, @PathVariable("email") String email, @PathVariable("password") String password, 
			@PathVariable("phone") String phone, @PathVariable("record") boolean criminalRecord)
	{
		Passenger p = null;
		try 
		{
			if(phone.length() == 10) 
			{
				Long.parseLong(phone);
				try 
				{
					if(email.indexOf("@")>=0 && email.indexOf(".")>=0)
					{
						try
						{
							if(password.length() >= 8 ) 
							{
								try
								{
									if(name.length()>=3)
									{
										p = repository.createPassenger(name, email, password, phone, criminalRecord);
									}
									else 
									{
										System.out.println("Your username must have 3 characters or over");
										return null;
									}
								}
								catch (NullPointerException e)
								{
									System.out.println("Please enter a name ");
									return null;
								}
							}
							else 
							{
								System.out.println("Your password must have over 6 characters");
								return null;
							}
						}
						catch (NullPointerException e)
						{
							System.out.print("Please enter a password");
							return null;
						}
					}
					else
					{
						System.out.println("Oups , this is not a valid email");
					}
				}
				catch(NullPointerException e)
				{
					System.out.println("Oups, this is not a valid email");
					return null;
				}
			}
			else 
			{
				System.out.println("Oups, this is not a valid phone number");
				return null;
			}
		}
		catch(NullPointerException e1) 
		{
			System.out.println("Exception - Null pointer");
			return null;
		}
		catch(NumberFormatException e2)
		{
			System.out.println("Exception - Number format");
			return null;
		}
		return p;
	}

	/**
	 * This method authenticates the passenger user on login page
	 *
	 * @param username
	 * @param password
	 * @return TRUE if the account is authenticated
	 */
	@GetMapping("/passengers/auth/{username}/{password}")
	public Response authenticatePassenger(@PathVariable("username")String username, @PathVariable("password")String password)
	{
		Response r = new Response();
		try {
			Passenger passenger = repository.getPassenger(username);
			if(passenger.getPassword().equals(password))
			{
				r.setResponse(true);
				r.setError(null);
			}
			else
			{
				r.setResponse(false);
				r.setError("Wrong username or password");
			}	
		}
		catch(NullPointerException e) {
			r.setResponse(false);
			r.setError("Error - Attempted to authenticate null passenger");
		}
		return r;
	}

	/**
	 * searches for a passenger with given name
	 *
	 * @param name
	 * @return the queried passenger
	 */
	@GetMapping("/passengers/{name}")
	public Passenger queryPassenger(@PathVariable("name")String name)
	{
		Passenger passenger = repository.getPassenger(name);
		if(passenger == null)
		{
			System.out.println("No passenger with that name in the database");
			return null;
		}
		return passenger;
	}

	/**
	 * lists all users in the database
	 * 
	 * @return list of users
	 */
	@GetMapping("/passengers/all")
	public List<Passenger> listAllPassengers()
	{
		List<String> pass = repository.getAllPassengers();
		List<Passenger> fullPass = new ArrayList<Passenger>();
		for(String p: pass)
		{
			fullPass.add(repository.getPassenger(p));
		}
		if(fullPass.isEmpty())
		{
			System.out.println("There are no passengers in the database");
			return null;
		}
		return fullPass;
	}

	/**
	 * lists all trips associated to particular passenger
	 * 
	 * @return list of trips for passenger
	 */
	@GetMapping("/trips/passengers/{name}")
	public Set<Trip> tripsForPassenger(@PathVariable("name") String name)
	{
		Set<Trip> t = repository.getTripsForPassenger(name);

		if(t == null)
		{
			System.out.println("There are no trips for this passenger");
			return null;
		}		
		return t;
	}

	/**
	 * This method allows for new passengers to be added to a specific 
	 * trip taking place. It checks to see if the passenger is already 
	 * signed up for this trip, if not it adds to passenger to the trip.
	 * @param passenger
	 * @param trip
	 * @return
	 */
	@PostMapping("/trips/{trip}/add/{name}")
	public Passenger addPassenger(@PathVariable("trip") int tripID, @PathVariable("name") String name) 
	{
		Trip t = repository.getSpecificTrip(tripID);
		Passenger p = repository.getPassenger(name);
		if (t.getSeatAvailable() <= 0) {
			System.out.println("No seats available");
			return null;
		}
		else if (repository.checkPassengerInTrip(t, p)) 
		{
			System.out.println("You are already on this trip");
			return null;
		}
		else if (t.isTripComplete() == true) {
			System.out.println("This trip is already complete");
			return null;
		}
		else {
			repository.addPassengerToTrip(p, t);
		}	
		return p;
	}
	
	@PostMapping("/trips/{trip}/remove/{name}")
	public Passenger removePassenger(@PathVariable("trip") int tripID, @PathVariable("name") String name)
	{
		Trip t = repository.getSpecificTrip(tripID);
		Passenger p = repository.getPassenger(name);
		if(repository.checkPassengerInTrip(t, p))
		{
			repository.removePassengerFromTrip(p, t);
		}
		else
		{
			System.out.println("You cannot leave a trip that you are not a part of");
			return null;
		}
		
		return p;
	}
	
	@GetMapping("/trips/{trip}/passengers")
	public Set<Passenger> getPassengersInTrip(@PathVariable("trip") int tripID)
	{
		Trip t = repository.getSpecificTrip(tripID);
		Set<Passenger> p = repository.getPassengersInTrip(t);
		if(p.isEmpty())
		{
			System.out.println("There are no passengers on this trip");
		}	
		
		return p;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////                                                                   /////////////////
	/////////////////                      TRIPS CONTROLLER                             /////////////////
	/////////////////                                                                   /////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * creates trip with given parameters
	 * 
	 * @param departureLocation
	 * @param destination
	 * @param seatAvailable
	 * @param departureTime
	 * @return the created trip
	 * @throws ParseException 
	 */
	@PostMapping("/trips/{driver}/{location}/{destination}/{seats}/{time}/{date}/{price}")
	public Trip createTrip(@PathVariable("driver") String name, @PathVariable("location") String departureLocation, @PathVariable("destination") String destination, 
			@PathVariable("seats") int seatAvailable, @PathVariable("time") String departureTime, @PathVariable("date") String departureDate, @PathVariable("price") int price) throws ParseException
	{
		Date date = new Date();
		Date time = new Date();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date dateInput = sdf.parse(departureDate);
		String date1 = sdf.format(dateInput);        
		String date2 = sdf.format(date);

		SimpleDateFormat sdf2 = new SimpleDateFormat("HHmm");
		Date timeInput = sdf2.parse(departureTime);
		String time1 = sdf2.format(timeInput);
		String time2 = sdf2.format(time);

		try 
		{
			if(seatAvailable == 0) {

				System.out.println("Must have one or more seats available");
				return null;
			} 

			//compares system date to departureDate
			else if ((date1.compareTo(date2)) < 0) {
				System.out.println("Cannot set a date that has already passed");
				return null;
			}

			//compares system time to departureTime
			else if ((date1.compareTo(date2)) == 0) {

				if ((time1.compareTo(time2)) < 0) {
					System.out.println(time1);
					System.out.println(time2);
					System.out.println("Cannot set a time that has already passed");
					return null;
				}
			}
		} 
		catch(NullPointerException |  NumberFormatException e) 
		{
			System.out.println("Exception - Invalid seat number");
			return null;
		}

		Driver d = repository.getDriver(name);

		Trip trip = repository.createTrip(d, destination, departureTime, departureDate, departureLocation, seatAvailable, price);
		return trip;
	}

	/**
	 * finds a trip that matches departure location and destination, with matching number of seats
	 * 
	 * @param departureLocation
	 * @param destination
	 * @param seatAvailable
	 * @return queried trip
	 */
	@GetMapping("/trips/{location}/{destination}")
	public List<Trip> queryTrip(@PathVariable("location") String departureLocation, 
			@PathVariable("destination") String destination)
	{
		List<Integer> trips = repository.getTrips(departureLocation, destination);
		List<Trip> fullTrip = new ArrayList<Trip>();
		for(int t: trips)
		{
			Trip tFull = repository.getSpecificTrip(t);
			if(tFull.getSeatAvailable()>0 && !tFull.isTripComplete())
			{
				fullTrip.add(tFull);
			}
		}
		if(fullTrip.isEmpty())
		{
			System.out.println("There are no trips that match your query");
			return null;
		}
		return fullTrip;
	}

	/**
	 * lists all trips matching the query in ascending order of times
	 * 
	 * @return sorted list of trips
	 */
	@GetMapping("/trips/{location}/{destination}/date")
	public List<Trip> listTripsAscendingDate(@PathVariable("location") String departureLocation, 
			@PathVariable("destination") String destination)
	{
		List<Integer> trips = repository.getSortedTripsTime(departureLocation, destination);
		List<Trip> fullTrip = new ArrayList<Trip>();
		for(int t: trips)
		{
			Trip tFull = repository.getSpecificTrip(t);
			if(tFull.getSeatAvailable()>0 && !tFull.isTripComplete())
			{
				fullTrip.add(tFull);
			}
		}
		if(fullTrip.isEmpty())
		{
			System.out.println("There are no trips that match your query");
			return null;
		}
		return fullTrip;
	}
	
	@GetMapping("/trips/{location}/{destination}/price")
	public List<Trip> listTripsAscendingPrice(@PathVariable("location") String departureLocation, 
			@PathVariable("destination") String destination)
	{
		List<Integer> trips = repository.getSortedTripsPrice(departureLocation, destination);
		List<Trip> fullTrip = new ArrayList<Trip>();
		for(int t: trips)
		{
			Trip tFull = repository.getSpecificTrip(t);
			if(tFull.getSeatAvailable()>0 && !tFull.isTripComplete())
			{
				fullTrip.add(tFull);
			}
		}
		if(fullTrip.isEmpty())
		{
			System.out.println("There are no trips that match your query");
			return null;
		}
		return fullTrip;
	}
	
	/**
	 * finds a trip that matches departure location and destination, with matching number of seats
	 * 
	 * @param departureLocation
	 * @param destination
	 * @param seatAvailable
	 * @return queried trip
	 */
	@GetMapping("/trips/{location}/{destination}/{passenger}")
	public List<Trip> queryTrip(@PathVariable("location") String departureLocation, 
			@PathVariable("destination") String destination, @PathVariable("destination") String name)
	{
		List<Integer> trips = repository.getTrips(departureLocation, destination);
		List<Trip> fullTrip = new ArrayList<Trip>();
		Passenger p = repository.getPassenger(name);
		for(int t: trips)
		{
			Trip tFull = repository.getSpecificTrip(t);
			if(tFull.getSeatAvailable()>0 && !tFull.isTripComplete() && !tFull.getPassenger().contains(p))
			{
				fullTrip.add(tFull);
			}
		}
		if(fullTrip.isEmpty())
		{
			System.out.println("There are no trips that match your query");
			return null;
		}
		return fullTrip;
	}
	
	/**
	 * lists all trips matching the query in ascending order of times
	 * 
	 * @return sorted list of trips
	 */
	@GetMapping("/trips/{location}/{destination}/{passenger}/date")
	public List<Trip> listTripsAscendingDate(@PathVariable("location") String departureLocation, 
			@PathVariable("destination") String destination, @PathVariable("passenger") String name)
	{
		List<Integer> trips = repository.getSortedTripsTime(departureLocation, destination);
		List<Trip> fullTrip = new ArrayList<Trip>();
		Passenger p = repository.getPassenger(name);
		for(int t: trips)
		{
			Trip tFull = repository.getSpecificTrip(t);
			if(tFull.getSeatAvailable()>0 && !tFull.isTripComplete() && !tFull.getPassenger().contains(p))
			{
				fullTrip.add(tFull);
			}
		}
		if(fullTrip.isEmpty())
		{
			System.out.println("There are no trips that match your query");
			return null;
		}
		return fullTrip;
	}
	
	@GetMapping("/trips/{location}/{destination}/{passenger}/price")
	public List<Trip> listTripsAscendingPrice(@PathVariable("location") String departureLocation, 
			@PathVariable("destination") String destination, @PathVariable("passenger") String name)
	{
		List<Integer> trips = repository.getSortedTripsPrice(departureLocation, destination);
		List<Trip> fullTrip = new ArrayList<Trip>();
		Passenger p = repository.getPassenger(name);
		for(int t: trips)
		{
			Trip tFull = repository.getSpecificTrip(t);
			if(tFull.getSeatAvailable()>0 && !tFull.isTripComplete() && !tFull.getPassenger().contains(p))
			{
				fullTrip.add(tFull);
			}
		}
		if(fullTrip.isEmpty())
		{
			System.out.println("There are no trips that match your query");
			return null;
		}
		return fullTrip;
	}


	/**
	 * lists all trips in the database
	 * 
	 * @return list of trips
	 */
	@GetMapping("/trips/all")
	public List<Trip> listAllTrips()
	{
		List<Integer> trips = repository.getAllTrips();
		List<Trip> fullTrip = new ArrayList<Trip>();
		for(int t: trips)
		{
			fullTrip.add(repository.getSpecificTrip(t));
		}
		if(fullTrip.isEmpty())
		{
			System.out.println("There are no trips in the databse");
			return null;
		}		
		return fullTrip;
	}
	
	@GetMapping("/trips/{tripID}")
	public Trip getTripInfo(@PathVariable("tripID")int tripID)
	{
		Trip t = repository.getSpecificTrip(tripID);
		return t;
	}
	
	/**
	 * lists all trips in the database
	 * 
	 * @return list of trips
	 */
	@GetMapping("/trips/open/all")
	public List<Trip> listAllOpenTrips()
	{
		List<Integer> trips = repository.getAllTrips();
		List<Trip> fullTrip = new ArrayList<Trip>();
		for(int t: trips)
		{
			Trip tempTrip = repository.getSpecificTrip(t);
			if(!tempTrip.isTripComplete())
			{
				fullTrip.add(tempTrip);
			}
		}
		if(fullTrip.isEmpty())
		{
			System.out.println("There are no trips in the database");
			return null;
		}		
		return fullTrip;
	}

	@PostMapping("/trips/{tripID}/date/{date}")
	public Response modifyTripDate(@PathVariable("tripID")int tripID, @PathVariable("date")String departureDate) throws ParseException
	{
		
		Trip t = repository.getSpecificTrip(tripID);
		Response r = new Response();
		
		if(t.getPassenger().size() == 0) {
		

		Date date = new Date();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date dateInput = sdf.parse(departureDate);
		String date1 = sdf.format(dateInput);        
		String date2 = sdf.format(date);

		try {
			if ((date1.compareTo(date2)) < 0) {
				r.setError("Cannot set a date that has already passed");
				r.setResponse(false);
				return r;
				
			}
			
			else {	
			repository.modifyDepartureDate(t, departureDate);
			r.setResponse(true);
			return r;
		}

	} catch (NullPointerException e) {
		System.out.println(ERROR_NOT_FOUND_MESSAGE);
	}
		r.setResponse(false);
				return r;
		}
		
		else {
			r.setError("Cannot Modify a trip once passengers have join");
			r.setResponse(false);
			return r;
		}
	}

	@PostMapping("/trips/{tripID}/time/{time}")
	public Response modifyTripTime(@PathVariable("tripID")int tripID, @PathVariable("time")String departureTime) throws ParseException 
	{
		Response r = new Response();
		
		Trip t = repository.getSpecificTrip(tripID);
		

		Date time = new Date();
		Date date = new Date();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date dateInput = sdf.parse(t.getDepartureDate());
		String date1 = sdf.format(dateInput);        
		String date2 = sdf.format(date);

		
		SimpleDateFormat sdf2 = new SimpleDateFormat("HHmm");
		Date timeInput = sdf2.parse(departureTime);
		String time1 = sdf2.format(timeInput);
		String time2 = sdf2.format(time);
		
		if(t.getPassenger().size() == 0) {
		
		try {
		
		if((date1.compareTo(date2)) == 0) {  
			
			if ((time1.compareTo(time2)) < 0) {

				r.setError("Cannot set a time that has already passed");
				r.setResponse(false);
				return r;
			}
		}
			
		else if ((date1.compareTo(date2)) < 0) {
				r.setError("Cannot set time for a trip that has already passed");
				r.setResponse(false);
				return r;
			}
			
		else {
			repository.modifyDepartureTime(t, departureTime);
			r.setResponse(true);
			return r;
			
			}
		
			
		} catch (NullPointerException e) {
			System.out.println(ERROR_NOT_FOUND_MESSAGE);
		}
			r.setResponse(false);
				return r;
		}
				
		else {
			r.setError("Cannot Modify a trip once passengers have join");
			r.setResponse(false);
			return r;
		}

	}

	@PostMapping("/trips/{tripID}/triplocation/{location}")
	public Response modifyTripLocation(@PathVariable("tripID")int tripID, @PathVariable("location")String departureLocation) 
	{
		Response r = new Response();
		Trip t = repository.getSpecificTrip(tripID);

		if(t.getPassenger().size() == 0) {
		try {
			repository.modifyTripLocation(t, departureLocation);
			r.setResponse(true);
			return r;

		} catch (NullPointerException e) {
			System.out.println(ERROR_NOT_FOUND_MESSAGE);
		}
			r.setResponse(false);
				return r;
		}
		
		else {
			r.setError("Cannot Modify a trip once passengers have join");
			r.setResponse(false);
			return r;
		}
		
		
	}

	@PostMapping("/trips/{tripID}/tripdestination/{destination}")
	public Response modifyTripDestination(@PathVariable("tripID")int tripID, @PathVariable("destination")String destination) 
	{
		Trip t = repository.getSpecificTrip(tripID);
		
		Response r = new Response();
		
		if(t.getPassenger().size() == 0) {
		try {
			
			repository.modifyTripDestination(t, destination);
			r.setResponse(true);
			return r;

		} catch (NullPointerException e) {
			System.out.println(ERROR_NOT_FOUND_MESSAGE);
		}
			r.setResponse(false);
				return r;
		}
		
		else {
			r.setError("Cannot Modify a trip once passengers have join");
			r.setResponse(false);
			return r;
		}
	}

	@PostMapping("/trips/{tripID}/tripprice/{price}")
	public Response modifyTripPrice(@PathVariable("tripID")int tripID, @PathVariable("price")int price) 
	{
		Response r = new Response();
		Trip t = repository.getSpecificTrip(tripID);
		
		
		if(t.getPassenger().size() == 0) {

		try {
			repository.modifyTripPrice(t, price);
			r.setResponse(true);
			return r;

		} catch (NullPointerException e) {
			System.out.println(ERROR_NOT_FOUND_MESSAGE);
		}
			r.setResponse(false);
				return r;
		}
		
		else {
			r.setError("Cannot Modify a trip once passengers have join");
			r.setResponse(false);
			return r;
		}
		
	}

	@PostMapping("/trips/{tripID}/seats/{seats}")
	public Response modifyTripSeats(@PathVariable("tripID")int tripID, @PathVariable("seats")int seatAvailable) 
	{
		Response r = new Response();
		Trip t = repository.getSpecificTrip(tripID);

		if(t.getPassenger().size() == 0) {
		try {
			repository.modifySeatAvailable(t, seatAvailable);
			r.setResponse(true);
			return r;


		} catch (NullPointerException e) {
			System.out.println(ERROR_NOT_FOUND_MESSAGE);
		}
			r.setResponse(false);
				return r;
		}
				
		else {
			r.setError("Cannot Modify a trip once passengers have join");
			r.setResponse(false);
			return r;
		}
	}
	

	/**
	 * This method marks a trip as completed
	 * @param trip
	 * @throws ParseException 
	 */
	@PostMapping("/trips/close/{tripID}")
	public void closeTrip(@PathVariable("tripID")int tripID) throws ParseException
	{
		repository.closeTrip(tripID);
	}
	
	/**
	 * This method deletes a trip from the repository. Used when a driver deletes a trip before
	 * there are any passengers on it
	 * 
	 * @param tripID
	 * @throws ParseException 
	 */
	@PostMapping("/trips/delete/{trip}")
	public void deleteTrip(@PathVariable("trip")int tripID) throws ParseException
	{
		//TODO can only delete trip if the date hasnt passed yet
		repository.deleteTrip(tripID);
	}

	/*public float Distance (int zipcode1, int zipcode2) throws MalformedURLException, IOException
	{

        BufferedReader br = null;

        try {

            URL url = new URL("https://www.zipcodeapi.com/rest/GOhazMBKVJ2VDSEOrrkf0sswW4D5c4NYOjZi2mGTjf2wuvgvTkUj5L1KpR2GkRRI/distance.json/" + zipcode1 + "/" +zipcode2 +"/km");
            br = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;

            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }

            String RoughDistance = sb.toString();
            if (RoughDistance.charAt(2) == 'e') {
            	return 0;
            }
            String intValue = RoughDistance.replaceAll("[^0-9, .]", "");
            float distance = Float.parseFloat(intValue);
            return distance;

        } finally {

            if (br != null) {
                br.close();
            }
        }
    }*/
}

