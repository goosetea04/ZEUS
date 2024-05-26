package zeus.zeushop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
    }

    @Test
    void testId() {
        Long expectedId = 1L;
        order.setId(expectedId);
        assertEquals(expectedId, order.getId());
    }

    @Test
    void testTotalCost() {
        BigDecimal cost = new BigDecimal("199.99");
        order.setTotalCost(cost);
        assertEquals(0, cost.compareTo(order.getTotalCost()));
    }

    @Test
    void testStatus() {
        String status = "APPROVED";
        order.setStatus(status);
        assertEquals(status, order.getStatus());
    }

    @Test
    void testOrderItemsRelationship() {
        List<OrderItem> items = new ArrayList<>();
        OrderItem item1 = new OrderItem();
        item1.setId(1L);
        items.add(item1);

        OrderItem item2 = new OrderItem();
        item2.setId(2L);
        items.add(item2);

        order.setItems(items);

        assertNotNull(order.getItems());
        assertEquals(2, order.getItems().size());
        assertEquals(1L, order.getItems().get(0).getId());
        assertEquals(2L, order.getItems().get(1).getId());
    }

    @Test
    void testCreatedAt() {
        LocalDateTime now = LocalDateTime.now();
        order.setCreatedAt(now);
        assertEquals(now, order.getCreatedAt());
    }
}
