package zeus.zeushop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import zeus.zeushop.model.TopUp;
import zeus.zeushop.model.User;
import zeus.zeushop.repository.TopUpRepository;
import zeus.zeushop.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TopUpServiceImplTest {

    @Mock
    private TopUpRepository topUpRepository;

    @InjectMocks
    private TopUpServiceImpl topUpService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testCreateSmallAmountTopUp() {
        TopUp inputTopUp = new TopUp("user2", 4, "INITIAL");
        when(userRepository.findByUsername("user2")).thenReturn(new User());
        when(topUpRepository.save(any(TopUp.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TopUp result = topUpService.createTopUp(inputTopUp);

        assertEquals("APPROVED", result.getStatus());
        verify(topUpRepository).save(result);
    }


    @Test
    void testCreateBigAmountTopUp() {
        TopUp inputTopUp = new TopUp("user2", 100, "INITIAL");
        when(userRepository.findByUsername("user2")).thenReturn(new User());
        when(topUpRepository.save(any(TopUp.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TopUp result = topUpService.createTopUp(inputTopUp);

        assertEquals("PENDING", result.getStatus());
        verify(topUpRepository).save(result);
    }
    @Test
    void testCreateTopUpWithNonExistentUser() {
        TopUp inputTopUp = new TopUp("user3", 50, "INITIAL");
        when(userRepository.findByUsername("user3")).thenReturn(null);  // No user found
        when(topUpRepository.save(any(TopUp.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TopUp result = topUpService.createTopUp(inputTopUp);

        assertNotNull(result, "TopUp creation should succeed even if user is not found");
        verify(topUpRepository).save(result);
        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    void getUserTopUps() {
        List<TopUp> expectedTopUps = Arrays.asList(new TopUp("user1", 100, "PENDING"));
        when(topUpRepository.findByUserId("user1")).thenReturn(expectedTopUps);

        List<TopUp> result = topUpService.getUserTopUps("user1");
        assertEquals(1, result.size());
        verify(topUpRepository).findByUserId("user1");
    }

    @Test
    void deleteTopUpFound() {
        String topUpId = "123";
        TopUp topUp = new TopUp("user1", 100, "PENDING");
        when(topUpRepository.findById(topUpId)).thenReturn(Optional.of(topUp));
        doNothing().when(topUpRepository).deleteById(topUpId);

        boolean deleted = topUpService.deleteTopUp(topUpId);
        assertTrue(deleted);
        verify(topUpRepository).deleteById(topUpId);
    }

    @Test
    void deleteTopUpNotFound() {
        String topUpId = "123";
        when(topUpRepository.findById(topUpId)).thenReturn(Optional.empty());

        boolean deleted = topUpService.deleteTopUp(topUpId);
        assertFalse(deleted);
        verify(topUpRepository, never()).deleteById(topUpId);
    }

    @Test
    void getAllTopUps() {
        List<TopUp> expectedTopUps = Arrays.asList(new TopUp("user1", 100, "PENDING"));
        when(topUpRepository.findAll()).thenReturn(expectedTopUps);

        List<TopUp> result = topUpService.getAllTopUps();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(topUpRepository).findAll();
    }

    @Test
    void cancelTopUp_WhenPending_TopUpCancelled() {
        TopUp topUp = new TopUp();
        topUp.setStatus("PENDING");
        when(topUpRepository.findById("123")).thenReturn(Optional.of(topUp));
        boolean result = topUpService.cancelTopUp("123");
        assertTrue(result, "TopUp should be cancelled since it is PENDING");
        assertEquals("CANCELLED", topUp.getStatus(), "Status should be updated to CANCELLED");
        verify(topUpRepository).save(topUp);
    }
    @Test
    void cancelTopUp_WhenNotPending_ReturnsFalse() {
        TopUp topUp = new TopUp();
        topUp.setStatus("APPROVED");
        when(topUpRepository.findById("123")).thenReturn(Optional.of(topUp));
        boolean result = topUpService.cancelTopUp("123");
        assertFalse(result, "TopUp should not be cancelled as it is not PENDING");
        assertEquals("APPROVED", topUp.getStatus(), "Status should not change from APPROVED");
        verify(topUpRepository, never()).save(topUp);
    }
}
