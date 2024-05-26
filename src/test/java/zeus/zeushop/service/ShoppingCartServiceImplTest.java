package zeus.zeushop.service;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zeus.zeushop.model.Listing;
import zeus.zeushop.model.CartItem;
import zeus.zeushop.repository.CartItemRepository;
import zeus.zeushop.repository.ListingRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceImplTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ListingRepository listingRepository;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @BeforeEach
    void setUp() {
        // You can add any setup logic here if needed
    }

    @Test
    void testAddListingToCart_Positive() {
        Listing listing = new Listing();
        int quantity = 1;

        shoppingCartService.addListingToCart(listing, quantity);

        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void testAddListingToCart_WithBuyerId_Positive() {
        Listing listing = new Listing();
        int quantity = 1;
        int buyerId = 123;

        shoppingCartService.addListingToCart(listing, quantity, buyerId);

        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void testAddListingToCart_WithBuyerId_Negative() {
        Listing listing = null;
        int quantity = 1;
        int buyerId = 123;

        assertThrows(IllegalArgumentException.class, () -> {
            shoppingCartService.addListingToCart(listing, quantity, buyerId);
        });

        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

    @Test
    void testGetAllListings_Positive() {
        List<Listing> expectedListings = new ArrayList<>();
        // Mock behavior to return listings when findAll is called
        when(listingRepository.findAll()).thenReturn(expectedListings);

        List<Listing> actualListings = shoppingCartService.getAllListings();

        assertEquals(expectedListings, actualListings);
        verify(listingRepository, times(1)).findAll();
    }

    @Test
    void testGetAllCartItems_Positive() {
        List<CartItem> expectedCartItems = new ArrayList<>();
        // Mock behavior to return cart items when findAll is called
        when(cartItemRepository.findAll()).thenReturn(expectedCartItems);

        List<CartItem> actualCartItems = shoppingCartService.getAllCartItems();

        assertEquals(expectedCartItems, actualCartItems);
        verify(cartItemRepository, times(1)).findAll();
    }

    @Test
    void testAddListingToCart_Negative() {
        // Case when listing is null
        Listing listing = null;
        int quantity = 1;

        assertThrows(IllegalArgumentException.class, () -> {
            shoppingCartService.addListingToCart(listing, quantity);
        });

        // Verify that save method is never called when listing is null
        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

    @Test
    void testGetCartItemsByBuyerId_Positive() {
        Integer buyerId = 123;
        List<CartItem> expectedCartItems = new ArrayList<>();
        when(cartItemRepository.findByBuyerId(buyerId)).thenReturn(expectedCartItems);

        List<CartItem> actualCartItems = shoppingCartService.getCartItemsByBuyerId(buyerId);

        assertEquals(expectedCartItems, actualCartItems);
        verify(cartItemRepository, times(1)).findByBuyerId(buyerId);
    }

    @Test
    void testMarkItemsPending_Positive() {
        List<CartItem> cartItems = new ArrayList<>();
        CartItem cartItem1 = new CartItem();
        CartItem cartItem2 = new CartItem();
        cartItems.add(cartItem1);
        cartItems.add(cartItem2);

        shoppingCartService.markItemsPending(cartItems);

        assertEquals("PENDING", cartItem1.getStatus());
        assertEquals("PENDING", cartItem2.getStatus());
        verify(cartItemRepository, times(1)).save(cartItem1);
        verify(cartItemRepository, times(1)).save(cartItem2);
    }
}
