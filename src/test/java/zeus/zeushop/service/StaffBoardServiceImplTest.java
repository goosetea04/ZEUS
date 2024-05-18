package zeus.zeushop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zeus.zeushop.model.TopUp;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class StaffBoardServiceImplTest {

    @Mock
    private TopUpService topUpService;

    @InjectMocks
    private StaffBoardServiceImpl staffBoardService;

    private TopUp topUp1, topUp2;

    @BeforeEach
    void setUp(){
        topUp1 = new TopUp();
        topUp1.setTopUpId(UUID.randomUUID().toString());
        topUp1.setUserId("U100");
        topUp1.setAmount(200);
        topUp1.setStatus("PENDING");

        topUp2 = new TopUp();
        topUp2.setTopUpId(UUID.randomUUID().toString());
        topUp2.setUserId("U101");
        topUp2.setAmount(300);
        topUp2.setStatus("APPROVED");
    }
    @Test
    void testPositiveApproveTopUp(){
        List<TopUp> topUps = Arrays.asList(topUp1, topUp2);
        when(topUpService.getAllTopUps()).thenReturn(topUps);
        when(topUpService.deleteTopUp(topUp1.getTopUpId())).thenReturn(true);
        when(topUpService.createTopUp(topUp1)).thenReturn(topUp1);

        assertTrue(staffBoardService.approveTopUp(topUp1.getTopUpId()));
        verify(topUpService, times(1)).getAllTopUps();
        verify(topUpService, times(1)).deleteTopUp(topUp1.getTopUpId());
        verify(topUpService, times(1)).createTopUp(topUp1);
    }
    @Test
    void testNegativeApproveTopUp(){
        List<TopUp> topUps = Arrays.asList(topUp2);
        when(topUpService.getAllTopUps()).thenReturn(topUps);

        assertFalse(staffBoardService.approveTopUp(topUp1.getTopUpId()));
        verify(topUpService, times(1)).getAllTopUps();
        verify(topUpService, never()).deleteTopUp(topUp1.getTopUpId());
        verify(topUpService, never()).createTopUp(topUp1);
    }
    @Test
    void testPositiveGetTopUpsByStatus(){
        List<TopUp> topUps = Arrays.asList(topUp1, topUp2);
        when(topUpService.getAllTopUps()).thenReturn(topUps);

        List<TopUp> result = staffBoardService.getTopUpsByStatus("PENDING");

        assertEquals(1, result.size());
        verify(topUpService, times(1)).getAllTopUps();
    }
    @Test
    void testNegativeGetTopUpsByStatus(){
        List<TopUp> topUps = Arrays.asList(topUp1, topUp2);
        when(topUpService.getAllTopUps()).thenReturn(topUps);

        List<TopUp> result = staffBoardService.getTopUpsByStatus("REJECTED");

        assertEquals(0, result.size());
        verify(topUpService, times(1)).getAllTopUps();
    }
    @Test
    void testPositiveGetAllTopUps(){
        List<TopUp> topUps = Arrays.asList(topUp1, topUp2);
        when(topUpService.getAllTopUps()).thenReturn(topUps);

        List<TopUp> result = staffBoardService.getAllTopUps();

        assertEquals(2, result.size());
        verify(topUpService, times(1)).getAllTopUps();
    }
    @Test
    void testPositiveGetUserTopUps(){
        List<TopUp> topUps = Arrays.asList(topUp1, topUp2);
        when(topUpService.getUserTopUps("U100")).thenReturn(topUps);

        List<TopUp> result = staffBoardService.getUserTopUps("U100");

        assertEquals(2, result.size());
        verify(topUpService, times(1)).getUserTopUps("U100");
    }
    @Test
    void testNegativeGetAllTopUps(){
        List<TopUp> topUps = Arrays.asList();
        when(topUpService.getAllTopUps()).thenReturn(topUps);

        List<TopUp> result = staffBoardService.getAllTopUps();

        assertEquals(0, result.size());
        verify(topUpService, times(1)).getAllTopUps();
    }
    @Test
    void testNegativeGetUserTopUps(){
        List<TopUp> topUps = Arrays.asList();
        when(topUpService.getUserTopUps("U100")).thenReturn(topUps);

        List<TopUp> result = staffBoardService.getUserTopUps("U100");

        assertEquals(0, result.size());
        verify(topUpService, times(1)).getUserTopUps("U100");
    }
}