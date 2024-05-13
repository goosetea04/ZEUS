package zeus.zeushop.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zeus.zeushop.model.User;
import zeus.zeushop.model.Payment;
import zeus.zeushop.service.PaymentService;
import zeus.zeushop.service.UserService;

import java.math.BigDecimal;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserService userService;

    @PostMapping("/initiate")
    public String initiatePayment(@RequestParam("amount") BigDecimal amount, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User user = userService.getUserByUsername(currentUsername);

        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "User not found.");
            return "redirect:/payment/status";
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            redirectAttributes.addFlashAttribute("error", "Payment amount must be positive.");
            return "redirect:/payment/status";
        }

        if (user.getBalance().compareTo(amount) >= 0) {
            paymentService.createPayment(user.getId(), amount);
            redirectAttributes.addFlashAttribute("success", "Payment initiated successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Insufficient balance.");
        }
        return "redirect:/payment/status";
    }

    @GetMapping("/status")
    public String paymentStatus(@ModelAttribute("success") String successMsg, @ModelAttribute("error") String errorMsg, Model model) {
        model.addAttribute("successMessage", successMsg);
        model.addAttribute("errorMessage", errorMsg);
        return "payment-status";
    }
}
