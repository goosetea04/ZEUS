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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        TopUp inputTopUp = new TopUp("user1", 5, "INITIAL");
        User user = new User();
        user.setUsername("user1");
        user.setBalance(BigDecimal.ZERO);

        when(userRepository.findByUsername("user1")).thenReturn(user);
        when(topUpRepository.save(any(TopUp.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TopUp result = topUpService.createTopUp(inputTopUp);

        assertEquals("APPROVED", result.getStatus());
        assertEquals(BigDecimal.valueOf(5), user.getBalance());
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
}
