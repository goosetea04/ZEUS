package zeus.zeushop.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Iterator;

import zeus.zeushop.model.TopUp;

public class TopUpRepositoryTest {

    private TopUpRepository topUpRepository;

    @BeforeEach
    void setUp() {
        topUpRepository = new TopUpRepository();
    }

    @Test
    void testCreate() {
        TopUp topUp = new TopUp();
        topUp.setTopUpId("T001");
        topUp.setUserId("U123");
        topUp.setAmount(100);

        TopUp savedTopUp = topUpRepository.create(topUp);

        assertNotNull(savedTopUp);
        assertEquals("T001", savedTopUp.getTopUpId());
        assertEquals("U123", savedTopUp.getUserId());
        assertEquals(100, savedTopUp.getAmount());
    }

    @Test
    void testFindAll() {
        TopUp topUp = new TopUp();
        topUpRepository.create(topUp);

        Iterator<TopUp> topUps = topUpRepository.findAll();

        assertTrue(topUps.hasNext());
        assertEquals(topUp, topUps.next());
    }

    @Test
    void testFindByUserId() {
        TopUp topUp1 = new TopUp();
        topUp1.setUserId("U123");
        TopUp topUp2 = new TopUp();
        topUp2.setUserId("U456");
        topUpRepository.create(topUp1);
        topUpRepository.create(topUp2);

        List<TopUp> result = topUpRepository.findByUserId("U123");

        assertEquals(1, result.size());
        assertEquals("U123", result.get(0).getUserId());
    }

    @Test
    void testDeleteTopUp_Positive() {
        TopUp topUp = new TopUp();
        topUp.setTopUpId("T001");
        topUpRepository.create(topUp);

        boolean isDeleted = topUpRepository.deleteTopUp("T001");

        assertTrue(isDeleted);
        assertFalse(topUpRepository.findAll().hasNext());
    }

    @Test
    void testDeleteTopUp_Negative() {
        TopUp topUp = new TopUp();
        topUp.setTopUpId("T002");
        topUpRepository.create(topUp);

        boolean isDeleted = topUpRepository.deleteTopUp("T003");

        assertFalse(isDeleted);
        assertTrue(topUpRepository.findAll().hasNext());
    }
}
