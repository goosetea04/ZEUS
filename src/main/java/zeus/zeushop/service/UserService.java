package zeus.zeushop.service;
import zeus.zeushop.model.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface UserService {
    public User createUser(User user);
    public List<User> getAllUsers();
    public User getUserById(Integer id);
    public User getUserByUsername(String username);
    public User updateUser(String username, User userDetails);
    public void deleteUser(Integer id);
    public Boolean verifyPassword(User user);
    User updateUserBalance(Integer id, BigDecimal newBalance);
}