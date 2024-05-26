package zeus.zeushop.service.strategies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zeus.zeushop.model.Payment;
import zeus.zeushop.model.User;
import zeus.zeushop.repository.PaymentRepository;
import zeus.zeushop.repository.UserRepository;

import java.math.BigDecimal;

@Component
public class ApprovePaymentStrategy implements AdministrativeAction<Payment> {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean execute(Payment payment) {
        if ("PENDING".equals(payment.getStatus())) {
            User user = userRepository.findById(payment.getUserId()).orElse(null);
            if (user != null && user.getBalance().compareTo(payment.getAmount()) >= 0) {
                user.setBalance(user.getBalance().subtract(payment.getAmount()));
                userRepository.save(user);
                payment.setStatus("APPROVED");
                paymentRepository.save(payment);
                return true;
            } else {
                payment.setStatus("REJECTED");
                paymentRepository.save(payment);
                return false;
            }
        }
        return false;
    }
}
