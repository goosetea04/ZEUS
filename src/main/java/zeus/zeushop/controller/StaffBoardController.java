package zeus.zeushop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zeus.zeushop.model.TopUp;
import zeus.zeushop.service.StaffBoardService;

import java.util.List;

@Controller
@RequestMapping("/staff")
public class StaffBoardController {

    private final StaffBoardService staffBoardService;

    @Autowired
    public StaffBoardController(StaffBoardService staffBoardService) {
        this.staffBoardService = staffBoardService;
    }

    @PostMapping("/approve/{topUpId}")
    public String approveTopUp(@PathVariable String topUpId, RedirectAttributes redirectAttributes) {
        boolean success = staffBoardService.approveTopUp(topUpId);
        redirectAttributes.addFlashAttribute("message", success ? "Top-up approved successfully." : "Failed to approve top-up. It may not exist or is not pending.");
        return "redirect:staffdashboard";
    }

    @GetMapping("/top-ups/status/{status}")
    public String getTopUpsByStatus(@PathVariable String status, Model model) {
        List<TopUp> topUps = staffBoardService.getTopUpsByStatus(status);
        model.addAttribute("topUps", topUps); // Change attribute name to "topUps"
        return "staffdashboard";
    }

    @GetMapping("/top-ups")
    public String getAllTopUps(Model model) {
        List<TopUp> topUps = staffBoardService.getAllTopUps();
        model.addAttribute("topUps", topUps); // Change attribute name to "topUps"
        return "staffdashboard";
    }

    @GetMapping("/user/{userId}/top-ups")
    public String getUserTopUps(@PathVariable String userId, Model model) {
        List<TopUp> topUps = staffBoardService.getUserTopUps(userId);
        model.addAttribute("topUps", topUps); // Change attribute name to "topUps"
        return "staffdashboard";
    }
}
