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
import zeus.zeushop.model.User;
import zeus.zeushop.service.StaffBoardService;
import zeus.zeushop.service.UserService;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class StaffBoardControllerTest {

    @InjectMocks
    private StaffBoardController staffBoardController;

    @Mock
    private StaffBoardService staffBoardService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    private void mockAdminUser() {
        User admin = new User();
        admin.setRole("ADMIN");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("admin");
        when(userService.getUserByUsername("admin")).thenReturn(admin);
    }

    private void mockNonAdminUser() {
        User user = new User();
        user.setRole("USER");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user");
        when(userService.getUserByUsername("user")).thenReturn(user);
    }

    // Positive test cases
    @Test
    public void testApproveTopUp_AdminUser_Positive() {
        mockAdminUser();
        String view = staffBoardController.approveTopUp("1");
        verify(staffBoardService, times(1)).approveTopUp("1");
        assertEquals("redirect:../../top-ups", view);
    }

    @Test
    public void testApprovePayment_AdminUser_Positive() {
        mockAdminUser();
        String view = staffBoardController.approvePayment(1L);
        verify(staffBoardService, times(1)).approvePayment(1L);
        assertEquals("redirect:../../payments", view);
    }

    @Test
    public void testGetAllTopUps_AdminUser_Positive() {
        mockAdminUser();
        when(staffBoardService.getAllTopUps()).thenReturn(Collections.emptyList());
        String view = staffBoardController.getAllTopUps(model);
        verify(model, times(1)).addAttribute(eq("topUps"), any());
        assertEquals("staffdashboard-topup", view);
    }

    @Test
    public void testGetAllPayments_AdminUser_Positive() {
        mockAdminUser();
        when(staffBoardService.getAllPayments()).thenReturn(Collections.emptyList());
        String view = staffBoardController.getAllPayments(model);
        verify(model, times(1)).addAttribute(eq("payments"), any());
        assertEquals("staffdashboard-payments", view);
    }

    // Negative test cases
    @Test
    public void testApproveTopUp_NonAdminUser_Negative() {
        mockNonAdminUser();
        String view = staffBoardController.approveTopUp("1");
        verify(staffBoardService, never()).approveTopUp(anyString());
        assertEquals("redirect:/listings", view);
    }

    @Test
    public void testApprovePayment_NonAdminUser_Negative() {
        mockNonAdminUser();
        String view = staffBoardController.approvePayment(1L);
        verify(staffBoardService, never()).approvePayment(anyLong());
        assertEquals("redirect:/listings", view);
    }

    @Test
    public void testGetAllTopUps_NonAdminUser_Negative() {
        mockNonAdminUser();
        String view = staffBoardController.getAllTopUps(model);
        verify(staffBoardService, never()).getAllTopUps();
        assertEquals("redirect:/listings", view);
    }

    @Test
    public void testGetAllPayments_NonAdminUser_Negative() {
        mockNonAdminUser();
        String view = staffBoardController.getAllPayments(model);
        verify(staffBoardService, never()).getAllPayments();
        assertEquals("redirect:/listings", view);
    }
}
