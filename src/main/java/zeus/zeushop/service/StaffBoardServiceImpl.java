package zeus.zeushop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zeus.zeushop.model.Payment;
import zeus.zeushop.model.TopUp;
import zeus.zeushop.repository.PaymentRepository;
import zeus.zeushop.repository.TopUpRepository;
import zeus.zeushop.service.strategies.ApprovePaymentStrategy;
import zeus.zeushop.service.strategies.ApproveTopUpStrategy;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffBoardServiceImpl implements StaffBoardService {
    @Autowired
    private ApprovePaymentStrategy approvePaymentStrategy;
    @Autowired
    private ApproveTopUpStrategy approveTopUpStrategy;

    @Autowired
    private TopUpService topUpService;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private TopUpRepository topUpRepository;

    @Override
    public boolean approveTopUp(String topUpId) {
        TopUp topUp = topUpRepository.findById(topUpId).orElse(null);
        if (topUp != null) {
            return approveTopUpStrategy.execute(topUp);
        }
        return false;
    }

    @Override
    public boolean approvePayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElse(null);
        if (payment != null) {
            return approvePaymentStrategy.execute(payment);
        }
        return false;
    }

    @Override
    public List<TopUp> getTopUpsByStatus(String status) {
        return topUpService.getAllTopUps().stream()
                .filter(topUp -> topUp.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public List<Payment> getPaymentsByStatus(String status) {
        return paymentRepository.findAll().stream()
                .filter(payment -> payment.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    @Override
    public List<TopUp> getAllTopUps() {
        return topUpService.getAllTopUps();
    }

    @Override
    public List<TopUp> getUserTopUps(String userId) {
        return topUpService.getUserTopUps(userId);
    }
}
