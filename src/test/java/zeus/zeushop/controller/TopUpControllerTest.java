package zeus.zeushop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zeus.zeushop.model.TopUp;
import zeus.zeushop.model.User;
import zeus.zeushop.service.TopUpService;
import zeus.zeushop.service.UserService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TopUpControllerTest {

    @Mock
    private TopUpService topUpService;

    @Mock
    private UserService userService;

    @InjectMocks
    private TopUpController topUpController;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        // Only setup necessary for all tests
        SecurityContextHolder.getContext().setAuthentication(authentication);
        lenient().when(authentication.getName()).thenReturn("user");
    }


    @Test
    void testCreateTopUp_Positive() {
        TopUp topUp = new TopUp();
        topUp.setAmount(500);
        when(authentication.getName()).thenReturn("user");
        String redirect = topUpController.createTopUp(topUp, redirectAttributes);
        assertEquals("redirect:/topups", redirect);
        verify(topUpService).createTopUp(topUp);
        verify(redirectAttributes).addFlashAttribute("message", "Top-up created successfully!");
    }

    @Test
    void testCreateTopUp_NegativeAmount() {
        TopUp topUp = new TopUp();
        topUp.setAmount(-100);
        String redirect = topUpController.createTopUp(topUp, redirectAttributes);
        assertEquals("redirect:/topups/new", redirect);
        verify(redirectAttributes).addFlashAttribute("error", "Top up amount cannot be negative.");
    }

    @Test
    void testGetUserTopUps() {
        List<TopUp> userTopUps = new ArrayList<>();
        User user = new User();
        user.setBalance(BigDecimal.valueOf(1000));  // Convert int to BigDecimal
        when(userService.getUserByUsername("user")).thenReturn(user);
        when(topUpService.getUserTopUps("user")).thenReturn(userTopUps);
        String viewName = topUpController.getUserTopUps(model);
        assertEquals("user-top-ups", viewName);
        verify(model).addAttribute("topUps", userTopUps);
        verify(model).addAttribute("balance", BigDecimal.valueOf(1000));  // Ensure BigDecimal is used here too
    }


    @Test
    void testDeleteTopUp_Success() {
        String topUpId = "1";
        when(topUpService.deleteTopUp(topUpId)).thenReturn(true);
        lenient().when(authentication.getName()).thenReturn("user");
        String redirect = topUpController.deleteTopUp(topUpId, redirectAttributes);
        assertEquals("redirect:/topups", redirect);
        verify(redirectAttributes).addFlashAttribute("message", "Top-up deleted successfully.");
    }


    @Test
    void testDeleteTopUp_Failure() {
        when(topUpService.deleteTopUp("1")).thenReturn(false);
        String redirect = topUpController.deleteTopUp("1", redirectAttributes);
        assertEquals("redirect:/topups", redirect);
        verify(redirectAttributes).addFlashAttribute("error", "Top-up not found or could not be deleted.");
    }

    @Test
    void testCancelTopUp_Success() {
        when(topUpService.cancelTopUp("2")).thenReturn(true);
        String redirect = topUpController.cancelTopUp("2", redirectAttributes);
        assertEquals("redirect:/topups", redirect);
        verify(redirectAttributes).addFlashAttribute("message", "Top-up cancelled successfully.");
    }

    @Test
    void testCancelTopUp_Failure() {
        String topUpId = "2";
        when(topUpService.cancelTopUp(topUpId)).thenReturn(false);
        lenient().when(authentication.getName()).thenReturn("user");
        String redirect = topUpController.cancelTopUp(topUpId, redirectAttributes);
        assertEquals("redirect:/topups", redirect);
        verify(redirectAttributes).addFlashAttribute("error", "Top-up cannot be cancelled or not found.");
    }

}
