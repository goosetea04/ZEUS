package zeus.zeushop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import zeus.zeushop.model.Payment;
import zeus.zeushop.model.TopUp;
import zeus.zeushop.model.User;
import zeus.zeushop.service.StaffBoardService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import zeus.zeushop.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/staff")
public class StaffBoardController {
    private final StaffBoardService staffBoardService;
    private UserService userService;
    @Autowired
    public StaffBoardController(StaffBoardService staffBoardService, UserService userService) {
        this.userService = userService;
        this.staffBoardService = staffBoardService;
    }


    @PostMapping("/top-ups/approve/{topUpId}")
    public String approveTopUp(@PathVariable String topUpId) {
        User admin = userService.getUserByUsername(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());
        if (admin == null || !admin.getRole().equals("ADMIN")) {
            return "redirect:/listings";
        }else {
            staffBoardService.approveTopUp(topUpId);
            return "redirect:../../top-ups";
        }
    }

    @PostMapping("/payments/approve/{id}")
    public String approvePayment(@PathVariable Long id) {
        User admin = userService.getUserByUsername(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());
        if (admin == null || !admin.getRole().equals("ADMIN")) {
            return "redirect:/listings";
        }else {
            staffBoardService.approvePayment(id);
            return "redirect:../../payments";
        }
    }
    @GetMapping("")
    public String staffHome() {
        User admin = userService.getUserByUsername(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());
        if (admin == null || !admin.getRole().equals("ADMIN")) {
            return "redirect:/listings";
        }else {
            return "staffdashboard";
        }
    }
    @GetMapping("/top-ups/{status}")
    public String getTopUpsByStatus(@PathVariable String status, Model model) {
        User admin = userService.getUserByUsername(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());
        if (admin == null || !admin.getRole().equals("ADMIN")) {
            return "redirect:/listings";
        }else {
            List<TopUp> topUps;
            if ("ALL".equals(status) || status == null) {
                topUps = staffBoardService.getAllTopUps();
            } else {
                topUps = staffBoardService.getTopUpsByStatus(status);
            }
            model.addAttribute("topUps", topUps);
            model.addAttribute("status", status);
            return "staffdashboard-topup";
        }
    }
    @GetMapping("/payments/{status}")
    public String getPaymentsByStatus(@PathVariable String status, Model model) {
        User admin = userService.getUserByUsername(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());
        if (admin == null || !admin.getRole().equals("ADMIN")) {
            return "redirect:/listings";
        }else {
            List<Payment> payments;
            if ("ALL".equals(status) || status == null) {
                payments = staffBoardService.getAllPayments();
            } else {
                payments = staffBoardService.getPaymentsByStatus(status);
            }
            model.addAttribute("payments", payments);
            model.addAttribute("status", status);
            return "staffdashboard-payments";
        }
    }

    @GetMapping("/top-ups")
    public String getAllTopUps(Model model) {
        User admin = userService.getUserByUsername(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());
        if (admin == null || !admin.getRole().equals("ADMIN")) {
            return "redirect:/listings";
        }else {
            List<TopUp> topUps = staffBoardService.getAllTopUps();
            model.addAttribute("topUps", topUps);// Change attribute name to "topUps"
            return "staffdashboard-topup";
        }
    }

    @GetMapping("/payments")
    public String getAllPayments(Model model) {
        User admin = userService.getUserByUsername(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());
        if (admin == null || !admin.getRole().equals("ADMIN")) {
            return "redirect:/listings";
        }else {
            List<Payment> payments = staffBoardService.getAllPayments();
            model.addAttribute("payments", payments); // Change attribute name to "topUps"
            return "staffdashboard-payments";
        }
    }
}