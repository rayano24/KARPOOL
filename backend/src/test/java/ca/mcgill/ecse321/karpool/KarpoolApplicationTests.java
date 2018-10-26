package ca.mcgill.ecse321.karpool;

import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ca.mcgill.ecse321.karpool.application.KarpoolApplication;
import ca.mcgill.ecse321.karpool.application.controller.KarpoolController;
import ca.mcgill.ecse321.karpool.application.model.Rating;
import ca.mcgill.ecse321.karpool.application.model.EndUser;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = KarpoolApplication.class)
public class KarpoolApplicationTests
{
	//	@Mock
	//private KarpoolRepository userDao;

	private static KarpoolController controller = new KarpoolController();

	private static final String USER_KEY = "TestParticipant";
	private static final String NONEXISTING_KEY = "NotAParticipant";
	private static final String USER_EMAIL = "email";
	private static final String USER_PASS = "correctPass";
	private static final String USER_PASS_INCORRECT = "incorrectPass";
	private static final String USER_PHONE = "1234567890";
	private static final int NON_EXISTANT_ZIPCODE = 3423;
	private static final int zipcode1 = 90210;
	private static final int zipcode2 = 72110;
	private static final float DistanceTraveled = (float) 2341.865;
	private static final boolean wasAdded = true;


//	Passenger mockPassenger = Mockito.mock(Passenger.class);
//	Trip mockTrip = Mockito.mock(Trip.class);
//
//	@Before
//	public void setMockOutput() {
//		when(userDao.getUser(anyString())).thenAnswer( (InvocationOnMock invocation) -> {
//			if(invocation.getArgument(0).equals(USER_KEY)) {
//				User user = new User(USER_KEY, USER_EMAIL, USER_PHONE, USER_PASS);
//				return user;
//			} else {
//				return null;
//			}
//		});
//	}

	@Mock
	EntityManager manager;
	
	@Test
	public void testCreateUser()
	{

		EndUser u = controller.createUser(USER_KEY, USER_EMAIL, USER_PASS, USER_PHONE, Rating.NONE, false);
		String name = u.getName();
		assertEquals(USER_KEY, name);
	}



	//    @Test
	//    public void testAddPassenger() {
	//    	assertEquals(wasAdded, controller.addPassenger(mockPassenger, mockTrip));
	//
	//    }

	@Test
	public void contextLoads() {
	}

//		@Test
//	public void testUserQueryFound() {
//	 assertEquals(controller.queryUser(USER_KEY), USER_KEY);
//	}
	//
	//@Test
	//public void testUserQueryNotFound() {
	//  assertEquals(controller.queryUser(NONEXISTING_KEY), KarpoolController.ERROR_NOT_FOUND_MESSAGE);
	//}
	//
	//@Test
	//public void testAuthenticateUserPassed()
	//{
	//	assertEquals(controller.authenticateUser(userDao.getUser(USER_KEY).getEmail(), USER_PASS), true);
	//	//when(controller.authenticateUser(userDao.getUser(USER_KEY).getEmail(), userDao.getUser(USER_KEY).getPassword())).thenReturn(true);
	//}
	//
	//@Test
	//public void testAuthenticateUserFailed()
	//{
	//	assertEquals(controller.authenticateUser(userDao.getUser(USER_KEY).getEmail(), USER_PASS_INCORRECT), false);
	//}
	//
	//@Test
	//public void testDistanceCorrect() throws MalformedURLException, IOException {
	//	assertEquals(controller.Distance(zipcode1, zipcode2), DistanceTraveled);
	//}
	//
	//@Test
	//public void testDistanceIncorrect() throws MalformedURLException, IOException{
	//	assertEquals(controller.Distance(NON_EXISTANT_ZIPCODE, zipcode2), 0);
	//}

}
