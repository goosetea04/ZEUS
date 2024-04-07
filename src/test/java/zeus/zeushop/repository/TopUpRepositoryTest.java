package zeus.zeushop.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zeus.zeushop.model.TopUp;

import java.time.LocalDateTime;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TopUpRepositoryTest {

    private TopUpRepository topUpRepository;

    @BeforeEach
    public void setUp() {
        topUpRepository = new TopUpRepository();
    }

    @Test
    public void testCreateTopUp() {
        TopUp topUp = new TopUp();
        topUp.setTopUpId("topUp1");
        topUp.setUserId("user1");
        topUp.setAmount(100.0);
        topUp.setStatus("PENDING");
        topUp.setCreatedAt(LocalDateTime.now());
        topUp.setUpdatedAt(LocalDateTime.now());

        TopUp createdTopUp = topUpRepository.create(topUp);

        assertEquals(topUp, createdTopUp);
    }

    @Test
    public void testFindAllTopUps() {
        TopUp topUp1 = new TopUp();
        topUp1.setTopUpId("topUp1");
        topUp1.setUserId("user1");
        topUp1.setAmount(100.0);
        topUp1.setStatus("PENDING");
        topUp1.setCreatedAt(LocalDateTime.now());
        topUp1.setUpdatedAt(LocalDateTime.now());

        TopUp topUp2 = new TopUp();
        topUp2.setTopUpId("topUp2");
        topUp2.setUserId("user2");
        topUp2.setAmount(200.0);
        topUp2.setStatus("PENDING");
        topUp2.setCreatedAt(LocalDateTime.now());
        topUp2.setUpdatedAt(LocalDateTime.now());

        topUpRepository.create(topUp1);
        topUpRepository.create(topUp2);

        Iterator<TopUp> iterator = topUpRepository.findAll();

        assertTrue(iterator.hasNext());
        assertEquals(topUp1, iterator.next());
        assertEquals(topUp2, iterator.next());
        assertTrue(!iterator.hasNext());
    }
}
