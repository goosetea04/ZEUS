package zeus.zeushop.service;
import org.springframework.security.crypto.password.PasswordEncoder;
import zeus.zeushop.model.User;
import zeus.zeushop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user) {
        if(user.getUsername() == null || user.getPassword() == null) {
            return null;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User updateUser(String username, User userDetails) {
        try {
            User user = userRepository.findByUsername(username);
            if (user == null) {
                return null;
            }
            if (userDetails.getUsername() != null && !userDetails.getUsername().isBlank()) {
                user.setUsername(userDetails.getUsername());
            }
            if (userDetails.getPassword() != null && !userDetails.getPassword().isBlank()) {
                user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            }
            return userRepository.save(user);
        } catch (Exception e) {
            System.out.println("Error updating user: " + e.getMessage());
            return null;
        }
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    public Boolean verifyPassword(User user) {
        return user.getPassword().equals(user.getConfirmPassword());
    }

    public User updateUserBalance(Integer id, BigDecimal newBalance) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();
            existingUser.setBalance(newBalance);
            userRepository.save(existingUser);
            return existingUser;
        }
        return null;
    }

}