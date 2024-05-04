package zeus.zeushop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zeus.zeushop.model.TopUp;
import zeus.zeushop.repository.TopUpRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TopUpServiceImplTest {

    @Mock
    private TopUpRepository topUpRepository;

    @InjectMocks
    private TopUpServiceImpl topUpService;

    @Test
    void testCreateTopUp() {
        TopUp topUp = new TopUp();
        topUp.setUserId("U100");
        topUp.setAmount(200);

        TopUp savedTopUp = new TopUp();
        savedTopUp.setTopUpId(UUID.randomUUID().toString());
        savedTopUp.setUserId(topUp.getUserId());
        savedTopUp.setAmount(topUp.getAmount());
        savedTopUp.setStatus("PENDING");
        savedTopUp.setCreatedAt(LocalDateTime.now());
        savedTopUp.setUpdatedAt(LocalDateTime.now());

        when(topUpRepository.create(any(TopUp.class))).thenReturn(savedTopUp);

        TopUp result = topUpService.createTopUp(topUp);

        assertNotNull(result.getTopUpId());
        assertEquals("PENDING", result.getStatus());
        assertEquals(topUp.getUserId(), result.getUserId());
        assertEquals(topUp.getAmount(), result.getAmount());
        verify(topUpRepository, times(1)).create(any(TopUp.class));
    }

    @Test
    void testGetUserTopUps() {
        List<TopUp> expectedTopUps = Arrays.asList(new TopUp(), new TopUp());
        when(topUpRepository.findByUserId("U100")).thenReturn(expectedTopUps);

        List<TopUp> actualTopUps = topUpService.getUserTopUps("U100");

        assertEquals(expectedTopUps.size(), actualTopUps.size());
        verify(topUpRepository, times(1)).findByUserId("U100");
    }

    @Test
    void testDeleteTopUp_Positive() {
        String topUpId = "T100";
        when(topUpRepository.deleteTopUp(topUpId)).thenReturn(true);

        boolean result = topUpService.deleteTopUp(topUpId);

        assertTrue(result);
        verify(topUpRepository, times(1)).deleteTopUp(topUpId);
    }

    @Test
    void testDeleteTopUp_Negative() {
        String topUpId = "T101";
        when(topUpRepository.deleteTopUp(topUpId)).thenReturn(false);

        boolean result = topUpService.deleteTopUp(topUpId);

        assertFalse(result);
        verify(topUpRepository, times(1)).deleteTopUp(topUpId);
    }

    @Test
    void testGetAllTopUps() {
        List<TopUp> expectedTopUps = new ArrayList<>();
        expectedTopUps.add(new TopUp());
        expectedTopUps.add(new TopUp());

        when(topUpRepository.findAll()).thenReturn(expectedTopUps.iterator());

        List<TopUp> actualTopUps = topUpService.getAllTopUps();

        assertEquals(expectedTopUps.size(), actualTopUps.size());
        verify(topUpRepository, times(1)).findAll();
    }
}
