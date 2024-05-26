package zeus.zeushop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zeus.zeushop.model.User;
import zeus.zeushop.service.PaymentService;
import zeus.zeushop.service.ShoppingCartService;
import zeus.zeushop.service.UserService;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @Mock
    private UserService userService;

    @Mock
    private ShoppingCartService shoppingCartService;

    @InjectMocks
    private PaymentController paymentController;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn("user");
    }

    @Test
    void initiatePayment_UserNotFound() {
        when(userService.getUserByUsername("user")).thenReturn(null);

        String viewName = paymentController.initiatePayment(BigDecimal.valueOf(100), redirectAttributes);

        assertEquals("redirect:/payment/status", viewName);
        verify(redirectAttributes).addFlashAttribute("error", "User not found.");
    }

//    @Test
//    void initiatePayment_NegativeAmount() {
//        when(userService.getUserByUsername("user")).thenReturn(new User());
//
//        String viewName = paymentController.initiatePayment(BigDecimal.valueOf(-100), redirectAttributes);
//
//        assertEquals("redirect:/payment/status", viewName);
//        verify(redirectAttributes).addFlashAttribute("error", "Payment amount must be positive.");
//    }

//    @Test
//    void initiatePayment_InsufficientBalance() {
//        User user = new User();
//        user.setId(10);
//        user.setBalance(BigDecimal.ZERO);
//        when(userService.getUserByUsername("user")).thenReturn(user);
//
//        String viewName = paymentController.initiatePayment(BigDecimal.valueOf(50), redirectAttributes);
//
//        assertEquals("redirect:/payment/status", viewName);
//        verify(redirectAttributes).addFlashAttribute("error", "Insufficient balance.");
//    }

//    @Test
//    void initiatePayment_Successful() {
//        User user = new User();
//        user.setId(10);
//        user.setBalance(BigDecimal.valueOf(100));
//        when(userService.getUserByUsername("user")).thenReturn(user);
//
//        String viewName = paymentController.initiatePayment(BigDecimal.valueOf(50), redirectAttributes);
//
//        assertEquals("redirect:/payment/status", viewName);
//        verify(paymentService).createPayment(10, BigDecimal.valueOf(50));
//        verify(shoppingCartService).clearCartItemsByBuyerId(10);
//        verify(redirectAttributes).addFlashAttribute("success", "Payment initiated successfully.");
//    }


    @Test
    void paymentStatus() {
        String viewName = paymentController.paymentStatus("Success", "Error", model);
        assertEquals("payment-status", viewName);
        verify(model).addAttribute("successMessage", "Success");
        verify(model).addAttribute("errorMessage", "Error");
        verifyNoMoreInteractions(model);
    }

}