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
import zeus.zeushop.service.UserDetailsServiceImpl;
import zeus.zeushop.service.UserService;

import java.util.Collections;

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
    @WithMockUser
    public void testEditProfile() {
        User user = new User();
        String oldUsername = "dummy";
        user.setId(1);
        user.setUsername(oldUsername);

        User updateUser = new User();
        String newUsername = "newDummy";
        updateUser.setId(1);
        updateUser.setUsername(newUsername);

        when(userService.updateUser(eq(1), any(User.class))).thenReturn(updateUser);

        User updatedUser = userService.updateUser(1, user);

        assertEquals(newUsername, updatedUser.getUsername());

        verify(userService, times(1)).updateUser(eq(1), eq(user));

        assertNotEquals(user.getUsername(), updatedUser.getUsername());
    }
}