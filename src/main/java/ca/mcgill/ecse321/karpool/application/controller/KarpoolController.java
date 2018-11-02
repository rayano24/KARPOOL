package ca.mcgill.ecse321.karpool.application.controller;

import java.util.*;
import ca.mcgill.ecse321.karpool.application.model.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.karpool.application.repository.*;

@RestController
public class KarpoolController {

	public static final String ERROR_NOT_FOUND_MESSAGE = "NOT FOUND";

	@Autowired
	KarpoolRepository repository;

	Driver driver;

	Set<Passenger> passengers;
	
	EndUser user;

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
	 * creates trip with given parameters
	 * 
	 * @param departureLocation
	 * @param destination
	 * @param seatAvailable
	 * @param departureTime
	 * @return the created trip
	 * @throws ParseException 
	 */
	@PostMapping("/trips/{location}/{destination}/{seats}/{time}/{date}")
	public Trip createTrip(@PathVariable("location") String departureLocation, @PathVariable("destination") String destination, 
			@PathVariable("seats") int seatAvailable, @PathVariable("time") String departureTime, @PathVariable("date") String departureDate) throws ParseException
	{
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		LocalDate currDate = LocalDate.now();
//		LocalTime currTime = LocalTime.now();
//		currDate = sdf.format(currDate);
//		destination = (destination.toLowerCase()).replaceAll("\\s+","");
		//Date tripDate = sdf.parse(departureDate);
		
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
		
		
		
		Trip trip = repository.createTrip(destination,departureTime, departureDate, departureLocation, seatAvailable);
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
			if(tFull.getSeatAvailable()>0)
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
			if(tFull.getSeatAvailable()>0)
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
	 * This method marks a trip as completed
	 * @param trip
	 */
	@PostMapping("/trips/close/{trip}")
	public void closeTrip(@PathVariable("trip")int tripID)
	{
		repository.closeTrip(tripID);

	}

	/**
	 * Rate the driver
	 * 
	 * @param name
	 * @param rating
	 */
	@PostMapping("/drivers/rate/{name}/{rating}")
	public void rateDriver(@PathVariable("name") String name,@PathVariable("rating") Rating rating)

	{
		//need to check if rating is a valid rating
		try {
			Driver d = repository.getDriver(name);
			d.setRating(rating);

		}
		catch (NullPointerException e) {
			System.out.println(ERROR_NOT_FOUND_MESSAGE);
		}
	}
	
	/**
	 * Rate the passenger
	 * 
	 * @param name
	 * @param rating
	 */
	@PostMapping("/passengers/rate/{name}/{rating}")
	public void ratePassenger(@PathVariable("name") String name,@PathVariable("rating") Rating rating)

	{
		//need to check if rating is a valid rating
		try {
			Passenger p = repository.getPassenger(name);
			p.setRating(rating);

		}
		catch (NullPointerException e) {
			System.out.println(ERROR_NOT_FOUND_MESSAGE);
		}

	}
	
	//@PostMapping("/trips/tripID/location/{location}")
	public void modifyTripDate(@PathVariable("trip")int tripID, @PathVariable("date")String departureDate) {
	
		try {
		Trip t = repository.getSpecificTrip(tripID);
		t.setDepartureDate(departureDate);
		} catch (NullPointerException e) {
			System.out.println(ERROR_NOT_FOUND_MESSAGE);
		}
			
	}
	//@PostMapping("")
	public void modifyTripTime(@PathVariable("trip")int tripID, @PathVariable("date")String departureTime) {
	
		try {
		Trip t = repository.getSpecificTrip(tripID);
		t.setDepartureTime(departureTime);
		
		} catch (NullPointerException e) {
			System.out.println(ERROR_NOT_FOUND_MESSAGE);
		}
			
	}

	@PostMapping("/trips/{tripID}/triplocation/{location}")
	public void modifyTripLocation(@PathVariable("tripID")int tripID, @PathVariable("location")String departureLocation) {
	
		try {
		Trip t = repository.getSpecificTrip(tripID);
		Trip t1 = repository.getSpecificTrip(tripID);
		repository.modifyTripLocation(t, departureLocation);
		
		System.out.println(t.getDepartureLocation() + " " + t1.getDepartureLocation());
		} catch (NullPointerException e) {
			System.out.println(ERROR_NOT_FOUND_MESSAGE);
		}
			
	}
	//@PostMapping("")
	public void modifyTripDestination(@PathVariable("trip")int tripID, @PathVariable("destination")String destination) {
	
		try {
		Trip t = repository.getSpecificTrip(tripID);
		t.setDestination(destination);
		} catch (NullPointerException e) {
			System.out.println(ERROR_NOT_FOUND_MESSAGE);
		}
			
	}
	//@PostMapping("")
	public void modifyTripSeats(@PathVariable("trip")int tripID, @PathVariable("seats")int seatAvailable) {
	
		try {
		Trip t = repository.getSpecificTrip(tripID);
		t.setSeatAvailable(seatAvailable);

		} catch (NullPointerException e) {
			System.out.println(ERROR_NOT_FOUND_MESSAGE);
		}
			
	}

	
	/**
	 * This method allows for new passengers to be added to a specific 
	 * trip taking place. It checks to see if the passenger is already 
	 * signed up for this trip, if not it adds to passenger to the trip.
	 * @param passenger
	 * @param trip
	 * @return
	 */
	@GetMapping
	public Response addPassenger(Passenger passenger, Trip trip) 
	{
		Response r = new Response();
		/*check this because its a mess */
		if (trip.getSeatAvailable() <= 0) {
			r.setResponse(false);
			r.setError("No seats available");
		}

		else if (passengers.contains(passenger)) 
		{
			r.setResponse(false);
			r.setError("You are already on this trip");
		}

		else {
			passenger.setTrip(trip);
			trip.getPassenger().add(passenger);
			r.setResponse(true);
			r.setError(null);
		}	
		return r;
	}

//	public float Distance (int zipcode1, int zipcode2) throws MalformedURLException, IOException
//	{
//
//        BufferedReader br = null;
//
//        try {
//
//            URL url = new URL("https://www.zipcodeapi.com/rest/GOhazMBKVJ2VDSEOrrkf0sswW4D5c4NYOjZi2mGTjf2wuvgvTkUj5L1KpR2GkRRI/distance.json/" + zipcode1 + "/" +zipcode2 +"/km");
//            br = new BufferedReader(new InputStreamReader(url.openStream()));
//
//            String line;
//
//            StringBuilder sb = new StringBuilder();
//
//            while ((line = br.readLine()) != null) {
//                sb.append(line);
//                sb.append(System.lineSeparator());
//            }
//
//            String RoughDistance = sb.toString();
//            if (RoughDistance.charAt(2) == 'e') {
//            	return 0;
//            }
//            String intValue = RoughDistance.replaceAll("[^0-9, .]", "");
//            float distance = Float.parseFloat(intValue);
//            return distance;
//
//        } finally {
//
//            if (br != null) {
//                br.close();
//            }
//        }
//    }

}

