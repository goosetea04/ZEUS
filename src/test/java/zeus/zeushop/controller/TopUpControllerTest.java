package zeus.zeushop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zeus.zeushop.model.TopUp;
import zeus.zeushop.service.TopUpService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TopUpControllerTest {

    @Mock
    private TopUpService topUpService;

    @InjectMocks
    private TopUpController topUpController;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Test
    void testShowTopUpForm() {
        String viewName = topUpController.showTopUpForm();
        assertEquals("top-up-form", viewName);
    }

    @Test
    void testCreateTopUp() {
        TopUp topUp = new TopUp();
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        String redirectUrl = topUpController.createTopUp(topUp, redirectAttributes);
        verify(topUpService).createTopUp(topUp);
        assertEquals("redirect:/topups", redirectUrl);
    }

    @Test
    void testGetAllTopUps() {
        List<TopUp> topUps = new ArrayList<>();
        when(topUpService.getAllTopUps()).thenReturn(topUps);

        String viewName = topUpController.getAllTopUps(model);
        verify(model).addAttribute("topUps", topUps);
        assertEquals("user-top-ups", viewName);
    }

    @Test
    void testGetUserTopUps() {
        String userId = "U123";
        List<TopUp> userTopUps = new ArrayList<>();
        when(topUpService.getUserTopUps(userId)).thenReturn(userTopUps);

        String viewName = topUpController.getUserTopUps(userId, model);
        verify(model).addAttribute("topUps", userTopUps);
        assertEquals("user-top-ups", viewName);
    }

    @Test
    void testDeleteTopUp_Success() {
        String topUpId = "T001";
        when(topUpService.deleteTopUp(topUpId)).thenReturn(true);

        String redirect = topUpController.deleteTopUp(topUpId, redirectAttributes);
        verify(redirectAttributes).addFlashAttribute("message", "Top-up deleted successfully.");
        assertEquals("redirect:/topups", redirect);
    }

    @Test
    void testDeleteTopUp_Failure() {
        String topUpId = "T002";
        when(topUpService.deleteTopUp(topUpId)).thenReturn(false);

        String redirect = topUpController.deleteTopUp(topUpId, redirectAttributes);
        verify(redirectAttributes).addFlashAttribute("error", "Top-up not found or could not be deleted.");
        assertEquals("redirect:/topups", redirect);
    }
}
