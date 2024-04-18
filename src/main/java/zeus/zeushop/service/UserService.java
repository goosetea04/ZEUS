package zeus.zeushop.service;
import zeus.zeushop.model.User;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;


public interface UserService {
    public User createUser(User user);
    public List<User> getAllUsers();
    public Optional<User> getUserById(String id);
    public User updateUser(String id, User userDetails);
    public void deleteUser(String id);
}