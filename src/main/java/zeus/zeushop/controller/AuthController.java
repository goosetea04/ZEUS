package zeus.zeushop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import zeus.zeushop.model.User;
import zeus.zeushop.service.UserService;

@Controller
public class AuthController {
    private final UserService userService;
    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }
    /*
    @GetMapping("/")
    public String dashboard() {
        return "Welcome to dashboard";
    }

    @GetMapping("/admin")
    public String admin() {
        return "You can view this only if you are admin";
    }

    @GetMapping("/user")
    public String staff() {
        return "If you are admin or user, you can access this page.";
    }
    */

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user) {
        User newUser = userService.createUser(user);
        return newUser != null ? "redirect:/login" : "register";
    }

}