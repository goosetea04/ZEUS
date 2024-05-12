package zeus.zeushop.service;

import zeus.zeushop.model.TopUp;
import zeus.zeushop.model.User;
import zeus.zeushop.repository.TopUpRepository;
import zeus.zeushop.repository.UserRepository;
import zeus.zeushop.service.TopUpFactory.TopUpFactoryInterface;
import zeus.zeushop.service.TopUpFactory.SmallAmountTopUpFactory;
import zeus.zeushop.service.TopUpFactory.BigAmountTopUpFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TopUpServiceImpl implements TopUpService {

    @Autowired
    private TopUpRepository topUpRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    public TopUpServiceImpl(TopUpRepository topUpRepository) {

        this.topUpRepository = topUpRepository;
    }

    @Override
    public TopUp createTopUp(TopUp topUp) {
        TopUpFactoryInterface factory;
        if (topUp.getAmount() < 10) {
            factory = new SmallAmountTopUpFactory();  // small amounts
        } else {
            factory = new BigAmountTopUpFactory();  // large amounts
        }
        // Create top up
        TopUp newTopUp = factory.createTopUp(topUp.getUserId(), topUp.getAmount());
        User user = userRepository.findByUsername(topUp.getUserId());
        if (user != null && "APPROVED".equals(newTopUp.getStatus())) {
            // Update balance only if top up is approved
            user.setBalance(user.getBalance().add(BigDecimal.valueOf(topUp.getAmount())));
            userRepository.save(user);
        }
        return topUpRepository.save(newTopUp);
    }

    @Override
    public List<TopUp> getUserTopUps(String userId) {
        return topUpRepository.findByUserId(userId);
    }

    @Override
    public boolean deleteTopUp(String topUpId) {
        Optional<TopUp> topUp = topUpRepository.findById(topUpId);
        if (topUp.isPresent()) {
            topUpRepository.deleteById(topUpId);
            return true;
        }
        return false;
    }

    @Override
    public List<TopUp> getAllTopUps() {
        return topUpRepository.findAll();
    }

    @Override
    public boolean cancelTopUp(String topUpId) {
        Optional<TopUp> topUp = topUpRepository.findById(topUpId);
        if (topUp.isPresent() && "PENDING".equals(topUp.get().getStatus())) {
            topUp.get().setStatus("CANCELLED");
            topUp.get().setUpdatedAt(LocalDateTime.now());
            topUpRepository.save(topUp.get());
            return true;
        }
        return false;
    }

}
