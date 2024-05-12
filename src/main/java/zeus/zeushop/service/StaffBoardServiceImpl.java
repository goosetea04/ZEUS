package zeus.zeushop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zeus.zeushop.model.TopUp;
import zeus.zeushop.model.User;
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


    @Autowired
    public StaffBoardServiceImpl(TopUpService topUpService, UserRepository userRepository, TopUpRepository topUpRepository) {
        this.topUpService = topUpService;
        this.userRepository = userRepository;
        this.topUpRepository = topUpRepository;
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
    public List<TopUp> getAllTopUps() {
        return topUpService.getAllTopUps();
    }

    @Override
    public List<TopUp> getUserTopUps(String userId) {
        return topUpService.getUserTopUps(userId);
    }
}