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

@Before
public void setMockOutput() {
  when(userDao.getUser(anyString())).thenAnswer( (InvocationOnMock invocation) -> {
    if(invocation.getArgument(0).equals(USER_KEY)) {
      User user = new User();
      user.setName(USER_KEY);
      return user;
    } else {
      return null;
    }
  });
}

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

}
