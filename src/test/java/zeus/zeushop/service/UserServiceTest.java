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

import java.util.ArrayList;
import java.util.List;
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
    public void testUpdateUser_UserNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(null);

        User result = userService.updateUser("nonexistent", new User());

        assertNull(result);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testUpdateUser_UpdateUsernameAndPassword() {
        User existingUser = new User();
        User updateUserDetails = new User();
        updateUserDetails.setUsername("newUsername");
        updateUserDetails.setPassword("newPassword");

        when(userRepository.findByUsername("existingUser")).thenReturn(existingUser);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User result = userService.updateUser("existingUser", updateUserDetails);

        assertNotNull(result);
        assertEquals("newUsername", existingUser.getUsername());
        assertEquals("encodedPassword", existingUser.getPassword());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    public void testUpdateUser_OnlyUpdateUsername() {
        User existingUser = new User();
        User updateUserDetails = new User();
        updateUserDetails.setUsername("newUsername");

        when(userRepository.findByUsername("existingUser")).thenReturn(existingUser);
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User result = userService.updateUser("existingUser", updateUserDetails);

        assertNotNull(result);
        assertEquals("newUsername", existingUser.getUsername());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    public void testUpdateUser_OnlyUpdatePassword() {
        User existingUser = new User();
        User updateUserDetails = new User();
        updateUserDetails.setPassword("newPassword");

        when(userRepository.findByUsername("existingUser")).thenReturn(existingUser);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User result = userService.updateUser("existingUser", updateUserDetails);

        assertNotNull(result);
        assertEquals("encodedPassword", existingUser.getPassword());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    public void testUpdateUser_Exception() {
        User updateUserDetails = new User();
        when(userRepository.findByUsername("existingUser")).thenThrow(new RuntimeException("Database error"));

        User result = userService.updateUser("existingUser", updateUserDetails);

        assertNull(result);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testVerifyPassword_PasswordsMatch() {
        User user = new User();
        user.setPassword("password123");
        user.setConfirmPassword("password123");

        Boolean result = userService.verifyPassword(user);

        assertTrue(result);
    }

    @Test
    public void testVerifyPassword_PasswordsDoNotMatch() {
        User user = new User();
        user.setPassword("password123");
        user.setConfirmPassword("password456");

        Boolean result = userService.verifyPassword(user);

        assertFalse(result);
    }

    @Test
    public void testVerifyPassword_NullPassword() {
        User user = new User();
        user.setPassword(null);
        user.setConfirmPassword("password456");

        Boolean result = userService.verifyPassword(user);

        assertFalse(result);
    }

    @Test
    public void testVerifyPassword_NullConfirmPassword() {
        User user = new User();
        user.setPassword("password123");
        user.setConfirmPassword(null);

        Boolean result = userService.verifyPassword(user);

        assertFalse(result);
    }

    @Test
    public void testVerifyPassword_BothNullPasswords() {
        User user = new User();
        user.setPassword(null);
        user.setConfirmPassword(null);

        Boolean result = userService.verifyPassword(user);

        assertFalse(result);
    }

    @Test
    public void testGetAllUsers() {
        User user1 = new User();
        User user2 = new User();
        user1.setUsername("dummy1");
        user2.setUsername("dummy2");
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("dummy1", result.getFirst().getUsername());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testGetUserById() {
        User user = new User();
        user.setId(1);
        user.setUsername("dummy");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1);

        assertTrue(result.isPresent());
        assertEquals("dummy", result.get().getUsername());

        verify(userRepository, times(1)).findById(1);
    }

    @Test
    public void testGetUserByUsername() {
        User user = new User();
        user.setId(1);
        user.setUsername("dummy");

        when(userRepository.findByUsername("dummy")).thenReturn(user);

        User result = userService.getUserByUsername("dummy");

        assertNotNull(result);
        assertEquals("dummy", result.getUsername());

        verify(userRepository, times(1)).findByUsername("dummy");
    }

    @Test
    public void testDeleteUser() {
        userService.deleteUser(1);

        verify(userRepository, times(1)).deleteById(1);
    }
}