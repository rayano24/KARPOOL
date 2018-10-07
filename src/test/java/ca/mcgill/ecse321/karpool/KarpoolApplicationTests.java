package ca.mcgill.ecse321.karpool;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ca.mcgill.ecse321.karpool.application.controller.KarpoolController;
import ca.mcgill.ecse321.karpool.application.model.User;
import ca.mcgill.ecse321.karpool.repository.KarpoolRepository;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KarpoolApplicationTests
{
	@Mock
private KarpoolRepository userDao;

@InjectMocks
private KarpoolController controller;

private static final String USER_KEY = "TestParticipant";
private static final String NONEXISTING_KEY = "NotAParticipant";

@BeforeEach
void setMockOutput() {
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
