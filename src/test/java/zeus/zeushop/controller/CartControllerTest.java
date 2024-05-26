package zeus.zeushop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import zeus.zeushop.model.*;
import zeus.zeushop.service.PaymentService;
import zeus.zeushop.service.ShoppingCartService;
import zeus.zeushop.service.UserService;
import zeus.zeushop.service.OrderService;
import zeus.zeushop.repository.CartItemRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class CartControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private ShoppingCartService shoppingCartService;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private PaymentService paymentService;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private CartController cartController;
    @Mock
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    public void testShowCart() {
        User user = new User();
        user.setId(1);
        user.setBalance(BigDecimal.valueOf(1000));

        List<CartItem> cartItems = new ArrayList<>();

        // Initialize the Listing objects correctly
        Listing listing1 = new Listing();
        listing1.setProduct_price(50.0f); // Use Float type as required
        CartItem item1 = new CartItem();
        item1.setQuantity(2);
        item1.setListing(listing1); // Associate Listing with CartItem

        Listing listing2 = new Listing();
        listing2.setProduct_price(100.0f); // Use Float type as required
        CartItem item2 = new CartItem();
        item2.setQuantity(1);
        item2.setListing(listing2); // Associate Listing with CartItem

        cartItems.add(item1);
        cartItems.add(item2);

        // Mocking authentication and services
        when(authentication.getName()).thenReturn("user1");
        when(userService.getUserByUsername("user1")).thenReturn(user);
        when(shoppingCartService.getCartItemsByBuyerId(user.getId())).thenReturn(cartItems);
        when(paymentService.getLatestPaymentStatus(user.getId())).thenReturn("PAID");

        String viewName = cartController.showCart(model, session);

        assertEquals("cart", viewName);
        verify(model, times(1)).addAttribute("cartItems", cartItems);
        verify(model, times(1)).addAttribute("totalCost", BigDecimal.valueOf(200.0));
        verify(model, times(1)).addAttribute("balance", BigDecimal.valueOf(1000));
        verify(model, times(1)).addAttribute("paymentStatus", "PAID");
    }


    @Test
    public void testRemoveFromCart_Success() {
        ResponseEntity<String> response = cartController.removeFromCart(1L);

        verify(cartItemRepository, times(1)).deleteById(anyInt());
        assertEquals(ResponseEntity.ok("Item removed from cart successfully."), response);
    }

    @Test
    public void testRemoveFromCart_Failure() {
        doThrow(new RuntimeException("Error")).when(cartItemRepository).deleteById(anyInt());

        ResponseEntity<String> response = cartController.removeFromCart(1L);

        verify(cartItemRepository, times(1)).deleteById(anyInt());
        assertEquals(ResponseEntity.status(500).body("Error removing item from cart: Error"), response);
    }

    @Test
    public void testAllItemsApproved_SomePending() throws Exception {
        List<CartItem> cartItems = new ArrayList<>();

        CartItem item1 = new CartItem();
        item1.setStatus("APPROVED");
        cartItems.add(item1);

        CartItem item2 = new CartItem();
        item2.setStatus("PENDING");
        cartItems.add(item2);

        boolean result = invokeAllItemsApproved(cartController, cartItems);
        assertEquals(false, result);
    }

    private boolean invokeAllItemsApproved(CartController cartController, List<CartItem> cartItems) throws Exception {
        java.lang.reflect.Method method = CartController.class.getDeclaredMethod("allItemsApproved", List.class);
        method.setAccessible(true);
        return (boolean) method.invoke(cartController, cartItems);
    }

    @Test
    public void testCreateOrder() {
        User user = new User();
        user.setId(1);
        user.setBalance(new BigDecimal("1000"));

        CartItem cartItem = new CartItem();
        Listing listing = new Listing();
        listing.setProduct_price(100.0f);
        cartItem.setListing(listing);
        cartItem.setQuantity(2);
        cartItem.setPrice(new BigDecimal("200.00"));

        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);

        when(authentication.getName()).thenReturn("user");
        when(userService.getUserByUsername("user")).thenReturn(user);
        when(shoppingCartService.getCartItemsByBuyerId(user.getId())).thenReturn(cartItems);

        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        String expectedView = cartController.createOrder(redirectAttributes);
        assertEquals("redirect:/orders", expectedView);
        verify(orderService).createOrder(any(Order.class));
        verify(shoppingCartService).clearCartItemsByBuyerId(user.getId());
        assertEquals("Order created successfully.", redirectAttributes.getFlashAttributes().get("success"));
    }

    @Test
    public void testCreateOrderEmptyCart() {
        User user = new User();
        user.setId(1);

        List<CartItem> cartItems = new ArrayList<>();

        when(authentication.getName()).thenReturn("user");
        when(userService.getUserByUsername("user")).thenReturn(user);
        when(shoppingCartService.getCartItemsByBuyerId(user.getId())).thenReturn(cartItems);

        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        String expectedView = cartController.createOrder(redirectAttributes);
        assertEquals("redirect:/cart", expectedView);
        verify(orderService, never()).createOrder(any(Order.class));
        assertEquals("Your cart is empty.", redirectAttributes.getFlashAttributes().get("error"));
    }

    @Test
    public void testShowOrders() {
        User user = new User();
        user.setId(1);

        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        orders.add(order);

        when(authentication.getName()).thenReturn("user");
        when(userService.getUserByUsername("user")).thenReturn(user);
        when(orderService.getOrdersByUserId(Long.valueOf(user.getId()))).thenReturn(orders);

        Model model = mock(Model.class);
        String expectedView = cartController.showOrders(model);
        assertEquals("orders", expectedView);
        verify(model).addAttribute("orders", orders);
    }
}
