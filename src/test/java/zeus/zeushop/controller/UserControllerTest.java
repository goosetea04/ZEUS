package zeus.zeushop.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.Model;
import zeus.zeushop.model.User;
import zeus.zeushop.repository.UserRepository;
import zeus.zeushop.service.UserDetailsServiceImpl;
import zeus.zeushop.service.UserService;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserControllerTest {

    @Mock
    private Model model;

    @Mock
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Mock
    private UserService userService;

    @Mock
    private UserDetails userDetails;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @Test
    public void testGetProfilePage() {
        String viewName = userController.getProfilePage(model);
        assertEquals("profile", viewName);
        verify(model).addAttribute(eq("user"), any(User.class));
    }

    @Test
    public void testGetEditProfilePage() {
        User mockUser = new User();
        mockUser.setUsername("dummy");

        when(userDetailsServiceImpl.loadUserByUsername(anyString())).thenReturn(mockUser);
        User user = userDetailsServiceImpl.loadUserByUsername(mockUser.getUsername());

        String viewName = userController.getEditProfilePage(model, user);
        assertEquals("editprofile", viewName);
        verify(model).addAttribute(eq("user"), any(User.class));
    }
    @Test
    public void testEditProfile() {
        // Setup
        User user = new User(); // This is the user from the form.
        user.setUsername("dummy");

        User oldUser = new User(); // This should be the result of loadUserByUsername.
        oldUser.setId(1);
        oldUser.setUsername("dummy");

        User updatedUser = new User(); // This should be the result of updateUser.
        updatedUser.setId(1);
        updatedUser.setUsername("newDummy");

        // Mock the behavior of userDetails to match the username of oldUser.
        when(userDetails.getUsername()).thenReturn("dummy");
        when(userDetailsServiceImpl.loadUserByUsername("dummy")).thenReturn(oldUser);

        // Setup updateUser to return updatedUser when called with correct id and user.
        when(userService.updateUser(eq(1), any(User.class))).thenReturn(updatedUser);

        // Action
        String result = userController.editProfile(user, userDetails);

        // Assertions
        assertEquals("profile", result);
        verify(userService).updateUser(eq(1), any(User.class));
    }

    @Test
    public void testGetRegisterPage() {
        // Action
        String viewName = userController.getRegisterPage(model);

        // Assertions
        assertEquals("register", viewName);
        verify(model).addAttribute(eq("user"), any(User.class));
    }

    @Test
    public void testRegister() {
        // Setup
        User newUser = new User();
        newUser.setUsername("newUser");
        newUser.setPassword("password");

        when(userService.createUser(any(User.class))).thenReturn(newUser);

        // Action
        String view = userController.register(newUser);

        // Assertions
        assertEquals("redirect:/login", view);
    }
}