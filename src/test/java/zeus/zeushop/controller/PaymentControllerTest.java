package zeus.zeushop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zeus.zeushop.model.User;
import org.springframework.ui.Model;
import zeus.zeushop.service.PaymentService;
import zeus.zeushop.service.ShoppingCartService;
import zeus.zeushop.service.UserService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class PaymentControllerTest {

    @InjectMocks
    private PaymentController paymentController;

    @Mock
    private PaymentService paymentService;

    @Mock
    private UserService userService;

    @Mock
    private ShoppingCartService shoppingCartService;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;
    @Mock
    private Model model;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user");
    }

    @Test
    public void testInitiatePayment_UserNotFound() {
        when(userService.getUserByUsername(anyString())).thenReturn(null);
        String view = paymentController.initiatePayment(new BigDecimal("100.00"), redirectAttributes);
        verify(redirectAttributes).addFlashAttribute("error", "User not found.");
        assertEquals("redirect:/payment/status", view);
    }

    @Test
    public void testInitiatePayment_AmountNotPositive() {
        User user = new User();
        user.setBalance(new BigDecimal("1000.00"));
        when(userService.getUserByUsername(anyString())).thenReturn(user);
        String view = paymentController.initiatePayment(new BigDecimal("-100.00"), redirectAttributes);
        verify(redirectAttributes).addFlashAttribute("error", "Payment amount must be positive.");
        assertEquals("redirect:/payment/status", view);
    }

    @Test
    public void testInitiatePayment_InsufficientBalance() {
        User user = new User();
        user.setBalance(new BigDecimal("50.00"));
        when(userService.getUserByUsername(anyString())).thenReturn(user);
        String view = paymentController.initiatePayment(new BigDecimal("100.00"), redirectAttributes);
        verify(redirectAttributes).addFlashAttribute("error", "Insufficient balance.");
        assertEquals("redirect:/payment/status", view);
    }

    @Test
    public void testInitiatePayment_Success() {
        User user = new User();
        user.setBalance(new BigDecimal("1000.00"));
        when(userService.getUserByUsername(anyString())).thenReturn(user);
        String view = paymentController.initiatePayment(new BigDecimal("100.00"), redirectAttributes);
        verify(paymentService).createPayment(user.getId(), new BigDecimal("100.00"));
        verify(shoppingCartService).clearCartItemsByBuyerId(user.getId());
        verify(redirectAttributes).addFlashAttribute("success", "Payment initiated successfully.");
        assertEquals("redirect:/payment/status", view);
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
