package zeus.zeushop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zeus.zeushop.model.TopUp;
import zeus.zeushop.repository.TopUpRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Service
public class TopUpServiceImpl implements TopUpService {

    private final TopUpRepository topUpRepository;

    @Autowired
    public TopUpServiceImpl(TopUpRepository topUpRepository) {
        this.topUpRepository = topUpRepository;
    }

    @Override
    public TopUp createTopUp(TopUp topUp) {
        topUp.setTopUpId(UUID.randomUUID().toString());
        topUp.setStatus("PENDING");
        topUp.setCreatedAt(LocalDateTime.now());
        topUp.setUpdatedAt(LocalDateTime.now());
        return topUpRepository.create(topUp);
    }

    @Override
    public List<TopUp> getUserTopUps(String userId) {
        return topUpRepository.findByUserId(userId);
    }

    @Override
    public boolean deleteTopUp(String topUpId) {
        return topUpRepository.deleteTopUp(topUpId);
    }

    @Override
    public List<TopUp> getAllTopUps() {
        List<TopUp> allTopUps = new ArrayList<>();
        for (Iterator<TopUp> it = topUpRepository.findAll(); it.hasNext(); ) {
            TopUp topUp = it.next();
            allTopUps.add(topUp);
        }
        return allTopUps;
    }
}
