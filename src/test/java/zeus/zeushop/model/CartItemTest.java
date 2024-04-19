package zeus.zeushop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CartItemTest {

    @Test
    public void testCartItemConstructor() {
        Listing listing = new Listing();
        CartItem cartItem = new CartItem(listing, 5);

        assertNotNull(cartItem);
        assertEquals(listing, cartItem.getListing());
        assertEquals(5, cartItem.getQuantity());
    }

    @Test
    public void testCartItemSetterAndGetter() {
        Listing listing = new Listing();
        CartItem cartItem = new CartItem();

        cartItem.setListing(listing);
        cartItem.setQuantity(10);

        assertEquals(listing, cartItem.getListing());
        assertEquals(10, cartItem.getQuantity());
    }
}
