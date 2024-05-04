package zeus.zeushop.service;

import zeus.zeushop.model.TopUp;
import zeus.zeushop.repository.TopUpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return topUpRepository.save(topUp); // Save handles both create and update
    }

    @Override
    public List<TopUp> getUserTopUps(String userId) {
        return topUpRepository.findByUserId(userId); // Custom method defined in repository
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
        return topUpRepository.findAll(); // Built-in method from JpaRepository
    }
}
