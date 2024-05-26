package zeus.zeushop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import zeus.zeushop.model.Payment;
import zeus.zeushop.repository.PaymentRepository;
import java.math.BigDecimal;
import org.mockito.Mockito;

class PaymentServiceImplTest {

    private PaymentRepository paymentRepository;
    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setUp() {
        paymentRepository = Mockito.mock(PaymentRepository.class);
        paymentService = new PaymentServiceImpl(paymentRepository);
    }

    @Test
    void whenLastPaymentExists_returnStatus() {
        Payment mockPayment = new Payment();
        mockPayment.setStatus("APPROVED");
        when(paymentRepository.findTopByUserIdOrderByIdDesc(1)).thenReturn(mockPayment);
        String result = paymentService.getLatestPaymentStatus(1);
        assertEquals("APPROVED", result);
        verify(paymentRepository).findTopByUserIdOrderByIdDesc(1);
    }

    @Test
    void whenNoLastPaymentExists_returnNoPaymentMade() {
        when(paymentRepository.findTopByUserIdOrderByIdDesc(1)).thenReturn(null);
        String result = paymentService.getLatestPaymentStatus(1);
        assertEquals("No Payment Made", result);
        verify(paymentRepository).findTopByUserIdOrderByIdDesc(1);
    }

    @Test
    void createPayment_ReturnsPayment() {
        Integer userId = 1;
        BigDecimal amount = BigDecimal.valueOf(1000);
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Payment result = paymentService.createPayment(userId, amount);
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(amount, result.getAmount());
        assertEquals("PENDING", result.getStatus());
        assertNotNull(result.getCreatedAt());
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void hasPendingPayment_True() {
        when(paymentRepository.findTopByUserIdAndStatusOrderByIdDesc(1, "PENDING")).thenReturn(new Payment());
        boolean hasPending = paymentService.hasPendingPayment(1);
        assertTrue(hasPending);
        verify(paymentRepository).findTopByUserIdAndStatusOrderByIdDesc(1, "PENDING");
    }

    @Test
    void hasPendingPayment_False() {
        when(paymentRepository.findTopByUserIdAndStatusOrderByIdDesc(1, "PENDING")).thenReturn(null);
        boolean hasPending = paymentService.hasPendingPayment(1);
        assertFalse(hasPending);
        verify(paymentRepository).findTopByUserIdAndStatusOrderByIdDesc(1, "PENDING");
    }
}