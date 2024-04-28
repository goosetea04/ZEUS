package zeus.zeushop.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

public class TopUpTest {

    @Test
    public void testTopUpConstructor() {
        TopUp topUp = new TopUp();
        assertNotNull(topUp);
    }

    @Test
    public void testTopUpSetterAndGetter() {
        TopUp topUp = new TopUp();
        topUp.setTopUpId("T001");
        topUp.setUserId("U123");
        topUp.setAmount(100);
        topUp.setStatus("PENDING");
        LocalDateTime now = LocalDateTime.now();
        topUp.setCreatedAt(now);
        topUp.setUpdatedAt(now);

        assertEquals("T001", topUp.getTopUpId());
        assertEquals("U123", topUp.getUserId());
        assertEquals(100, topUp.getAmount());
        assertEquals("PENDING", topUp.getStatus());
        assertEquals(now, topUp.getCreatedAt());
        assertEquals(now, topUp.getUpdatedAt());
    }

    @Test
    public void testNullFields() {
        TopUp topUp = new TopUp();
        assertNull(topUp.getTopUpId());
        assertNull(topUp.getUserId());
        assertNotNull(topUp.getAmount()); // Primitive int default to 0
        assertNull(topUp.getStatus());
        assertNull(topUp.getCreatedAt());
        assertNull(topUp.getUpdatedAt());
    }

    @Test
    public void testNegativeAmount() {
        TopUp topUp = new TopUp();
        topUp.setAmount(-100);
        assertEquals(-100, topUp.getAmount());
    }

    @Test
    public void testStatusValues() {
        TopUp topUp = new TopUp();
        topUp.setStatus("APPROVED");
        assertEquals("APPROVED", topUp.getStatus());
        topUp.setStatus("CANCELLED");
        assertEquals("CANCELLED", topUp.getStatus());
        topUp.setStatus("PENDING");
        assertEquals("PENDING", topUp.getStatus());
    }
}
