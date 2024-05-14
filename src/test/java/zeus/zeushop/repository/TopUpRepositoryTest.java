package zeus.zeushop.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import zeus.zeushop.model.TopUp;

@ExtendWith(MockitoExtension.class)
public class TopUpRepositoryTest {

    @Mock
    private TopUpRepository topUpRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreate() {
        TopUp topUp = new TopUp();
        topUp.setTopUpId("T001");
        topUp.setUserId("U123");
        topUp.setAmount(100);

        when(topUpRepository.save(any(TopUp.class))).thenReturn(topUp);

        TopUp savedTopUp = topUpRepository.save(topUp);

        assertNotNull(savedTopUp);
        assertEquals("T001", savedTopUp.getTopUpId());
        assertEquals("U123", savedTopUp.getUserId());
        assertEquals(100, savedTopUp.getAmount());
    }

    @Test
    void testFindAll() {
        TopUp topUp = new TopUp();
        when(topUpRepository.findAll()).thenReturn(Arrays.asList(topUp));

        List<TopUp> topUps = topUpRepository.findAll();

        assertFalse(topUps.isEmpty());
        assertEquals(topUp, topUps.get(0));
    }

    @Test
    void testFindByUserId() {
        TopUp topUp1 = new TopUp();
        topUp1.setUserId("U123");
        when(topUpRepository.findByUserId("U123")).thenReturn(Arrays.asList(topUp1));

        List<TopUp> result = topUpRepository.findByUserId("U123");

        assertEquals(1, result.size());
        assertEquals("U123", result.get(0).getUserId());
    }

    @Test
    void testDeleteTopUp_Positive() {
        doNothing().when(topUpRepository).deleteById("T001");
        topUpRepository.deleteById("T001");
        verify(topUpRepository, times(1)).deleteById("T001");
    }

    @Test
    void testDeleteTopUp_Negative() {
        doThrow(new RuntimeException()).when(topUpRepository).deleteById("T003");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            topUpRepository.deleteById("T003");
        });

        verify(topUpRepository, times(1)).deleteById("T003");
    }
}
