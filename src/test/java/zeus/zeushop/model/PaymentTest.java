package zeus.zeushop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    private Payment payment;

    @BeforeEach
    void setUp() {
        payment = new Payment();
        payment.setId(1L);
        payment.setUserId(123);
        payment.setAmount(BigDecimal.valueOf(100));
        payment.setStatus("PENDING");
        payment.setCreatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    void testCreatedAtSetterAndGetter() {
        LocalDateTime specificTime = LocalDateTime.of(2024, 5, 27, 2, 2, 11);
        payment.setCreatedAt(specificTime);
        assertEquals(specificTime, payment.getCreatedAt());
    }

    @Test
    void testChangeCreatedAt() {
        LocalDateTime initialTime = payment.getCreatedAt();
        LocalDateTime updatedTime = initialTime.plusHours(1).truncatedTo(ChronoUnit.SECONDS);
        payment.setCreatedAt(updatedTime);
        assertEquals(updatedTime, payment.getCreatedAt());
    }
}
