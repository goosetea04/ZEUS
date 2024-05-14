package zeus.zeushop.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zeus.zeushop.model.Payment;
import zeus.zeushop.repository.PaymentRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public String getLatestPaymentStatus(Integer userId) {
        Payment lastPayment = paymentRepository.findTopByUserIdOrderByIdDesc(userId);
        return lastPayment != null ? lastPayment.getStatus() : "No Payment Made";
    }

    @Transactional
    @Override
    public Payment createPayment(Integer userId, BigDecimal amount) {
        Payment payment = new Payment();
        payment.setUserId(userId);
        payment.setAmount(amount);
        payment.setStatus("PENDING");
        payment.setCreatedAt(LocalDateTime.now());
        return paymentRepository.save(payment);
    }
    public boolean hasPendingPayment(Integer userId) {
        Payment lastPayment = paymentRepository.findTopByUserIdAndStatusOrderByIdDesc(userId, "PENDING");
        return lastPayment != null;
    }


}
