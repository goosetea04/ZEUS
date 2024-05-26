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
import zeus.zeushop.model.Payment;
import zeus.zeushop.model.TopUp;
import zeus.zeushop.model.User;
import zeus.zeushop.service.StaffBoardService;
import zeus.zeushop.service.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("admin");
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

    private void setupNonAdminUser() {
        User user = new User();
        user.setRole("USER");
        when(authentication.getPrincipal()).thenReturn(user);
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

    @Test
    void staffHome_AdminAccess() {
        // Arrange
        User admin = new User();
        admin.setUsername("admin");
        admin.setRole("ADMIN");
        when(userService.getUserByUsername("admin")).thenReturn(admin);

        // Act
        String view = staffBoardController.staffHome();

        // Assert
        assertEquals("staffdashboard", view, "Admin should access the staff dashboard");
    }

    @Test
    void staffHome_NonAdminRedirection() {
        setupNonAdminUser();
        String view = staffBoardController.staffHome();
        assertEquals("redirect:/listings", view, "Non-admin should be redirected from staff dashboard");
    }

    @Test
    void getTopUpsByStatus_NonAdminRedirection() {
        setupNonAdminUser();
        String status = "ALL";
        String view = staffBoardController.getTopUpsByStatus(status, model);
        assertEquals("redirect:/listings", view, "Non-admin should be redirected when accessing top-ups by status");
    }

    @Test
    void getPaymentsByStatus_NonAdminRedirection() {
        setupNonAdminUser();
        String status = "ALL";
        String view = staffBoardController.getPaymentsByStatus(status, model);
        assertEquals("redirect:/listings", view, "Non-admin should be redirected when accessing payments by status");
    }

    @Test
    void testGetTopUpsByStatus_AdminAccess() {
        // Arrange
        User admin = new User();
        admin.setRole("ADMIN");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("admin");
        when(userService.getUserByUsername("admin")).thenReturn(admin);

        List<TopUp> topUps = new ArrayList<>(); // Example list
        when(staffBoardService.getTopUpsByStatus("ACTIVE")).thenReturn(topUps);

        // Act
        String viewName = staffBoardController.getTopUpsByStatus("ACTIVE", model);

        // Assert
        assertEquals("staffdashboard-topup", viewName);
        verify(model).addAttribute("topUps", topUps);
        verify(model).addAttribute("status", "ACTIVE");
    }

    @Test
    void testGetTopUpsByStatus_NonAdminAccess() {
        // Arrange
        User nonAdmin = new User();
        nonAdmin.setRole("USER");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user");
        when(userService.getUserByUsername("user")).thenReturn(nonAdmin);

        // Act
        String viewName = staffBoardController.getTopUpsByStatus("ACTIVE", model);

        // Assert
        assertEquals("redirect:/listings", viewName);
        verify(staffBoardService, never()).getTopUpsByStatus(anyString());
    }

    @Test
    void testGetPaymentsByStatus_AdminAccess() {
        // Arrange
        User admin = new User();
        admin.setRole("ADMIN");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("admin");
        when(userService.getUserByUsername("admin")).thenReturn(admin);

        List<Payment> payments = new ArrayList<>();
        when(staffBoardService.getPaymentsByStatus("COMPLETED")).thenReturn(payments);

        // Act
        String viewName = staffBoardController.getPaymentsByStatus("COMPLETED", model);

        // Assert
        assertEquals("staffdashboard-payments", viewName);
        verify(model).addAttribute("payments", payments);
        verify(model).addAttribute("status", "COMPLETED");
    }

    @Test
    void testGetPaymentsByStatus_NonAdminAccess() {
        // Arrange
        User nonAdmin = new User();
        nonAdmin.setRole("USER");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user");
        when(userService.getUserByUsername("user")).thenReturn(nonAdmin);

        // Act
        String viewName = staffBoardController.getPaymentsByStatus("COMPLETED", model);

        // Assert
        assertEquals("redirect:/listings", viewName);
        verify(staffBoardService, never()).getPaymentsByStatus(anyString());
    }

}
