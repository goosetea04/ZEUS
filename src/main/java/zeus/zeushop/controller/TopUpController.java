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

    @GetMapping("/new")
    public String showTopUpForm() {
        return "top-up-form";
    }

    @PostMapping
    public String createTopUp(@ModelAttribute TopUp topUp) {
        topUpService.createTopUp(topUp);
        return "redirect:/topups";
    }

    @GetMapping
    public String getAllTopUps(Model model) {
        List<TopUp> allTopUps = topUpService.getAllTopUps();
        model.addAttribute("topUps", allTopUps);
        return "user-top-ups";
    }

    @GetMapping("/{userId}")
    public String getUserTopUps(@PathVariable String userId, Model model) {
        model.addAttribute("topUps", topUpService.getUserTopUps(userId));
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

}