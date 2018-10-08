package ca.mcgill.ecse321.karpool;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import org.mockito.Mock.*;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ca.mcgill.ecse321.karpool.application.KarpoolApplication;
import ca.mcgill.ecse321.karpool.application.Passenger;
import ca.mcgill.ecse321.karpool.application.Rating;
import ca.mcgill.ecse321.karpool.application.controller.KarpoolController;
import ca.mcgill.ecse321.karpool.application.repository.KarpoolRepository;
import ca.mcgill.ecse321.karpool.model.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KarpoolApplication.class)
public class KarpoolApplicationTests {
	@Mock
	private KarpoolRepository userDao;

	@InjectMocks
	private KarpoolController controller;

	private static final String USER_KEY = "TestParticipant";
	private static final String NONEXISTING_KEY = "NotAParticipant";
	private static final String USER_EMAIL = "email";
	private static final String USER_PASS = "correctPass";
	private static final String USER_PASS_INCORRECT = "incorrectPass";
	private static final int NON_EXISTANT_ZIPCODE = 3423;
	private static final int zipcode1 = 90210;
	private static final int zipcode2 = 72110;
	private static final float DistanceTraveled = (float) 2341.865;
	private static final boolean wasAdded = true;
	private static final String USER_PHONE = "5141231234";
	private static final String USER_PHONE_INCORRECT = "514";
	private static final Boolean USER_NO_RECORD = false;
	private static final Boolean USER_RECORD = true;
	private static final Rating USER_RATING = Rating.NONE;
	private static final Rating USER_TEST_RATING = Rating.FIVE;

	Passenger mockPassenger = Mockito.mock(Passenger.class);
	Trip mockTrip = Mockito.mock(Trip.class);


	@Before
	public void setMockOutput() {
		when(userDao.getUser(anyString())).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0).equals(USER_KEY)) {
				User user = new User();
				user.setName(USER_KEY);
				user.setEmail(USER_EMAIL);
				user.setPassword(USER_PASS);
				user.setPhoneNumber(USER_PHONE);
				user.setRating(USER_RATING);
				user.setRecord(USER_NO_RECORD);
				return user;
			} else {
				return null;
			}
		});
	}

	@Test
	    public void testAddPassenger()
			{
	    	assertEquals(wasAdded, controller.addPassenger(mockPassenger, mockTrip));

	    }

	@Test
	public void contextLoads() {
	}

	@Test
	public void testUserQueryFound() {
		assertEquals(controller.queryUser(USER_KEY), USER_KEY);
	}

	@Test
	public void testAddRating() {
		controller.addRating(USER_EMAIL, USER_TEST_RATING);
		assertEquals(Rating.FIVE, userDao.getUser(USER_EMAIL).getRating());
	}

	@Test
	public void testRegisterSuccess() {
		Boolean isAccountCreatedSuccessfully = false;
		if (controller.createUser(USER_KEY, USER_EMAIL, USER_PASS, USER_PHONE, USER_RATING, USER_RECORD) != null) {

			isAccountCreatedSuccessfully = true;

		}
		assertTrue(isAccountCreatedSuccessfully);
	}

	@Test
	public void testRegisterFailure() {
		Boolean accountCreationFailed = false;
		if (controller.createUser(USER_KEY, USER_EMAIL, USER_PASS, USER_PHONE_INCORRECT, USER_RATING,
				USER_RECORD) == null) {
			accountCreationFailed = true;

		}
		assertTrue(accountCreationFailed);
	}

	@Test
	public void testAuthenticateUserPassed() {
		assertEquals(true, controller.authenticateUser(userDao.getUser(USER_KEY).getEmail(),
				userDao.getUser(USER_KEY).getPassword()));
		// when(controller.authenticateUser(userDao.getUser(USER_KEY).getEmail(),
		// userDao.getUser(USER_KEY).getPassword())).thenReturn(true);
	}

	@Test
	public void testAuthenticateUserFailed() {
		assertEquals(false, controller.authenticateUser(userDao.getUser(USER_KEY).getEmail(), USER_PASS_INCORRECT));
	}

	@Test
	public void testDistanceCorrect() throws MalformedURLException, IOException {
		assertEquals(controller.Distance(zipcode1, zipcode2), DistanceTraveled, 0);
	}

	@Test
	public void testDistanceIncorrect() throws MalformedURLException, IOException {
		assertEquals(controller.Distance(NON_EXISTANT_ZIPCODE, zipcode2), -1, 0);
	}

}
