package zeus.zeushop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class TopUpTest {

    private TopUp topUp;

    @BeforeEach
    void setUp() {
        topUp = new TopUp("user123", 100, "PENDING");
    }

    @Test
    void testConstructorAndFields() {

        assertEquals("user123", topUp.getUserId());
        assertEquals(100, topUp.getAmount());
        assertEquals("PENDING", topUp.getStatus());

        assertNotNull(topUp.getCreatedAt());
        assertNotNull(topUp.getUpdatedAt());

        // Using ChronoUnit.SECONDS to truncate to the nearest second for comparison, in case of minor discrepancies
        assertEquals(topUp.getCreatedAt().truncatedTo(ChronoUnit.SECONDS), topUp.getUpdatedAt().truncatedTo(ChronoUnit.SECONDS));
    }


    @Test
    void testSettersAndGetters() {
        topUp.setUserId("user456");
        assertEquals("user456", topUp.getUserId());

        topUp.setAmount(200);
        assertEquals(200, topUp.getAmount());

        topUp.setStatus("APPROVED");
        assertEquals("APPROVED", topUp.getStatus());

        LocalDateTime now = LocalDateTime.now();
        topUp.setUpdatedAt(now);
        assertEquals(now, topUp.getUpdatedAt());
    }

    @Test
    void testBehaviorOnUpdate() {
        LocalDateTime initialUpdatedAt = topUp.getUpdatedAt();
        topUp.setAmount(150);
        topUp.setUpdatedAt(LocalDateTime.now());

        assertNotEquals(initialUpdatedAt, topUp.getUpdatedAt());
    }
    @Test
    void testSetCreatedAt() {
        LocalDateTime now = LocalDateTime.now();
        topUp.setCreatedAt(now);
        assertEquals(now, topUp.getCreatedAt());
    }

    @Test
    void testGetFormattedCreatedAt() {
        LocalDateTime fixedDate = LocalDateTime.of(2023, 5, 15, 10, 30);
        topUp.setCreatedAt(fixedDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
        String expectedFormattedDate = fixedDate.format(formatter);
        assertEquals(expectedFormattedDate, topUp.getFormattedCreatedAt());
    }

}

