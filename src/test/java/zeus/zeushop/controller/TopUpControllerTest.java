
/*
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

    @BeforeEach
    void setUp() {
        model = mock(Model.class);
        redirectAttributes = mock(RedirectAttributes.class);
    }

    @Test
    void testShowTopUpForm() {
        String viewName = topUpController.showTopUpForm(model);
        assertEquals("top-up-form", viewName);
        verify(model).addAttribute(eq("topUp"), any(TopUp.class));
    }

    @Test
    void testCreateTopUp() {
        TopUp topUp = new TopUp();
        String redirect = topUpController.createTopUp(topUp, redirectAttributes);
        assertEquals("redirect:/topups", redirect);
        verify(topUpService).createTopUp(topUp);
        verify(redirectAttributes).addFlashAttribute(eq("message"), eq("Top-up created successfully!"));
    }

    @Test
    void testGetAllTopUps() {
        List<TopUp> allTopUps = new ArrayList<>();
        when(topUpService.getAllTopUps()).thenReturn(allTopUps);

        String viewName = topUpController.getAllTopUps(model);
        assertEquals("user-top-ups", viewName);
        verify(model).addAttribute("topUps", allTopUps);
    }

    @Test
    void testGetUserTopUps() {
        List<TopUp> userTopUps = new ArrayList<>();
        when(topUpService.getUserTopUps("123")).thenReturn(userTopUps);

        String viewName = topUpController.getUserTopUps("123", model);
        assertEquals("user-top-ups", viewName);
        verify(model).addAttribute("topUps", userTopUps);
    }

    @Test
    void testDeleteTopUp() {
        when(topUpService.deleteTopUp("1")).thenReturn(true);

        String redirect = topUpController.deleteTopUp("1", redirectAttributes);
        assertEquals("redirect:/topups", redirect);
        verify(redirectAttributes).addFlashAttribute("message", "Top-up deleted successfully.");
        verify(topUpService).deleteTopUp("1");
    }

    @Test
    void testDeleteTopUp_Failure() {
        when(topUpService.deleteTopUp("1")).thenReturn(false);

        String redirect = topUpController.deleteTopUp("1", redirectAttributes);
        assertEquals("redirect:/topups", redirect);
        verify(redirectAttributes).addFlashAttribute("error", "Top-up not found or could not be deleted.");
        verify(topUpService).deleteTopUp("1");
    }
}
*/

