package zeus.zeushop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zeus.zeushop.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findTopByUserIdOrderByIdDesc(Integer userId);
    Payment findTopByUserIdAndStatusOrderByIdDesc(Integer userId, String status);
}
