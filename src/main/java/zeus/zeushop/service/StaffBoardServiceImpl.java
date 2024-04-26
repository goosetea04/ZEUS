package zeus.zeushop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zeus.zeushop.model.TopUp;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffBoardServiceImpl implements StaffBoardService {

    private final TopUpService topUpService;

    @Autowired
    public StaffBoardServiceImpl(TopUpService topUpService) {
        this.topUpService = topUpService;
    }

    @Override
    public boolean approveTopUp(String topUpId) {
        List<TopUp> topUps = topUpService.getAllTopUps();
        for (TopUp topUp : topUps) {
            if (topUp.getTopUpId().equals(topUpId) && "PENDING".equals(topUp.getStatus())) {
                topUp.setStatus("APPROVED");
                topUpService.createTopUp(topUp);  // Assuming createTopUp updates top-ups; if not, adjust accordingly.
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
