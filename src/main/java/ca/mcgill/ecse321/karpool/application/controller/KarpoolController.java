package ca.mcgill.ecse321.karpool.application.controller;

import java.util.*;
import ca.mcgill.ecse321.karpool.application.model.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;

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
	//FIXME: Why does createUser need to return the usersName?
	
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
	 * Creates a user via the createUser method from KarpoolRepository. Performs parameter validation
	 * 
	 * @param name
	 * @param email
	 * @param password
	 * @param phone
	 * @param rating
	 * @return the users object if found, null if not
	 */
	@PostMapping("/users/{name}/{email}/{password}/"
			+ "{phone}/{rating}/{record}")
	public EndUser createUser(@PathVariable("name") String name, @PathVariable("email") String email, @PathVariable("password") String password, 
			@PathVariable("phone") String phone, @PathVariable("rating") Rating rating, @PathVariable("record") boolean criminalRecord)
	{
		
		try 
		{
			if(phone.length() == 10) 
			{

				Long.parseLong(phone);
			}
			else 
			{
				System.out.println("You entered an invalid phone number");
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
		EndUser u;
		
		if(!criminalRecord) {
			u = repository.createUser(name, email, password, phone, rating, criminalRecord);
			return u;
		}
		else
		{
			System.out.println("Error - Criminal record");
			return null;
		}
	}

	/**
	 *
	 * @param email
	 * @param password
	 * @return TRUE if the account is authenticated
	 */
	@GetMapping("/users/auth/{email}/{password}")
	public boolean authenticateUser(@PathVariable("email")String email, @PathVariable("password")String password)

	{
		boolean authenticate = false;
		try {
			EndUser user = repository.getUser(email);
			if(user.getPassword().equals(password))
				authenticate = true;
		}
		catch(NullPointerException e) {
			System.out.println("Error - Attempted to authenticate null user");
			authenticate =  false;
		}
		return authenticate;
	}

	/**
	 * searches for a user with given name
	 *
	 * @param name
	 * @return the queried user
	 */
	@GetMapping("/users/{name}")
	public EndUser queryUser(@PathVariable("name")String name)

	{
		EndUser user = repository.getUser(name);
		if(user == null)
		{
			System.out.println(ERROR_NOT_FOUND_MESSAGE);
			return null;
		}
		return user;
	}
	
	/**
	 * lists all users in the database
	 * 
	 * @return list of users
	 */
	@GetMapping("/users/all")
	public List<EndUser> listAllUsers()
	{
		List<String> users = repository.getAllUsers();
		List<EndUser> fullUser = new ArrayList<EndUser>();
		for(String u: users)
		{
			fullUser.add(repository.getUser(u));
		}
		
		return fullUser;
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
	public Trip createTrip (@PathVariable("location") String departureLocation, @PathVariable("destination") String destination, 
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
			else if ((time1.compareTo(time2)) < 0) {
				System.out.println("Cannot set a time that has already passed");
				return null;
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
	@GetMapping("/trips/{location}/{destination}/{seats}")
	public List<Trip> queryTrip(@PathVariable("location") String departureLocation, 
			@PathVariable("destination") String destination, @PathVariable ("seats") int seatAvailable)
	{
		//should be querying trip from repository with matching departure and destination, with required number of seats
		List<Integer> trips = repository.getTrips(destination);
		List<Trip> fullTrip = new ArrayList<Trip>();
		for(int t: trips)
		{
			fullTrip.add(repository.getSpecificTrip(t));
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
		
		return fullTrip;
	}


	@PostMapping("/trips/{trip}")
	public void closeTrip(@PathVariable ("trip") Trip trip)
	{
		repository.closeTrip(trip);

	}


	/**
	 * Add a  rating to the user
	 * 
	 * @param name
	 * @param rating
	 */
	@PostMapping("/users/rate/{name}/{rating}")
	public void addRating(@PathVariable("name") String name,@PathVariable("rating") Rating rating)

	{
		//need to check if rating is a valid rating
		try {
			EndUser user = repository.getUser(name);
			user.setRating(rating);

		}
		catch (NullPointerException e) {
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
	public boolean addPassenger(Passenger passenger, Trip trip) {

		/*check this because its a mess */
		if (trip.getSeatAvailable() <= 0) {
			return false;
		}
	
		else if (passengers.contains(passenger)) {
			return false;
		}

		else {
			passenger.setTrip(trip);
			trip.getPassenger().add(passenger);
			return true;
		}	

	//wasAdded = true;
	//return wasAdded;
	
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

