package zeus.zeushop.service;

import zeus.zeushop.model.TopUp;
import zeus.zeushop.repository.TopUpRepository;
import zeus.zeushop.service.TopUpFactory;
import zeus.zeushop.service.TopUpFactory.TopUpFactoryInterface;
import zeus.zeushop.service.TopUpFactory.SmallAmountTopUpFactory;
import zeus.zeushop.service.TopUpFactory.BigAmountTopUpFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TopUpServiceImpl implements TopUpService {

    private final TopUpRepository topUpRepository;

    @Autowired
    public TopUpServiceImpl(TopUpRepository topUpRepository) {
        this.topUpRepository = topUpRepository;
    }

    @Override
    public TopUp createTopUp(TopUp topUp) {
        TopUpFactoryInterface factory;
        if (topUp.getAmount() < 10000) {
            factory = new SmallAmountTopUpFactory();
        } else {
            factory = new BigAmountTopUpFactory();
        }
        TopUp newTopUp = factory.createTopUp(topUp.getUserId(), topUp.getAmount());
        return topUpRepository.save(newTopUp); // Save handles both create and update
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
