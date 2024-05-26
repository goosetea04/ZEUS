package zeus.zeushop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import zeus.zeushop.model.User;
import zeus.zeushop.service.UserDetailsServiceImpl;
import zeus.zeushop.service.UserService;

@Controller
public class AuthController {
    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    public AuthController(UserService userService, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.userService = userService;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user, Model model) {
        if (!userService.verifyPassword(user)) {
            model.addAttribute("passwordError", "Passwords do not match");
            return "register";
        }
        User newUser = userService.createUser(user);
        return newUser != null ? "redirect:/login" : "register";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/profile")
    public String getProfilePage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userDetailsServiceImpl.loadUserByUsername(userDetails.getUsername());
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/editprofile")
    public String getEditProfilePage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userDetailsServiceImpl.loadUserByUsername(userDetails.getUsername());
        model.addAttribute("user", user);
        return "editprofile";
    }

    @PostMapping("/editprofile")
    public String editProfile(@ModelAttribute("user") User user, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (!userService.verifyPassword(user)) {
            model.addAttribute("passwordError", "Passwords do not match");
            return "editprofile";
        }
        User updateUser = userService.updateUser(userDetails.getUsername(), user);

        Authentication auth = new UsernamePasswordAuthenticationToken(updateUser, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        return updateUser != null ? "profile" : "editprofile";
    }

}