package zeus.zeushop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zeus.zeushop.model.TopUp;
import zeus.zeushop.service.TopUpService;

import java.util.List;

@Controller
@RequestMapping("/topups")
public class TopUpController {

    private final TopUpService topUpService;

    @Autowired
    public TopUpController(TopUpService topUpService) {
        this.topUpService = topUpService;
    }

    // Display the form for new top-up
    @GetMapping("/new")
    public String showTopUpForm(Model model) {
        model.addAttribute("topUp", new TopUp()); // Add a new topUp to the model for form binding
        return "top-up-form";
    }

    // Handle the form submission and create a new top-up
    @PostMapping
    public String createTopUp(@ModelAttribute TopUp topUp, RedirectAttributes redirectAttributes) {
        // Set default status or perform logic to determine status
        topUp.setStatus("PENDING");

        topUpService.createTopUp(topUp);
        redirectAttributes.addFlashAttribute("message", "Top-up created successfully!");
        return "redirect:/topups";
    }

    // List all top-ups
    @GetMapping
    public String getAllTopUps(Model model) {
        List<TopUp> allTopUps = topUpService.getAllTopUps();
        model.addAttribute("topUps", allTopUps);
        return "user-top-ups"; // Assuming 'all-top-ups' is the view for listing all top-ups
    }

    // Get top-ups by user ID
    @GetMapping("/{userId}")
    public String getUserTopUps(@PathVariable String userId, Model model) {
        List<TopUp> userTopUps = topUpService.getUserTopUps(userId);
        model.addAttribute("topUps", userTopUps);
        return "user-top-ups"; // View for displaying top-ups of a specific user
    }

    // Delete a top-up and handle the redirection with a message
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

    // Cancel when the status is still pending
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
