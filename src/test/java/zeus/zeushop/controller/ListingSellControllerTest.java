package zeus.zeushop.controller;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import zeus.zeushop.model.ListingSell;
import zeus.zeushop.model.User;
import zeus.zeushop.service.ListingSellService;
import zeus.zeushop.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ListingSellControllerTest {

    @Mock
    private ListingSellService listingSellService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ListingSellController listingSellController;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    private Model model;
    private RedirectAttributes redirectAttributes;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        model = new BindingAwareModelMap();
        redirectAttributes = new RedirectAttributesModelMap();
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("user");
    }

    @Test
    public void testCreateListingSellPage() {
        String viewName = listingSellController.createListingSellPage(model);
        assertEquals("createListingSell", viewName);
        assertTrue(model.containsAttribute("listingSell"));
    }

    @Test
    public void testCreateListingSellPost() {
        User currentUser = new User();
        currentUser.setId(1);
        when(userService.getUserByUsername("user")).thenReturn(currentUser);

        ListingSell listingSell = new ListingSell();
        String viewName = listingSellController.createListingSellPost(listingSell, redirectAttributes);
        verify(listingSellService).create(listingSell);
        assertEquals("redirect:/sell-list", viewName);
        assertEquals("Listing created successfully.", redirectAttributes.getFlashAttributes().get("successMessage"));
    }

    @Test
    public void testListingSellPage() {
        User currentUser = new User();
        currentUser.setId(1);
        when(userService.getUserByUsername("user")).thenReturn(currentUser);

        List<ListingSell> listingSells = new ArrayList<>();
        when(listingSellService.findBySellerId(1)).thenReturn(listingSells);

        String viewName = listingSellController.listingSellPage(model);
        assertEquals("sell", viewName);
        assertEquals(listingSells, model.getAttribute("listingSells"));
    }

    @Test
    public void testDeleteListingSell() {
        String viewName = listingSellController.deleteListingSell(1, redirectAttributes);
        verify(listingSellService).deleteListingSell(1);
        assertEquals("redirect:/sell-list", viewName);
        assertEquals("Listing deleted successfully.", redirectAttributes.getFlashAttributes().get("successMessage"));
    }

    @Test
    public void testEditListingSellPage() {
        Optional<ListingSell> optionalListingSell = Optional.of(new ListingSell());
        when(listingSellService.findById(1)).thenReturn(optionalListingSell);

        String viewName = listingSellController.editListingSellPage(1, model);
        assertEquals("editListingSell", viewName);
        assertTrue(model.containsAttribute("listingSell"));
    }

    @Test
    public void testEditListingSellPageNotFound() {
        when(listingSellService.findById(1)).thenReturn(Optional.empty());
        String viewName = listingSellController.editListingSellPage(1, model);
        assertEquals("redirect:/sell-list", viewName);
    }

    @Test
    public void testEditListingSellPost() {
        ListingSell listingSell = new ListingSell();
        String viewName = listingSellController.editListingSellPost(1, listingSell, redirectAttributes);
        verify(listingSellService).editListingSell(1, listingSell);
        assertEquals("redirect:/sell-list", viewName);
        assertEquals("Listing updated successfully.", redirectAttributes.getFlashAttributes().get("successMessage"));
    }
}
