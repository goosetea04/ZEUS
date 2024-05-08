package zeus.zeushop.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import zeus.zeushop.model.CartItem;
import zeus.zeushop.service.ShoppingCartService;
import zeus.zeushop.repository.CartItemRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    @Mock
    private ShoppingCartService shoppingCartService;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartController cartController;

    @Test
    void testShowCart() {
        // Given
        List<CartItem> cartItems = new ArrayList<>();
        when(shoppingCartService.getAllCartItems()).thenReturn(cartItems);
        Model model = mock(Model.class);

        // When
        String viewName = cartController.showCart(model);

        // Then
        assertEquals("cart", viewName);
        verify(model).addAttribute("cartItems", cartItems);
    }

    @Test
    void testRemoveFromCart() {
        // Given
        Long cartItemId = 1L;

        // When
        String viewName = cartController.removeFromCart(cartItemId);

        // Then
        assertEquals("redirect:/cart", viewName);
        verify(cartItemRepository).deleteById(cartItemId.intValue());
    }
}
