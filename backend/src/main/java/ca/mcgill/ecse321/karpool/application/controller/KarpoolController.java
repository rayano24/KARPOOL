package ca.mcgill.ecse321.karpool.application.controller;

import java.util.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ca.mcgill.ecse321.karpool.application.model.*;

import ca.mcgill.ecse321.karpool.application.repository.*;

@RestController
public class KarpoolController {

	public static final String ERROR_NOT_FOUND_MESSAGE = "NOT FOUND";

	@Autowired
	KarpoolRepository repository;

	@Autowired
	Driver driver;

	@Autowired
	Set<Passenger> passengers;
	
	@Autowired
	User user;

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
	public User createUser(@PathVariable("name") String name, @PathVariable("email") String email, @PathVariable("password") String password, 
			@PathVariable("phone") String phone, @PathVariable("rating") Rating rating, @PathVariable("record") boolean criminalRecord)
	{
		try 
		{
			if(phone.length() == 10) 
			{

				Integer.parseInt(phone);
			}
			else 
			{
				System.out.println("You entered an invalid phone number");
				return null;
			}
		} 
		catch(NullPointerException |  NumberFormatException e) 
		{
			System.out.println("Exception - Invalid phone number");
			return null;
		}
		User u;
		
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
	@GetMapping("/users/auth/{name}/{password}")
	public boolean authenticateUser(@PathVariable("name")String name, @PathVariable("password")String password)

	{
		try {
			User user = repository.getUser(name);
			if(user.getPassword().equals(password))
				return true;
		}
		catch(NullPointerException e) {
			System.out.println("Error - Attempted to authenticate null user");
			return false;
		}
		return false;
	}

	/**
	 * searches for a user with given name
	 *
	 * @param name
	 * @return the queried user
	 */
	@GetMapping("/users/{name}")
	public String queryUser(@PathVariable("name")String name)

	{
		User user = repository.getUser(name);
		if(user == null)
		{
			return ERROR_NOT_FOUND_MESSAGE;
		}
		return user.getName();
	}

	/**
	 * creates trip with given parameters
	 * 
	 * @param departureLocation
	 * @param destination
	 * @param seatAvailable
	 * @param departureTime
	 * @return the created trip
	 */
	@PostMapping("/trips/{location}/{destination}/{seats}/{time}")
	public Trip createTrip (@PathVariable("location") String departureLocation, @PathVariable("destination") String destination, 
			@PathVariable("seats") int seatAvailable, @PathVariable("time") String departureTime)

	{
		Trip trip = repository.createTrip(destination,departureTime, departureLocation, seatAvailable);
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
	public Trip queryTrip(@PathVariable("location") String departureLocation, 
			@PathVariable("destination") String destination, @PathVariable ("seats") int seatAvailable)
	{
		//should be querying trip from repository with matching departure and destination, with required number of seats
		Trip Trip = driver.getTrip();

		return Trip;
	}


	@PostMapping("/TRIPS/{trip}")
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
			User user = repository.getUser(name);
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

		boolean wasAdded = false;
		if (trip.getSeatAvailable()<=0) {
		return false;
	}
		else if (passengers.contains(passenger)) {
		return false;
	}

	else
	{
		passenger.setTrip(trip);
	}

	wasAdded = true;
	return wasAdded;



}


	public float Distance (int zipcode1, int zipcode2) throws MalformedURLException, IOException
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
    }




}