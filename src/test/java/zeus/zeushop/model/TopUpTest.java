package zeus.zeushop.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TopUpTest {

    @Test
    public void testTopUpConstructorAndSetterGetter() {
        TopUp topUp = new TopUp();
        LocalDateTime now = LocalDateTime.now();

        topUp.setTopUpId("topUp123");
        topUp.setUserId("user123");
        topUp.setAmount(100.0);
        topUp.setStatus("PENDING");
        topUp.setCreatedAt(now);
        topUp.setUpdatedAt(now);

        assertNotNull(topUp);
        assertEquals("topUp123", topUp.getTopUpId());
        assertEquals("user123", topUp.getUserId());
        assertEquals(100.0, topUp.getAmount());
        assertEquals("PENDING", topUp.getStatus());
        assertEquals(now, topUp.getCreatedAt());
        assertEquals(now, topUp.getUpdatedAt());
    }
}
