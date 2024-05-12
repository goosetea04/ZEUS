package zeus.zeushop.service;
import zeus.zeushop.model.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface UserService {
    public User createUser(User user);
    public List<User> getAllUsers();
    public Optional<User> getUserById(Integer id);
    public User getUserByUsername(String username);
    public User updateUser(Integer id, User userDetails);
    public void deleteUser(Integer id);
    User updateUserBalance(Integer id, BigDecimal newBalance);
}