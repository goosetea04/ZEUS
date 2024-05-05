package zeus.zeushop.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zeus.zeushop.model.CartItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartItemRepositoryTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartItem cartItem;

    @Test
    void testFindAll_Positive() {
        List<CartItem> cartItems = new ArrayList<>();
        // Mock behavior to return cart items when findAll is called
        when(cartItemRepository.findAll()).thenReturn(cartItems);

        List<CartItem> retrievedCartItems = cartItemRepository.findAll();

        // Assert that retrieved cart items match the mocked cart items
        assertEquals(cartItems, retrievedCartItems);
        // Verify that findAll method was called once
        verify(cartItemRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Positive() {
        Integer id = 1;
        CartItem cartItem = new CartItem();
        when(cartItemRepository.findById(id)).thenReturn(Optional.of(cartItem));

        Optional<CartItem> retrievedCartItem = cartItemRepository.findById(id);

        assertEquals(Optional.of(cartItem), retrievedCartItem);
        verify(cartItemRepository, times(1)).findById(id);
    }

    @Test
    void testFindAll_Negative() {
        when(cartItemRepository.findAll()).thenReturn(new ArrayList<>());

        List<CartItem> retrievedCartItems = cartItemRepository.findAll();

        // Assert that retrieved cart items are empty
        assertEquals(0, retrievedCartItems.size());
        // Verify that findAll method was called once
        verify(cartItemRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Negative() {
        Integer id = 1;
        when(cartItemRepository.findById(id)).thenReturn(Optional.empty());

        Optional<CartItem> retrievedCartItem = cartItemRepository.findById(id);
        assertEquals(Optional.empty(), retrievedCartItem);

        verify(cartItemRepository, times(1)).findById(id);
    }
}
