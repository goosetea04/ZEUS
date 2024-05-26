package zeus.zeushop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.math.BigDecimal;
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

    @Test
    public void testSetId() {
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        assertEquals(1L, cartItem.getId());
    }
    @Test
    public void testSetBuyerId() {
        CartItem cartItem = new CartItem();
        cartItem.setBuyerId(123);
        assertEquals(123, cartItem.getBuyerId());
    }

    @Test
    public void testSetPrice() {
        CartItem cartItem = new CartItem();
        BigDecimal price = BigDecimal.valueOf(19.99);
        cartItem.setPrice(price);
        assertEquals(price, cartItem.getPrice());
    }

    @Test
    public void testSetPayment() {
        Payment payment = new Payment();
        CartItem cartItem = new CartItem();
        cartItem.setPayment(payment);
        assertEquals(payment, cartItem.getPayment());
    }

    @Test
    public void testSetStatus() {
        CartItem cartItem = new CartItem();
        cartItem.setStatus("PENDING");
        assertEquals("PENDING", cartItem.getStatus());
    }

    @Test
    public void testGetId() {
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        assertEquals(1L, cartItem.getId());
    }

    @Test
    public void testGetBuyerId() {
        CartItem cartItem = new CartItem();
        cartItem.setBuyerId(123);
        assertEquals(123, cartItem.getBuyerId());
    }

    @Test
    public void testGetPrice() {
        CartItem cartItem = new CartItem();
        BigDecimal price = BigDecimal.valueOf(19.99);
        cartItem.setPrice(price);
        assertEquals(price, cartItem.getPrice());
    }

    @Test
    public void testGetPayment() {
        Payment payment = new Payment();
        CartItem cartItem = new CartItem();
        cartItem.setPayment(payment);
        assertEquals(payment, cartItem.getPayment());
    }

    @Test
    public void testGetStatus() {
        CartItem cartItem = new CartItem();
        cartItem.setStatus("PENDING");
        assertEquals("PENDING", cartItem.getStatus());
    }
}
