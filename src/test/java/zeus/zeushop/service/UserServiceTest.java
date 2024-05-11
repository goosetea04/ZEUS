package zeus.zeushop.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import zeus.zeushop.model.User;
import zeus.zeushop.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    public void testRegisterUserSuccess() {
        User user = new User();
        user.setUsername("dummy");
        user.setPassword("asdf");

        when(passwordEncoder.encode(any(String.class))).thenReturn(user.getPassword());
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

    @Test
    public void testUpdateUser() {
        User existingUser = new User();
        existingUser.setId(1);
        existingUser.setUsername("oldUsername");

        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setUsername("newUsername");

        when(userRepository.findById(eq(1))).thenReturn(Optional.of(existingUser));

        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User resultUser = userService.updateUser(1, updatedUser);

        assertNotNull(resultUser);
        assertEquals("newUsername", resultUser.getUsername());

        verify(userRepository, times(1)).findById(eq(1));

        verify(userRepository, times(1)).save(any(User.class));
    }
}