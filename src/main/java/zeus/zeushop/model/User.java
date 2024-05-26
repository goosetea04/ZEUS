package zeus.zeushop.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "user", schema = "public")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private String role = "USER";

    @Column(name = "balance", nullable = false, precision = 10, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO; // Default balance set to 0

    @Transient
    private String confirmPassword;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
        authorityList.add(authority);
        return authorityList;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static class Builder {
        private final User user;

        public Builder() {
            user = new User();
        }

        public Builder withId(Integer id) {
            user.setId(id);
            return this;
        }

        public Builder withUsername(String username) {
            user.setUsername(username);
            return this;
        }

        public Builder withPassword(String password) {
            user.setPassword(password);
            return this;
        }

        public Builder withRole(String role) {
            user.setRole(role);
            return this;
        }

        public Builder withBalance(BigDecimal balance) {
            user.setBalance(balance);
            return this;
        }

        public Builder withConfirmPassword(String confirmPassword) {
            user.setConfirmPassword(confirmPassword);
            return this;
        }

        public User build() {
            return user;
        }
    }

}