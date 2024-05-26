package zeus.zeushop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    private OrderItem orderItem;

    @BeforeEach
    void setUp() {
        orderItem = new OrderItem();
    }

    @Test
    void testId() {
        Long expectedId = 1L;
        orderItem.setId(expectedId);
        assertEquals(expectedId, orderItem.getId());
    }

    @Test
    void testOrderRelationship() {
        Order order = new Order();
        order.setId(1L);
        orderItem.setOrder(order);
        assertNotNull(orderItem.getOrder());
        assertEquals(1L, orderItem.getOrder().getId());
    }

//    @Test
//    void testListingRelationship() {
//        Listing listing = new Listing();
//        listing.setId(2);
//        orderItem.setListing(listing);
//        assertNotNull(orderItem.getListing());
//        assertEquals(2L, Optional.ofNullable(orderItem.getListing().getId()));
//    }

    @Test
    void testQuantity() {
        int quantity = 5;
        orderItem.setQuantity(quantity);
        assertEquals(quantity, orderItem.getQuantity());
    }

    @Test
    void testPrice() {
        BigDecimal price = new BigDecimal("19.99");
        orderItem.setPrice(price);
        assertEquals(0, price.compareTo(orderItem.getPrice()));
    }
}
