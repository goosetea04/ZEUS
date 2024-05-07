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
public class UserController {
    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    public UserController(UserService userService, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.userService = userService;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

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

    @GetMapping("/profile")
    public String getProfilePage(Model model) {
        model.addAttribute("user", new User());
        return "profile";
    }

    @GetMapping("/editprofile")
    public String getEditProfilePage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userDetailsServiceImpl.loadUserByUsername(userDetails.getUsername());
        model.addAttribute("user", user);
        return "editprofile";
    }

    @PostMapping("/editprofile")
    public String editProfile(@ModelAttribute("user") User user, @AuthenticationPrincipal UserDetails userDetails) {
        User oldUser = userDetailsServiceImpl.loadUserByUsername(userDetails.getUsername());
        Integer id = oldUser.getId();

        user.setId(id);
        User updateUser = userService.updateUser(user.getId(), user);

        Authentication auth = new UsernamePasswordAuthenticationToken(updateUser, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        return updateUser != null ? "profile" : "editprofile";
    }

}