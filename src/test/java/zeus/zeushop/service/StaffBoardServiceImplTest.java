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
    void setUp() {
        String uuid1 = UUID.randomUUID().toString();
        String uuid2 = UUID.randomUUID().toString();
        topUp1 = new TopUp(uuid1, 100, "PENDING");
        topUp2 = new TopUp(uuid2,  200, "APPROVED");

        // Assuming the createTopUp method updates or saves the top-up object.
        when(topUpService.createTopUp(topUp1)).thenReturn(topUp1);
        when(topUpService.createTopUp(topUp2)).thenReturn(topUp2);
    }

    @Test
    void approveTopUp_ExistingPendingId_ReturnsTrue() {
        // Arrange
        when(topUpService.getAllTopUps()).thenReturn(Arrays.asList(topUp1, topUp2));
        when(topUpService.deleteTopUp(topUp1.getTopUpId())).thenReturn(true);

        // Act
        boolean result = staffBoardService.approveTopUp(topUp1.getTopUpId());

        // Assert
        assertTrue(result);
        verify(topUpService).deleteTopUp(topUp1.getTopUpId());
        verify(topUpService).createTopUp(topUp1);
    }

    @Test
    void getTopUpsByStatus_ReturnsFilteredTopUps() {
        // Arrange
        when(topUpService.getAllTopUps()).thenReturn(Arrays.asList(topUp1, topUp2));

        // Act
        List<TopUp> result = staffBoardService.getTopUpsByStatus("APPROVED");

        // Assert
        assertEquals(1, result.size());
        assertEquals("APPROVED", result.get(0).getStatus());
    }

    @Test
    void getAllTopUps_ReturnsAllTopUps() {
        // Arrange
        when(topUpService.getAllTopUps()).thenReturn(Arrays.asList(topUp1, topUp2));

        // Act
        List<TopUp> result = staffBoardService.getAllTopUps();

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void getUserTopUps_ReturnsUserSpecificTopUps() {
        // Arrange
        when(topUpService.getUserTopUps("user1")).thenReturn(Arrays.asList(topUp1));

        // Act
        List<TopUp> result = staffBoardService.getUserTopUps("user1");

        // Assert
        assertEquals(1, result.size());
        assertEquals("user1", result.get(0).getUserId());
    }
}
