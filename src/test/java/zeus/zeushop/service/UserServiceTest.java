package zeus.zeushop.service;

import zeus.zeushop.model.User;
import zeus.zeushop.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    /*
    @Test
    public void testRegisterUserSuccess() {
        User user = new User();
        user.setUsername("dummy");
        user.setPassword("asdf");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User registeredUser = userService.createUser(user);

        assertNotNull(registeredUser);
        assertEquals(user.getUsername(), registeredUser.getUsername());
        assertEquals(user.getPassword(), registeredUser.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

   
    @Test
    public void testRegisterUserFailure() {
        User user = new User();
        user.setUsername(null);
        user.setPassword(null);

        User registeredUser = userService.createUser(user);

        assertNull(registeredUser);
        verify(userRepository, never()).save(any(User.class));
    }
    */
}