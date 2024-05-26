package zeus.zeushop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zeus.zeushop.model.ListingSell;
import zeus.zeushop.model.User;
import zeus.zeushop.service.ListingSellService;
import zeus.zeushop.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ListingSellControllerTest {

    @Mock
    private ListingSellService listingSellService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private ListingSellController listingSellController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        User currentUser = new User();
        currentUser.setId(1);
        currentUser.setUsername("testUser");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("testUser");
        when(userService.getUserByUsername("testUser")).thenReturn(currentUser);
    }

    @Test
    void testCreateListingSellPage() {
        String expectedViewName = "createListingSell";

        String actualViewName = listingSellController.createListingSellPage(model);

        assertEquals(expectedViewName, actualViewName);
        verify(model).addAttribute(eq("listingSell"), any(ListingSell.class));
    }

    @Test
    void testCreateListingSellPost() {
        ListingSell listingSell = new ListingSell();
        when(listingSellService.create(any(ListingSell.class))).thenReturn(listingSell);

        String expectedViewName = "redirect:/sell-list";

        String actualViewName = listingSellController.createListingSellPost(listingSell, redirectAttributes);

        assertEquals(expectedViewName, actualViewName);
        verify(redirectAttributes).addFlashAttribute("successMessage", "Listing created successfully.");
        verify(listingSellService).create(listingSell);
    }

    @Test
    void testListingSellPage() {
        List<ListingSell> listingSells = new ArrayList<>();
        listingSells.add(new ListingSell());

        when(listingSellService.findBySellerId(1)).thenReturn(listingSells);

        String expectedViewName = "sell";

        String actualViewName = listingSellController.listingSellPage(model);

        assertEquals(expectedViewName, actualViewName);
        verify(model).addAttribute("listingSells", listingSells);
    }

    @Test
    void testDeleteListingSell() {
        String expectedViewName = "redirect:/sell-list";

        String actualViewName = listingSellController.deleteListingSell(1, redirectAttributes);

        assertEquals(expectedViewName, actualViewName);
        verify(redirectAttributes).addFlashAttribute("successMessage", "Listing deleted successfully.");
        verify(listingSellService).deleteListingSell(1);
    }

    @Test
    void testEditListingSellPage() {
        ListingSell listingSell = new ListingSell();
        when(listingSellService.findById(1)).thenReturn(Optional.of(listingSell));

        String expectedViewName = "editListingSell";

        String actualViewName = listingSellController.editListingSellPage(1, model);

        assertEquals(expectedViewName, actualViewName);
        verify(model).addAttribute("listingSell", listingSell);
    }

    @Test
    void testEditListingSellPageNotFound() {
        when(listingSellService.findById(1)).thenReturn(Optional.empty());

        String expectedViewName = "redirect:/sell-list";

        String actualViewName = listingSellController.editListingSellPage(1, model);

        assertEquals(expectedViewName, actualViewName);
    }

    @Test
    void testEditListingSellPost() {
        ListingSell listingSell = new ListingSell();
        when(listingSellService.editListingSell(eq(1), any(ListingSell.class))).thenReturn(listingSell);

        String expectedViewName = "redirect:/sell-list";

        String actualViewName = listingSellController.editListingSellPost(1, listingSell, redirectAttributes);

        assertEquals(expectedViewName, actualViewName);
        verify(redirectAttributes).addFlashAttribute("successMessage", "Listing updated successfully.");
        verify(listingSellService).editListingSell(1, listingSell);
    }
}