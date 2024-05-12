package zeus.zeushop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zeus.zeushop.model.TopUp;
import zeus.zeushop.model.User;
import zeus.zeushop.service.TopUpService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import zeus.zeushop.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/topups")
public class TopUpController {

    private final TopUpService topUpService;
    private final UserService userService;

    @Autowired
    public TopUpController(TopUpService topUpService, UserService userService) {

        this.topUpService = topUpService;
        this.userService = userService;
    }

    @GetMapping("/new")
    public String showTopUpForm(Model model) {
        model.addAttribute("topUp", new TopUp());  // Top-up model attribute for form binding
        return "top-up-form";
    }

    @PostMapping
    public String createTopUp(@ModelAttribute TopUp topUp, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        topUp.setUserId(currentUsername);
        if (topUp.getAmount() < 0) {
            redirectAttributes.addFlashAttribute("error", "Top up amount cannot be negative.");
            return "redirect:/topups/new";
        }
        topUp.setStatus("PENDING");

        topUpService.createTopUp(topUp);
        redirectAttributes.addFlashAttribute("message", "Top-up created successfully!");
        return "redirect:/topups";
    }

    // Modified to use authenticated user's ID
    @GetMapping
    public String getUserTopUps(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userService.getUserByUsername(currentUsername);
        List<TopUp> userTopUps = topUpService.getUserTopUps(currentUsername);
        model.addAttribute("topUps", userTopUps);
        model.addAttribute("balance", currentUser.getBalance());
        return "user-top-ups";
    }

    @PostMapping("/{topUpId}/delete")
    public String deleteTopUp(@PathVariable String topUpId, RedirectAttributes redirectAttributes) {
        boolean deleted = topUpService.deleteTopUp(topUpId);
        if (deleted) {
            redirectAttributes.addFlashAttribute("message", "Top-up deleted successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Top-up not found or could not be deleted.");
        }
        return "redirect:/topups";
    }

    @PostMapping("/{topUpId}/cancel")
    public String cancelTopUp(@PathVariable String topUpId, RedirectAttributes redirectAttributes) {
        boolean cancelled = topUpService.cancelTopUp(topUpId);
        if (cancelled) {
            redirectAttributes.addFlashAttribute("message", "Top-up cancelled successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Top-up cannot be cancelled or not found.");
        }
        return "redirect:/topups";
    }
}
