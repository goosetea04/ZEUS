package zeus.zeushop.service;

import zeus.zeushop.model.TopUp;

import java.util.List;

public interface TopUpService {
    TopUp createTopUp(TopUp topUp);
    List<TopUp> getUserTopUps(String userId);
    boolean deleteTopUp(String topUpId);
    List<TopUp> getAllTopUps();
}
