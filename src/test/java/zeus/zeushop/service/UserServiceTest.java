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

    @Test
    public void testUpdateUser_UserDoesNotExist() {
        // Create a user to simulate the updated user details
        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setUsername("newUsername");
        updatedUser.setPassword("newPassword");

        // Mock the userRepository.findById to return an empty Optional, simulating that the user does not exist
        when(userRepository.findById(eq(1))).thenReturn(Optional.empty());

        // Perform the update operation
        User resultUser = userService.updateUser(1, updatedUser);

        // Assert that the result is null since no user was found to update
        assertNull(resultUser);

        // Verify the userRepository.findById was called correctly
        verify(userRepository, times(1)).findById(eq(1));

        // Verify that userRepository.save was never called, as there was no user to update
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testGetAllUsers() {
        // Create a list of users to simulate database contents
        User user1 = new User();
        User user2 = new User();
        user1.setUsername("dummy1");
        user2.setUsername("dummy2");
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        // Mock the findAll method to return the list of users
        when(userRepository.findAll()).thenReturn(users);

        // Call the service method
        List<User> result = userService.getAllUsers();

        // Assertions to check the correct data is returned
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("dummy1", result.getFirst().getUsername());

        // Verify the interaction with userRepository
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testGetUserById() {
        // Create a user to simulate finding a user by ID
        User user = new User();
        user.setId(1);
        user.setUsername("dummy");

        // Mock the findById method to return the user
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Call the service method
        Optional<User> result = userService.getUserById(1);

        // Assertions to check the correct user is returned
        assertTrue(result.isPresent());
        assertEquals("dummy", result.get().getUsername());

        // Verify the interaction with userRepository
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    public void testGetUserByUsername() {
        // Create a user to simulate finding a user by username
        User user = new User();
        user.setId(1);
        user.setUsername("dummy");

        // Mock the findByUsername method to return the user
        when(userRepository.findByUsername("dummy")).thenReturn(user);

        // Call the service method
        User result = userService.getUserByUsername("dummy");

        // Assertions to check the correct user is returned
        assertNotNull(result);
        assertEquals("dummy", result.getUsername());

        // Verify the interaction with userRepository
        verify(userRepository, times(1)).findByUsername("dummy");
    }

    @Test
    public void testDeleteUser() {
        // This test will not return a value but will verify action was taken

        // Call the delete method
        userService.deleteUser(1);

        // Verify the interaction with userRepository
        verify(userRepository, times(1)).deleteById(1);
    }
}