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
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import zeus.zeushop.model.Order;
import zeus.zeushop.model.User;
import zeus.zeushop.service.OrderService;
import zeus.zeushop.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class OrderControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    private Model model;
    private RedirectAttributes redirectAttributes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        model = new BindingAwareModelMap();
        redirectAttributes = new RedirectAttributesModelMap();
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("user");
    }

    @Test
    void testManageOrders() {
        User currentUser = new User();
        currentUser.setId(1);
        when(userService.getUserByUsername("user")).thenReturn(currentUser);

        List<Order> orders = new ArrayList<>();
        when(orderService.getOrdersByUserId(1L)).thenReturn(orders);

        String viewName = orderController.manageOrders(model);

        assertEquals("manage-orders", viewName);
        verify(userService).getUserByUsername("user");
        verify(orderService).getOrdersByUserId(1L);
        assertEquals(orders, model.getAttribute("orders"));
    }

    @Test
    void testUpdateOrderStatus() {
        Long orderId = 1L;
        String status = "COMPLETED";
        orderController.updateOrderStatus(orderId, status, redirectAttributes);

        verify(orderService).updateOrderStatus(orderId, status);
        assertEquals("Order status updated successfully.", redirectAttributes.getFlashAttributes().get("success"));
        assertEquals("redirect:/orders/manage", orderController.updateOrderStatus(orderId, status, redirectAttributes));
    }
}
