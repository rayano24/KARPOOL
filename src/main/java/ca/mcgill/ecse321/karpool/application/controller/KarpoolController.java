package ca.mcgill.ecse321.karpool.application.controller;

import java.util.*;
import ca.mcgill.ecse321.karpool.application.model.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.karpool.application.repository.*;

/**
 * Controller class. Organized into three sections: Driver controller, Passenger controller and Trips controller.
 * 
 */
@RestController
@CrossOrigin
public class KarpoolController {

	public static final String ERROR_NOT_FOUND_MESSAGE = "NOT FOUND";

	@Autowired
	KarpoolRepository repository;

	/**
	 * Greeting on home page of the website
	 * @return hello world
	 */
	@RequestMapping("/")
	public String greeting()
	{
		return "Hello world!";
	}

	/**
	 * Name specific greeting
	 * @param name
	 * @return hello + name
	 */
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
	 * @param phone 10 digits
	 * @param criminalRecord true or false
	 * @return driver if successfully created, null driver with error message string if error
	 */
	@PostMapping("/drivers/{name}/{email}/{password}/{phone}/{record}")
	public Driver createDriver(@PathVariable("name") String name, @PathVariable("email") String email, @PathVariable("password") String password, 
			@PathVariable("phone") String phone, @PathVariable("record") boolean criminalRecord)
	{
		Driver d = new Driver();;
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
										d.setError("Your username must have 3 characters or over");
										return d;
									}
								}
								catch (NullPointerException e)
								{
									d.setError("Please enter a name ");
									return d;
								}
							}
							else 
							{
								d.setError("Your password must have over 8 characters");
								return d;
							}
						}
						catch (NullPointerException e)
						{
							d.setError("Please enter a password");
							return d;
						}
					}
					else
					{
						d.setError("Oups , this is not a valid email");
					}
				}
				catch(NullPointerException e)
				{
					d.setError("Oups, this is not a valid email");
					return d;
				}
			}
			else 
			{
				d.setError("Oups, this is not a valid phone number");
				return d;
			}
		}
		catch(NullPointerException e1) 
		{
			d.setError("Exception - Null pointer");
			return d;
		}
		catch(NumberFormatException e2)
		{
			d.setError("Exception - Number format");
			return d;
		}
		return d;
	}

	/**
	 * This method authenticates the driver on login page
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

	/**
	 * Gets trips that are assigned to a specific driver
	 * @param name
	 * @return list of trips
	 */
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
	 * Gets the average rating of a driver using the list of all ratings
	 * 
	 * @param name name of driver
	 * @return Response with set rating
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
			if(rNum == 0)
			{
				throw new ArithmeticException();
			}
			r = rSum/rNum;
			resp.setRating(r);
		} catch(NullPointerException e1) {
			resp.setError("Attempted to get rating of nonexistent driver");
			resp.setRating(-1.0);
		} catch(ArithmeticException e2) {
			resp.setError("Divided by zero rating");
			resp.setRating(-1.0);
		}
		return resp;
	}

	/**
	 * Private method to calculate average rating with input of driver instead of name 
	 * 
	 * @param d driver
	 * @return double
	 */
	private double getAvgRating(Driver d)
	{ 
		double r = 0.0;
		List<Double> ratings = d.getRatings();
		int rNum = ratings.size();
		double rSum = 0.0;
		for(double rate: ratings)
		{
			rSum+=rate;
		}
		try {
			r = rSum/rNum;
		} catch(ArithmeticException e) {
			r = -1.0;
			System.out.println("Divided by zero rating");
		}
		return r;
	}

	/**
	 * Gets the list of all active drivers. Active drivers are defined as having open trips/ads
	 * 
	 * @return list of all active drivers
	 */
	@GetMapping("/drivers/active/all")
	public List<Driver> getActiveDrivers()
	{
		List<Driver> allDrivers = listAllDrivers();
		List<Driver> actDrivers = new ArrayList<Driver>();
		boolean active = false;
		for(Driver d: allDrivers)
		{
			Set<Trip> trips = repository.getTripForDriver(d);
			if(trips.isEmpty())
			{
				active = false;
			}
			else
			{
				for(Trip t: trips)
				{
					if(!t.isTripComplete())
					{
						active = true;
						break;
					}
				}
			}
			if(active)
			{
				actDrivers.add(d);
			}
		}
		return actDrivers;
	}

	/**
	 * Gets the top 3 drivers, as in the top 3 with highest ratings 
	 * 
	 * @return ordered list of top 3
	 */
	@GetMapping("/drivers/top3")
	public ArrayList<Driver> getTopDrivers()
	{
		List<Double> rate = new ArrayList<Double>();
		rate.add(0.0);
		List<Driver> allDrivers = listAllDrivers();
		ArrayList<Driver> topThree = new ArrayList<Driver>();
		Driver first = new Driver();
		first.setRatings(rate);
		Driver second = new Driver();
		second.setRatings(rate);
		Driver third = new Driver();
		third.setRatings(rate);
		for(Driver d: allDrivers)
		{
			if(getAvgRating(d) >= getAvgRating(first)) //#1
			{
				third = second;
				second = first;
				first = d;
			}
			else if(getAvgRating(d) >= getAvgRating(second)) //#2
			{
				third = second;
				second = d;
			}
			else if(getAvgRating(d) >= getAvgRating(third)) //#3
			{
				third = d;
			}
		}
		topThree.add(first);
		topThree.add(second);
		topThree.add(third);
		return topThree;
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
	 * @return created passenger if successful, null passenger with set error message if error
	 */
	@PostMapping("/passengers/{name}/{email}/{password}/{phone}/{record}")
	public Passenger createPassenger(@PathVariable("name") String name, @PathVariable("email") String email, @PathVariable("password") String password, 
			@PathVariable("phone") String phone, @PathVariable("record") boolean criminalRecord)
	{
		Passenger p = new Passenger();
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
										p.setError("Your username must have 3 characters or over");
										return p;
									}
								}
								catch (NullPointerException e)
								{
									p.setError("Please enter a name ");
									return p;
								}
							}
							else 
							{
								p.setError("Your password must have over 6 characters");
								return p;
							}
						}
						catch (NullPointerException e)
						{
							p.setError("Please enter a password");
							return p;
						}
					}
					else
					{
						p.setError("Oups , this is not a valid email");
					}
				}
				catch(NullPointerException e)
				{
					p.setError("Oups , this is not a valid email");
					return p;
				}
			}
			else 
			{
				p.setError("Oups, this is not a valid phone number");
				return p;
			}
		}
		catch(NullPointerException e1) 
		{
			p.setError("Exception - Null pointer");
			return p;
		}
		catch(NumberFormatException e2)
		{
			p.setError("Exception - Number format");
			return p;
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
		else
		{
			List<Trip> orderedTrips = new ArrayList<Trip>();
			orderedTrips.addAll(t);	
		}
		return t;
	}

	/**
	 * This method allows for new passengers to be added to a specific 
	 * trip taking place. It checks to see if the passenger is already 
	 * signed up for this trip, if not it adds to passenger to the trip.
	 * 
	 * @param passenger
	 * @param trip
	 * @return Response
	 */
	@PostMapping("/trips/{trip}/add/{name}")
	public Response addPassenger(@PathVariable("trip") int tripID, @PathVariable("name") String name) 
	{
		Trip t = repository.getSpecificTrip(tripID);
		Passenger p = repository.getPassenger(name);
		Response error = new Response();
		if (t.getSeatAvailable() <= 0) {
			error.setError("No seats available");
			return error;
		}
		else if (repository.checkPassengerInTrip(t, p)) 
		{
			error.setError("You are already on this trip");
			return error;
		}
		else if (t.isTripComplete() == true) {
			error.setError("This trip is already complete");
			return error;
		}
		else {
			repository.addPassengerToTrip(p, t);
			error.setResponse(true);
		}	
		return error;
	}

	/**
	 * Removes passenger from a trip if said passenger and trip exist and if that passenger is actually a part of that trip
	 * 
	 * @param tripID
	 * @param name
	 * @return Response
	 */
	@PostMapping("/trips/{trip}/remove/{name}")
	public Response removePassenger(@PathVariable("trip") int tripID, @PathVariable("name") String name)
	{
		Trip t = repository.getSpecificTrip(tripID);
		Passenger p = repository.getPassenger(name);
		Response error = new Response();
		try {
			if(repository.checkPassengerInTrip(t, p))
			{
				repository.removePassengerFromTrip(p, t);
				error.setResponse(true);
			}
			else
			{
				error.setError("You cannot leave a trip that you are not a part of");
			}
		} catch(NullPointerException e){
			error.setError("There is no such trip or passenger");
		}
		return error;
	}

	/**
	 * Gets the list of passengers that have joined a specific trip
	 * @param tripID
	 * @return Set of trips
	 */
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

	/**
	 * Gets list of active passengers. Active passengers are defined as passengers 
	 * having joined a trip that is still active.
	 * 
	 * @return List of passengers
	 */
	@GetMapping("/passengers/active/all")
	public List<Passenger> getActivePassengers()
	{
		List<Passenger> allPassengers = listAllPassengers();
		List<Passenger> actPassengers = new ArrayList<Passenger>();
		boolean active = false;
		for(Passenger p: allPassengers)
		{
			Set<Trip> trips = repository.getTripsForPassenger(p);
			if(trips.isEmpty())
			{
				active = false;
			}
			else
			{
				for(Trip t: trips)
				{
					if(!t.isTripComplete()) //change this condition
					{
						active = true;
						break;
					}
				}
			}
			if(active)
			{
				actPassengers.add(p);
			}
		}
		return actPassengers;
	}

	/**
	 * Gets list of top 3 passengers. Top 3 defined by passengers having gone on the most trips
	 * 
	 * @return Ordered list of passengers
	 */
	@GetMapping("/passengers/top3")
	public ArrayList<Passenger> getTopPassengers()
	{
		Set<Trip> trip = new HashSet<Trip>();
		List<Passenger> allPassengers = listAllPassengers();
		ArrayList<Passenger> topThree = new ArrayList<Passenger>();
		Passenger first = new Passenger();
		first.setTrips(trip);
		Passenger second = new Passenger();
		second.setTrips(trip);
		Passenger third = new Passenger();
		third.setTrips(trip);
		for(Passenger p: allPassengers)
		{
			if(p.getTrips().size() >= first.getTrips().size()) //#1
			{
				third = second;
				second = first;
				first = p;
			}
			else if(p.getTrips().size() >= second.getTrips().size()) //#2
			{
				third = second;
				second = p;
			}
			else if(p.getTrips().size() >= third.getTrips().size()) //#3
			{
				third = p;
			}
		}
		topThree.add(first);
		topThree.add(second);
		topThree.add(third);
		return topThree;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////                                                                   /////////////////
	/////////////////                      TRIPS CONTROLLER                             /////////////////
	/////////////////                                                                   /////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * creates trip with given parameters
	 * @param name String name of driver
	 * @param departureLocation
	 * @param destination
	 * @param seatAvailable int 
	 * @param departureTime format is HHmm
	 * @param departureDate format is yyyyMMdd
	 * @param price int
	 * @return created trip if successful, blank trip with set error message if error
	 * @throws ParseException to make sure phone number is actually a number
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

		Trip t = new Trip();

		try 
		{
			if(seatAvailable == 0) {

				t.setError("Must have one or more seats available");
				return t;
			} 

			//compares system date to departureDate
			else if ((date1.compareTo(date2)) < 0) {
				t.setError("Cannot set a date that has already passed");
				return t;
			}

			//compares system time to departureTime
			else if ((date1.compareTo(date2)) == 0) {

				if ((time1.compareTo(time2)) < 0) {
					System.out.println(time1);
					System.out.println(time2);
					t.setError("Cannot set a time that has already passed");
					return t;
				}
			}
		} 
		catch(NullPointerException |  NumberFormatException e) 
		{
			t.setError("Exception - Invalid seat number");
			return t;
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
	 * @return list of queried trip
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
	 * @param departureLocation
	 * @param destination
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

	/**
	 * lists all trips matching the query in ascending order of price
	 * 
	 * @param departureLocation
	 * @param destination
	 * @return sorted list of trips
	 */
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
	 * finds a trip that matches departure location and destination, with matching number of seats. This 
	 * query takes as input the name of the passenger that is logged into the app. It excludes trips that 
	 * the passenger has already joined in the search results. 
	 * 
	 * @param departureLocation
	 * @param destination
	 * @param seatAvailable
	 * @return queried trip excluding passenger
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
	 * lists all trips matching the query in ascending order of date. This 
	 * query takes as input the name of the passenger that is logged into the app. 
	 * It excludes trips that the passenger has already joined in the search results. 
	 * 
	 * @param departureLocation
	 * @param destination
	 * @param name
	 * @return sorted list of trips excluding passenger
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

	/**
	 * lists all trips matching the query in ascending order of price. This 
	 * query takes as input the name of the passenger that is logged into the app. 
	 * It excludes trips that the passenger has already joined in the search results. 
	 * 
	 * @param departureLocation
	 * @param destination
	 * @param name
	 * @return sorted list of trips excluding passenger
	 */
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

	/**
	 * lists all trips that are set between two dates
	 * 
	 * @param startDate
	 * @param endDate
	 * @return list of trips
	 */
	@GetMapping("/trips/date/{date1}/{date2}")
	public List<Trip> listTripsInTimeframe(@PathVariable("date1") String startDate, @PathVariable("date2") String endDate) {
		List<Integer> trips = repository.getAllTrips();
		List<Trip> tripsInTimeframe = new ArrayList<Trip>();

		for(int t: trips) {
			Trip tempTrip = repository.getSpecificTrip(t);

			if((tempTrip.getDepartureDate()).compareTo(startDate) >= 0 && (tempTrip.getDepartureDate()).compareTo(endDate) <= 0) {
				tripsInTimeframe.add(tempTrip);

			}
		}

		return tripsInTimeframe;

	}

	/**
	 * Gets the top 3 most popular destinations
	 * 
	 * @return sorted list top 3
	 */
	@GetMapping("/trips/destination/top3")
	public Map<String,String> getPopularDestination() 
	{
		List<String> popularDestination = repository.getFrequentDestinations();
		Map<String,String> top3 = new HashMap<String,String>();
		String first = null;
		int firstCount = 0;

		String second = null;
		int secondCount = 0;

		String third = null;
		int thirdCount = 0;

		int cityFrequency;

		List<String> cityNames = repository.getFrequentDestinations();
		Set<String> hs = new HashSet<>();

		hs.addAll(cityNames);
		cityNames.clear();
		cityNames.addAll(hs);

		for (String s: cityNames) 
		{
			cityFrequency = Collections.frequency(popularDestination, s);

			if( cityFrequency  >= firstCount) //#1
			{
				thirdCount = secondCount;
				secondCount = firstCount;
				firstCount = cityFrequency;
				third = second;
				second = first;
				first = s;	
			}
			else if(cityFrequency >= secondCount) //#2
			{
				thirdCount = secondCount;
				secondCount = cityFrequency;

				third = second;
				second = s;


			}
			else if(cityFrequency >= thirdCount) //#3
			{
				thirdCount = cityFrequency;

				third = s;
			}

		}
		top3.put("first", first);
		top3.put("second", second);
		top3.put("third", third);
		return top3;

	}

	/**
	 * Displays info for specific trip
	 * 
	 * @param tripID
	 * @return trip
	 */
	@GetMapping("/trips/{tripID}")
	public Trip getTripInfo(@PathVariable("tripID")int tripID)
	{
		Trip t = repository.getSpecificTrip(tripID);
		return t;
	}

	/**
	 * lists all open trips in the database. Open trips are trips which
	 * have not yet been completed.
	 * 
	 * @return list of open trips
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

	/**
	 * Modifies the departure date of a trip. You can't set the date of a trip to
	 * a date that has already passed.
	 * 
	 * @param tripID
	 * @param departureDate
	 * @return Response
	 * @throws ParseException
	 */
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

	/**
	 * Modifies departure time of a trip. Cannot set departure time to a time that has already passed. 
	 * 
	 * @param tripID
	 * @param departureTime
	 * @return Response
	 * @throws ParseException
	 */
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

					else {
						repository.modifyDepartureTime(t, departureTime);
						r.setResponse(true);
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

	/**
	 * Modifies departure location of a trip. Cannot modify departure location once passengers have joined
	 * 
	 * @param tripID
	 * @param departureLocation
	 * @return Response
	 */
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
			r.setError("Cannot modify a trip once passengers have joined");
			r.setResponse(false);
			return r;
		}


	}

	/**
	 * Modifies destination of a trip. Cannot modify destination once passengers have joined.
	 * 
	 * @param tripID
	 * @param destination
	 * @return Response
	 */
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
			r.setError("Cannot modify a trip once passengers have joined");
			r.setResponse(false);
			return r;
		}
	}

	/**
	 * Modifies the price of a trip. Cannot modify the price once passengers have joined
	 * @param tripID
	 * @param price
	 * @return Response
	 */
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
			r.setError("Cannot modify a trip once passengers have joined");
			r.setResponse(false);
			return r;
		}

	}

	/**
	 * Modifies available seats in a trip. Cannot modify available seats once passengers have joined
	 * 
	 * @param tripID
	 * @param seatAvailable
	 * @return Response
	 */
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
	 * 
	 * @param tripID
	 * @return Response
	 */
	@PostMapping("/trips/close/{tripID}")
	public Response closeTrip(@PathVariable("tripID")int tripID)
	{
		Response r = new Response();
		try {
			repository.closeTrip(tripID);

			r.setResponse(true);
		} catch (NullPointerException e) {
			r.setResponse(false);
			r.setError("No such trip exists");
		}
		return r;
	}

	/**
	 * This method deletes a trip from the repository. Used when a driver deletes a trip before
	 * there are any passengers on it
	 * 
	 * @param tripID
	 * @throws ParseException 
	 */
	@PostMapping("/trips/delete/{trip}")
	public Response deleteTrip(@PathVariable("trip")int tripID) throws ParseException
	{
		Response r = new Response();
		try {
			repository.deleteTrip(tripID);
			r.setResponse(true);
		} catch(NullPointerException e) {
			r.setResponse(false);
			r.setError("No such trip exists");
		}
		return r;
	}

}

