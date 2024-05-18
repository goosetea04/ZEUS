package zeus.zeushop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zeus.zeushop.model.Payment;
import zeus.zeushop.model.TopUp;
import zeus.zeushop.model.User;
import zeus.zeushop.repository.PaymentRepository;
import zeus.zeushop.repository.TopUpRepository;
import zeus.zeushop.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StaffBoardServiceImpl implements StaffBoardService {
    private final TopUpService topUpService;

    private final UserRepository userRepository;
    private final TopUpRepository topUpRepository;
    private final PaymentRepository paymentRepository;


    @Autowired
    public StaffBoardServiceImpl(TopUpService topUpService, UserRepository userRepository, TopUpRepository topUpRepository, PaymentRepository paymentRepository){
        this.topUpService = topUpService;
        this.userRepository = userRepository;
        this.topUpRepository = topUpRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public boolean approveTopUp(String topUpId) {
        Optional<TopUp> topUpOptional = topUpRepository.findById(topUpId);
        if (topUpOptional.isPresent()) {
            TopUp topUp = topUpOptional.get();
            if ("PENDING".equals(topUp.getStatus())) {
                topUp.setStatus("APPROVED");
                User user = userRepository.findByUsername(topUp.getUserId());
                if (user != null) {
                    user.setBalance(user.getBalance().add(BigDecimal.valueOf(topUp.getAmount())));
                    userRepository.save(user);
                }
                topUpRepository.save(topUp);
                return true;
            }
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
    // approve payment function for staff
    // if payment is pending, and the user has enough balance, approve the payment
    // if payment is pending, and the user does not have enough balance, reject the payment
    public boolean approvePayment(Long paymentId) {
        Optional<Payment> paymentOptional = paymentRepository.findById(paymentId);
        if (paymentOptional.isPresent()) {
            Payment payment = paymentOptional.get();
            if ("PENDING".equals(payment.getStatus())) {
                Optional<User> userOptional = userRepository.findById(payment.getUserId());
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    if (user.getBalance().compareTo(payment.getAmount()) >= 0) {
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
            }
        }
        return false;
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