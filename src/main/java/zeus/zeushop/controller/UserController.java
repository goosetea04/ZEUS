package zeus.zeushop.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import zeus.zeushop.model.User;
import zeus.zeushop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable String id) { return userService.getUserById(id); }

    @GetMapping("/profile")
    public String getProfilePage(Model model) {
        model.addAttribute("name", "Clay");
        model.addAttribute("username", "cl4ytn");
        model.addAttribute("email", "cl4ytn@gmail.com");
        return "profile";
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable String id, @RequestBody User userDetails) {
        return userService.updateUser(id, userDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }
}