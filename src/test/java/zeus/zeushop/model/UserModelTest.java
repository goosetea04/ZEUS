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

import java.math.BigDecimal;
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
    public void testUserBuilder() {
        User user = new User.Builder()
                .withUsername("john")
                .withPassword("password")
                .withRole("ADMIN")
                .withBalance(BigDecimal.valueOf(1000))
                .withConfirmPassword("password")
                .build();

        assertEquals("john", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("ADMIN", user.getRole());
        assertEquals(BigDecimal.valueOf(1000), user.getBalance());
        assertEquals("password", user.getConfirmPassword());
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

    @Test
    public void testLoadByUsername_ExistingUser() {
        User user = new User();
        user.setUsername("dummy");

        when(userRepository.findByUsername(eq("dummy"))).thenReturn(user);

        User result = userDetailsServiceImpl.loadUserByUsername("dummy");

        assertEquals(user, result);
    }

    @Test
    public void testLoadByUsername_UserNotFound() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsServiceImpl.loadUserByUsername("dummy");
        });
    }
}
