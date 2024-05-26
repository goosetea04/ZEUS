package zeus.zeushop.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import zeus.zeushop.model.User;
import zeus.zeushop.repository.UserRepository;
import zeus.zeushop.service.UserDetailsServiceImpl;
import zeus.zeushop.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthControllerTest {

    @Mock
    private Model model;

    @Mock
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Mock
    private UserService userService;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private AuthController authController;

    @Test
    public void testGetProfilePage() {
        User user = new User();
        when(userDetails.getUsername()).thenReturn("testUser");
        when(userDetailsServiceImpl.loadUserByUsername("testUser")).thenReturn(user);

        String viewName = authController.getProfilePage(model, userDetails);

        assertEquals("profile", viewName);
        verify(userDetailsServiceImpl, times(1)).loadUserByUsername("testUser");
        verify(model, times(1)).addAttribute("user", user);
    }

    @Test
    public void testGetEditProfilePage() {
        User mockUser = new User();
        mockUser.setUsername("dummy");

        when(userDetailsServiceImpl.loadUserByUsername(anyString())).thenReturn(mockUser);
        User user = userDetailsServiceImpl.loadUserByUsername(mockUser.getUsername());

        String viewName = authController.getEditProfilePage(model, user);
        assertEquals("editprofile", viewName);
        verify(model).addAttribute(eq("user"), any(User.class));
    }
    @Test
    public void testEditProfile_PasswordsDoNotMatch() {
        User user = new User();
        user.setPassword("password123");
        user.setConfirmPassword("password456");

        when(userService.verifyPassword(any(User.class))).thenReturn(false);

        String viewName = authController.editProfile(user, userDetails, model);

        assertEquals("editprofile", viewName);
        verify(model, times(1)).addAttribute("passwordError", "Passwords do not match");
        verify(userService, times(1)).verifyPassword(any(User.class));
        verify(userService, never()).updateUser(anyString(), any(User.class));
    }

    @Test
    public void testEditProfile_PasswordsMatch_UserUpdateSuccess() {
        User user = new User();
        user.setPassword("password123");
        user.setConfirmPassword("password123");

        User updatedUser = new User();
        updatedUser.setUsername("updatedUsername");

        when(userService.verifyPassword(any(User.class))).thenReturn(true);
        when(userService.updateUser(anyString(), any(User.class))).thenReturn(updatedUser);

        when(userDetails.getUsername()).thenReturn("username");
        when(userDetails.getPassword()).thenReturn("password123");
        when(userDetails.getAuthorities()).thenReturn(null); // Adjust as necessary for your authorities

        String viewName = authController.editProfile(user, userDetails, model);

        assertEquals("profile", viewName);
        verify(userService, times(1)).verifyPassword(any(User.class));
        verify(userService, times(1)).updateUser(eq("username"), any(User.class));
    }

    @Test
    public void testEditProfile_PasswordsMatch_UserUpdateFailure() {
        User user = new User();
        user.setPassword("password123");
        user.setConfirmPassword("password123");

        when(userService.verifyPassword(any(User.class))).thenReturn(true);
        when(userService.updateUser(anyString(), any(User.class))).thenReturn(null);

        when(userDetails.getUsername()).thenReturn("username");

        String viewName = authController.editProfile(user, userDetails, model);

        assertEquals("editprofile", viewName);
        verify(userService, times(1)).verifyPassword(any(User.class));
        verify(userService, times(1)).updateUser(eq("username"), any(User.class));
    }

    @Test
    public void testGetRegisterPage() {
        String viewName = authController.getRegisterPage(model);

        assertEquals("register", viewName);
        verify(model).addAttribute(eq("user"), any(User.class));
    }

    @Test
    public void testRegister_PasswordsDoNotMatch() {
        User user = new User();
        user.setPassword("password123");
        user.setConfirmPassword("password456");

        when(userService.verifyPassword(any(User.class))).thenReturn(false);

        String viewName = authController.register(user, model);

        assertEquals("register", viewName);
        verify(model, times(1)).addAttribute("passwordError", "Passwords do not match");
        verify(userService, times(1)).verifyPassword(any(User.class));
        verify(userService, never()).createUser(any(User.class));
    }

    @Test
    public void testRegister_PasswordsMatch() {
        User user = new User();
        user.setPassword("password123");
        user.setConfirmPassword("password123");

        when(userService.verifyPassword(any(User.class))).thenReturn(true);
        when(userService.createUser(any(User.class))).thenReturn(user);

        String viewName = authController.register(user, model);

        assertEquals("redirect:/login", viewName);
        verify(userService, times(1)).verifyPassword(any(User.class));
        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    public void testRegister_UserCreationFails() {
        User user = new User();
        user.setPassword("password123");
        user.setConfirmPassword("password123");

        when(userService.verifyPassword(any(User.class))).thenReturn(true);
        when(userService.createUser(any(User.class))).thenReturn(null);

        String viewName = authController.register(user, model);

        assertEquals("register", viewName);
        verify(userService, times(1)).verifyPassword(any(User.class));
        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    public void testLogin() {
        String viewName = authController.login();
        assertEquals("login", viewName);
    }
}