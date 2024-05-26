package zeus.zeushop.model;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import zeus.zeushop.repository.UserRepository;
import zeus.zeushop.service.UserDetailsServiceImpl;

import java.util.Collection;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserModelTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Test
    public void testUserFields() {
        User user = new User();
        user.setId(1);
        user.setUsername("dummy");
        user.setPassword("asdf");
        user.setRole("USER");

        assertEquals(1, user.getId());
        assertEquals("dummy", user.getUsername());
        assertEquals("asdf", user.getPassword());
        assertEquals("USER", user.getRole());
    }
    @Test
    public void testGetAuthorities() {
        User user = new User();
        user.setRole("ADMIN");

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ADMIN")));
    }

    @Test
    public void testIsAccountNonExpired() {
        User user = new User();
        assertTrue(user.isAccountNonExpired());
    }

    @Test
    public void testIsAccountNonLocked() {
        User user = new User();
        assertTrue(user.isAccountNonLocked());
    }

    @Test
    public void testIsCredentialsNonExpired() {
        User user = new User();
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    public void testIsEnabled() {
        User user = new User();
        assertTrue(user.isEnabled());
    }
    /*
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username);
    }
     */
    @Test
    public void testLoadByUsername_ExistingUser() {
        // Setup
        User user = new User();
        user.setUsername("dummy");

        // Mock the behavior of findByUsername to return a specific user when called with "dummy"
        when(userRepository.findByUsername(eq("dummy"))).thenReturn(user);

        // Action
        User result = userDetailsServiceImpl.loadUserByUsername("dummy");

        // Assertions
        assertEquals(user, result);
    }

    @Test
    public void testLoadByUsername_UserNotFound() {
        // Setup the behavior of findByUsername to return null when the user is not found
        when(userRepository.findByUsername(any(String.class))).thenReturn(null);

        // Action and Assertions
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsServiceImpl.loadUserByUsername("dummy");
        });
    }
}
