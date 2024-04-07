package zeus.zeushop.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionModelTests {

    @Test
    public void testTransactionGettersAndSetters() {
        Transaction transaction = new Transaction();
        LocalDateTime now = LocalDateTime.now();

        // Setting values
        transaction.setTransactionId(1L);
        transaction.setUserId(2L);
        transaction.setType(TransactionType.TOP_UP);
        transaction.setAmount(100.0);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setCreatedAt(now);
        transaction.setUpdatedAt(now);

        // Assertions to verify if getters return what was set
        assertEquals(1L, transaction.getTransactionId());
        assertEquals(2L, transaction.getUserId());
        assertEquals(TransactionType.TOP_UP, transaction.getType());
        assertEquals(100.0, transaction.getAmount());
        assertEquals(TransactionStatus.PENDING, transaction.getStatus());
        assertEquals(now, transaction.getCreatedAt());
        assertEquals(now, transaction.getUpdatedAt());
    }

    // Additional tests can be added here as needed
}
