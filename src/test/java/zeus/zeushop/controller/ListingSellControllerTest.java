package zeus.zeushop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

@ExtendWith(MockitoExtension.class)
class ListingSellControllerTest {

    @InjectMocks
    private ListingSellController listingSellController;

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

    private ListingSell listingSell;
    private User currentUser;

    @BeforeEach
    void setUp() {
        listingSell = new ListingSell();
        listingSell.setProduct_id(1);
        listingSell.setProduct_name("Mini Skirt");
        listingSell.setProduct_description("Lorem ipsum dolor sit amet,");
        listingSell.setProduct_quantity(10);
        listingSell.setProduct_price(129000f);

        currentUser = new User();
        currentUser.setId(1);
        currentUser.setUsername("testUser");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("testUser");
        when(userService.getUserByUsername("testUser")).thenReturn(currentUser);
    }

    @Test
    void testCreateListingSellPage() {
        String viewName = listingSellController.createListingSellPage(model);
        assertEquals("createListingSell", viewName);
        verify(model).addAttribute("listingSell", new ListingSell());
    }

    @Test
    void testCreateListingSellPost() {
        when(listingSellService.create(any(ListingSell.class))).thenReturn(listingSell);
        String viewName = listingSellController.createListingSellPost(listingSell, redirectAttributes);
        assertEquals("redirect:/sell-list", viewName);
        verify(redirectAttributes).addFlashAttribute("successMessage", "Listing created successfully.");
        verify(listingSellService).create(listingSell);
    }

    @Test
    void testListingSellPage() {
        List<ListingSell> listingSells = new ArrayList<>();
        listingSells.add(listingSell);

        when(listingSellService.findBySellerId(1)).thenReturn(listingSells);

        String viewName = listingSellController.listingSellPage(model);

        assertEquals("sell", viewName);
        verify(model).addAttribute("listingSells", listingSells);
    }

    @Test
    void testDeleteListingSell() {
        doNothing().when(listingSellService).deleteListingSell(1);
        String viewName = listingSellController.deleteListingSell(1, redirectAttributes);
        assertEquals("redirect:/sell-list", viewName);
        verify(redirectAttributes).addFlashAttribute("successMessage", "Listing deleted successfully.");
        verify(listingSellService).deleteListingSell(1);
    }

    @Test
    void testEditListingSellPage() {
        when(listingSellService.findById(1)).thenReturn(Optional.of(listingSell));
        String viewName = listingSellController.editListingSellPage(1, model);
        assertEquals("editListingSell", viewName);
        verify(model).addAttribute("listingSell", listingSell);
    }

    @Test
    void testEditListingSellPageNotFound() {
        when(listingSellService.findById(1)).thenReturn(Optional.empty());
        String viewName = listingSellController.editListingSellPage(1, model);
        assertEquals("redirect:/sell-list", viewName);
    }

    @Test
    void testEditListingSellPost() {
        when(listingSellService.editListingSell(eq(1), any(ListingSell.class))).thenReturn(listingSell);
        String viewName = listingSellController.editListingSellPost(1, listingSell, redirectAttributes);
        assertEquals("redirect:/sell-list", viewName);
        verify(redirectAttributes).addFlashAttribute("successMessage", "Listing updated successfully.");
        verify(listingSellService).editListingSell(1, listingSell);
    }
}