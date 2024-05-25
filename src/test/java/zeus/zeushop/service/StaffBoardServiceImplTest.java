package zeus.zeushop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zeus.zeushop.model.Payment;
import zeus.zeushop.model.TopUp;
import zeus.zeushop.model.User;
import zeus.zeushop.repository.PaymentRepository;
import zeus.zeushop.repository.TopUpRepository;
import zeus.zeushop.repository.UserRepository;
import zeus.zeushop.service.strategies.ApprovePaymentStrategy;
import zeus.zeushop.service.strategies.ApproveTopUpStrategy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class StaffBoardServiceImplTest {

    @Mock
    private TopUpService topUpService;

    @Mock
    private TopUpRepository topUpRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private UserRepository userRepository;

    @Mock
    private ApproveTopUpStrategy approveTopUpStrategy;  // Mock the strategy
    @Mock
    private ApprovePaymentStrategy approvePaymentStrategy;  // Mock the strategy

    @InjectMocks
    private StaffBoardServiceImpl staffBoardService;

    private TopUp topUp1, topUp2;
    private Payment payment1, payment2;
    private User user1;
    private UUID topupid1 = UUID.randomUUID();

    @BeforeEach
    void setUp(){
        topUp1 = new TopUp();
        topUp1.setTopUpId(topupid1.toString());
        topUp1.setUserId("U100");
        topUp1.setAmount(200);
        topUp1.setStatus("PENDING");

        topUp2 = new TopUp();
        topUp2.setTopUpId(UUID.randomUUID().toString());
        topUp2.setUserId("U101");
        topUp2.setAmount(300);
        topUp2.setStatus("APPROVED");

        user1 = new User();
        user1.setId(1);
        user1.setBalance(new BigDecimal("500.00"));

        payment1 = new Payment();
        payment1.setId(1L);
        payment1.setUserId(1);
        payment1.setAmount(new BigDecimal("100.00"));
        payment1.setStatus("PENDING");

        payment2 = new Payment();
        payment2.setId(2L);
        payment2.setUserId(1);
        payment2.setAmount(new BigDecimal("600.00"));
        payment2.setStatus("PENDING");
    }
//    @Test
//    void testPositiveApproveTopUp() {
//        //make test positive approve topup using approve strategy
//        when(topUpRepository.findById("3L")).thenReturn(Optional.of(topUp1));
//        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
//        when(approveTopUpStrategy.execute(topUp1)).thenReturn(true);
//    }
    @Test
    void testNegativeApproveTopUp(){
        when(topUpRepository.findById("3L")).thenReturn(Optional.empty());

        assertFalse(staffBoardService.approveTopUp("3L"));
        verify(topUpRepository, never()).save(any(TopUp.class));
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
        List<TopUp> topUps = List.of();
        when(topUpService.getAllTopUps()).thenReturn(topUps);

        List<TopUp> result = staffBoardService.getAllTopUps();

        assertEquals(0, result.size());
        verify(topUpService, times(1)).getAllTopUps();
    }
    @Test
    void testNegativeGetUserTopUps(){
        List<TopUp> topUps = List.of();
        when(topUpService.getUserTopUps("U100")).thenReturn(topUps);

        List<TopUp> result = staffBoardService.getUserTopUps("U100");

        assertEquals(0, result.size());
        verify(topUpService, times(1)).getUserTopUps("U100");
    }

    @Test
    void testApproveTopUpWhenTopUpIsNull() {
        // Arrange
        String topUpId = "nonexistent-id";
        when(topUpRepository.findById(topUpId)).thenReturn(Optional.empty());

        // Act
        boolean result = staffBoardService.approveTopUp(topUpId);

        // Assert
        assertFalse(result, "Expected approveTopUp to return false when TopUp is null");
        verify(topUpRepository).findById(topUpId);
        verifyNoInteractions(approveTopUpStrategy); // Ensure strategy is not called
    }

    @Test
    void testApproveTopUpWhenStrategyReturnsFalse() {
        // Arrange
        String topUpId = "existing-id";
        TopUp foundTopUp = new TopUp();
        foundTopUp.setTopUpId(topUpId);
        foundTopUp.setStatus("PENDING");
        when(topUpRepository.findById(topUpId)).thenReturn(Optional.of(foundTopUp));
        when(approveTopUpStrategy.execute(foundTopUp)).thenReturn(false);

        // Act
        boolean result = staffBoardService.approveTopUp(topUpId);

        // Assert
        assertFalse(result, "Expected approveTopUp to return false when strategy returns false");
        verify(topUpRepository).findById(topUpId);
        verify(approveTopUpStrategy).execute(foundTopUp); // Verify that the strategy was indeed called
    }
    @Test
    void testApprovePaymentWhenPaymentNotFound() {
        // Given
        Long paymentId = 1L;
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        // When
        boolean result = staffBoardService.approvePayment(paymentId);

        // Then
        assertFalse(result, "Expected approvePayment to return false when Payment is not found");
        verify(paymentRepository).findById(paymentId);
        verifyNoMoreInteractions(paymentRepository); // Ensures no other interactions with the repository
    }
    @Test
    void testApprovePaymentWhenStrategyReturnsFalse() {
        // Given
        Long paymentId = 1L;
        Payment payment = new Payment();
        payment.setId(paymentId);
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(approvePaymentStrategy.execute(payment)).thenReturn(false);

        // When
        boolean result = staffBoardService.approvePayment(paymentId);

        // Then
        assertFalse(result, "Expected approvePayment to return false when strategy execution fails");
        verify(paymentRepository).findById(paymentId);
        verify(approvePaymentStrategy).execute(payment);
    }
//    @Test
//    void testPositiveApprovePayment(){
//        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment1));
//        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
//
//        assertTrue(staffBoardService.approvePayment(1L));
//        assertEquals("APPROVED", payment1.getStatus());
//        assertEquals(0, new BigDecimal("400.00").compareTo(user1.getBalance()));
//        verify(paymentRepository).save(payment1);
//    }

//    @Test
//    void testNegativeApprovePayment_InsufficientFunds(){
//        when(paymentRepository.findById(2L)).thenReturn(Optional.of(payment2));
//        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
//
//        assertFalse(staffBoardService.approvePayment(2L));
//        assertEquals("REJECTED", payment2.getStatus());
//        assertEquals(0, new BigDecimal("500.00").compareTo(user1.getBalance())); // Balance should not change
//        verify(paymentRepository).save(payment2);
//    }

    @Test
    void testNegativeApprovePayment_NotFound(){
        when(paymentRepository.findById(3L)).thenReturn(Optional.empty());

        assertFalse(staffBoardService.approvePayment(3L));
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void testPositiveGetPaymentsByStatus(){
        List<Payment> payments = Arrays.asList(payment1, payment2);
        when(paymentRepository.findAll()).thenReturn(payments);

        List<Payment> result = staffBoardService.getPaymentsByStatus("PENDING");

        assertEquals(2, result.size());
        assertTrue(result.contains(payment1));
        assertTrue(result.contains(payment2));
    }

    @Test
    void testNegativeGetPaymentsByStatus_NoMatch(){
        List<Payment> payments = Collections.singletonList(payment1);
        when(paymentRepository.findAll()).thenReturn(payments);

        List<Payment> result = staffBoardService.getPaymentsByStatus("REJECTED");

        assertTrue(result.isEmpty());
    }

    @Test
    void testPositiveGetAllPayments(){
        List<Payment> payments = Arrays.asList(payment1, payment2);
        when(paymentRepository.findAll()).thenReturn(payments);

        List<Payment> result = staffBoardService.getAllPayments();

        assertEquals(2, result.size());
        assertTrue(result.contains(payment1));
        assertTrue(result.contains(payment2));
    }

    @Test
    void testNegativeGetAllPayments(){
        List<Payment> payments = Collections.singletonList(payment1);
        when(paymentRepository.findAll()).thenReturn(payments);

        List<Payment> result = staffBoardService.getAllPayments();

        assertEquals(1, result.size());
        assertTrue(result.contains(payment1));
    }

}