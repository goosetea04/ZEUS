package zeus.zeushop.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import zeus.zeushop.model.TopUp;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

class StaffBoardRepositoryTest {

    private TopUpRepository topUpRepository;
    private StaffBoardRepository staffBoardRepository;

    @BeforeEach
    void setup() {
        // Mock the TopUpRepository
        topUpRepository = mock(TopUpRepository.class);
        staffBoardRepository = new StaffBoardRepository(topUpRepository);
    }

    @Test
    void testFindAllTopUps() {
        // Setup
        TopUp topUp1 = new TopUp("1", "user1", 100.0, "PENDING", LocalDateTime.now(), LocalDateTime.now());
        TopUp topUp2 = new TopUp("2", "user2", 200.0, "APPROVED", LocalDateTime.now(), LocalDateTime.now());
        when(topUpRepository.findAll()).thenReturn(Arrays.asList(topUp1, topUp2).iterator());

        // Execute
        List<TopUp> result = staffBoardRepository.findAllTopUps();

        // Verify
        assertEquals(2, result.size());
        verify(topUpRepository).findAll();
    }

    @Test
    void testFindAllTopUpsByStatus() {
        // Setup
        TopUp topUp1 = new TopUp("1", "user1", 100.0, "PENDING", LocalDateTime.now(), LocalDateTime.now());
        TopUp topUp2 = new TopUp("2", "user2", 200.0, "APPROVED", LocalDateTime.now(), LocalDateTime.now());
        when(topUpRepository.findAll()).thenReturn(Arrays.asList(topUp1, topUp2).iterator());

        // Execute
        List<TopUp> result = staffBoardRepository.findAllTopUpsByStatus("APPROVED");

        // Verify
        assertEquals(1, result.size());
        assertEquals("2", result.get(0).getTopUpId());
        verify(topUpRepository).findAll();
    }

    @Test
    void testApproveTopUp() {
        // Setup
        TopUp topUp1 = new TopUp("1", "user1", 100.0, "PENDING", LocalDateTime.now(), LocalDateTime.now());
        TopUp topUp2 = new TopUp("2", "user2", 200.0, "APPROVED", LocalDateTime.now(), LocalDateTime.now());
        when(topUpRepository.findAll()).thenReturn(Arrays.asList(topUp1, topUp2).iterator());

        // Execute
        boolean result = staffBoardRepository.approveTopUp("1");

        // Verify
        assertTrue(result);
        assertEquals("APPROVED", topUp1.getStatus());
        verify(topUpRepository).findAll();
    }
}
