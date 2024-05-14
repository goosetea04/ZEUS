package zeus.zeushop.service;

import zeus.zeushop.model.Payment;

import java.math.BigDecimal;

public interface PaymentService {
    Payment createPayment(Integer userId, BigDecimal amount);
    String getLatestPaymentStatus(Integer userId);
}


