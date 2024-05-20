package zeus.zeushop.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import zeus.zeushop.model.CartItem;
import zeus.zeushop.model.Listing;
import zeus.zeushop.model.User;
import zeus.zeushop.service.PaymentService;
import zeus.zeushop.service.ShoppingCartService;
import zeus.zeushop.service.UserService;
import zeus.zeushop.repository.CartItemRepository;

@ExtendWith(MockitoExtension.class)
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
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private CartController cartController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
    }

    @Test
    public void testShowCart_Positive() throws Exception {
        User user = new User();
        user.setId(1);
        user.setBalance(BigDecimal.valueOf(1000));

        Listing listing = new Listing();
        listing.setProduct_price(3.14f);

        CartItem cartItem = new CartItem();
        cartItem.setListing(listing);
        cartItem.setQuantity(2);

        when(authentication.getName()).thenReturn("testUser");
        when(userService.getUserByUsername("testUser")).thenReturn(user);
        when(shoppingCartService.getCartItemsByBuyerId(1)).thenReturn(Arrays.asList(cartItem));
        when(paymentService.getLatestPaymentStatus(1)).thenReturn("PAID");

        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cartItems"))
                .andExpect(model().attributeExists("totalCost"))
                .andExpect(model().attributeExists("balance"))
                .andExpect(model().attributeExists("paymentStatus"));
    }

    @Test
    public void testShowCart_Negative() throws Exception {
        when(authentication.getName()).thenReturn("testUser");
        when(userService.getUserByUsername("testUser")).thenReturn(null);

        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cartItems"))
                .andExpect(model().attributeExists("totalCost"))
                .andExpect(model().attributeExists("balance"))
                .andExpect(model().attributeExists("paymentStatus"));
    }

    @Test
    public void testRemoveFromCart_Positive() throws Exception {
        Long cartItemId = 1L;

        mockMvc.perform(post("/remove-from-cart")
                        .param("cartItemId", cartItemId.toString())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("Item removed from cart successfully."));

        verify(cartItemRepository, times(1)).deleteById(cartItemId.intValue());
    }

    @Test
    public void testRemoveFromCart_Negative() throws Exception {
        Long cartItemId = 1L;

        doThrow(new RuntimeException("Error removing item")).when(cartItemRepository).deleteById(cartItemId.intValue());

        mockMvc.perform(post("/remove-from-cart")
                        .param("cartItemId", cartItemId.toString())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error removing item from cart: Error removing item"));
    }
}
