package ca.mcgill.ecse321.karpool;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

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
import ca.mcgill.ecse321.karpool.application.controller.KarpoolController;
import ca.mcgill.ecse321.karpool.application.repository.KarpoolRepository;
import ca.mcgill.ecse321.karpool.model.User;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = KarpoolApplication.class)
public class KarpoolApplicationTests
{
	@Mock
private KarpoolRepository userDao;

@InjectMocks
private KarpoolController controller;

private static final String USER_KEY = "TestParticipant";
private static final String NONEXISTING_KEY = "NotAParticipant";
private static final String USER_EMAIL = "email";
private static final String USER_PASS = "correctPass";
private static final String USER_PASS_INCORRECT = "incorrectPass";
private static final int NON_EXISTANT_ZIPCODE = 12345;
private static final int zipcode1 = 90210;
private static final int zipcode2 = 72110;
private static final float DistanceTraveled = (float) 2341.865;

@Before
public void setMockOutput() {
  when(userDao.getUser(anyString())).thenAnswer( (InvocationOnMock invocation) -> {
    if(invocation.getArgument(0).equals(USER_KEY)) {
      User user = new User();
      user.setName(USER_KEY);
			user.setEmail(USER_EMAIL);
			user.setPassword(USER_PASS);
      return user;
    } else {
      return null;
    }
  });
}

//    @Test
//    public void testAddPassenger() {
//    	assertEquals(controller.addPassenger(), USER_KEY);
//    	
//    }

	@Test
	public void contextLoads() {
	}

	@Test
public void testUserQueryFound() {
  assertEquals(controller.queryUser(USER_KEY), USER_KEY);
}

@Test
public void testUserQueryNotFound() {
  assertEquals(controller.queryUser(NONEXISTING_KEY), KarpoolController.ERROR_NOT_FOUND_MESSAGE);
}

@Test
public void testAuthenticateUserPassed()
{
	assertEquals(controller.authenticateUser(userDao.getUser(USER_KEY).getEmail(), USER_PASS), true);
	//when(controller.authenticateUser(userDao.getUser(USER_KEY).getEmail(), userDao.getUser(USER_KEY).getPassword())).thenReturn(true);
}

@Test
public void testAuthenticateUserFailed()
{
	assertEquals(controller.authenticateUser(userDao.getUser(USER_KEY).getEmail(), USER_PASS_INCORRECT), false);
}

@Test
public void testDistanceCorrect() {
	assertEquals(controller.Distance(zipcode1, zipcode2), DistanceTraveled, DistanceTraveled);
}

@Test 
public void testDistanceIncorrect() {
	assertEquals(controller.Distance(NON_EXISTANT_ZIPCODE, zipcode2), 0 , 0);
}

}
