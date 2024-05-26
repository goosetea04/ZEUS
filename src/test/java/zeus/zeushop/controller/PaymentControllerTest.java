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
    private UserService userService;


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

    @Test
    void paymentStatus() {
        String viewName = paymentController.paymentStatus("Success", "Error", model);
        assertEquals("payment-status", viewName);
        verify(model).addAttribute("successMessage", "Success");
        verify(model).addAttribute("errorMessage", "Error");
        verifyNoMoreInteractions(model);
    }

}