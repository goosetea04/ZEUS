package zeus.zeushop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zeus.zeushop.repository.CartItemRepository;
import zeus.zeushop.repository.ListingRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ShoppingCartServiceFactoryTest {

    private CartItemRepository cartItemRepository;
    private ListingRepository listingRepository;

    @BeforeEach
    void setUp() {
        cartItemRepository = mock(CartItemRepository.class);
        listingRepository = mock(ListingRepository.class);
    }

    @Test
    void testCreateShoppingCartService() {
        ShoppingCartService shoppingCartService = ShoppingCartServiceFactory.createShoppingCartService(cartItemRepository, listingRepository);
        assertNotNull(shoppingCartService, "ShoppingCartService should not be null");
        assertTrue(shoppingCartService instanceof ShoppingCartServiceImpl, "ShoppingCartService should be an instance of ShoppingCartServiceImpl");
    }

    @Test
    void testCreateShoppingCartServiceWithNullCartItemRepository() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ShoppingCartServiceFactory.createShoppingCartService(null, listingRepository);
        });
        assertEquals("CartItemRepository cannot be null", exception.getMessage());
    }

    @Test
    void testCreateShoppingCartServiceWithNullListingRepository() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ShoppingCartServiceFactory.createShoppingCartService(cartItemRepository, null);
        });
        assertEquals("ListingRepository cannot be null", exception.getMessage());
    }

    @Test
    void testCreateShoppingCartServiceWithNullRepositories() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ShoppingCartServiceFactory.createShoppingCartService(null, null);
        });
        assertEquals("CartItemRepository and ListingRepository cannot be null", exception.getMessage());
    }

    @Test
    void testCreateShoppingCartServiceWithMockedDependencies() {
        // This is just an example to illustrate testing with mocked dependencies.
        // Additional specific assertions and verifications can be added here.
        ShoppingCartService shoppingCartService = ShoppingCartServiceFactory.createShoppingCartService(cartItemRepository, listingRepository);
        assertNotNull(shoppingCartService, "ShoppingCartService should not be null");
        assertTrue(shoppingCartService instanceof ShoppingCartServiceImpl, "ShoppingCartService should be an instance of ShoppingCartServiceImpl");
    }
}
