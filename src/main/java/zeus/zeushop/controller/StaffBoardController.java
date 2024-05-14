package zeus.zeushop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zeus.zeushop.model.TopUp;
import zeus.zeushop.service.StaffBoardService;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/staff")
public class StaffBoardController {

    private final StaffBoardService staffBoardService;

    @Autowired
    public StaffBoardController(StaffBoardService staffBoardService) {
        this.staffBoardService = staffBoardService;
    }

    @PostMapping("/approve/{topUpId}")
    public String approveTopUp(@PathVariable String topUpId) {
        staffBoardService.approveTopUp(topUpId);
        return "redirect:../top-ups";
    }

    @GetMapping("/top-ups/{status}")
    public String getTopUpsByStatus(@PathVariable String status, Model model) {
        List<TopUp> topUps;
        if ("ALL".equals(status) || status == null) {
            topUps = staffBoardService.getAllTopUps();
        } else {
            topUps = staffBoardService.getTopUpsByStatus(status);
        }
        model.addAttribute("topUps", topUps);
        model.addAttribute("status", status);
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
