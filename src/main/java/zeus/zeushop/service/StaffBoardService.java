package zeus.zeushop.service;

import zeus.zeushop.model.TopUp;

import java.util.List;

public interface StaffBoardService {
    boolean approveTopUp(String topUpId);
    List<TopUp> getTopUpsByStatus(String status);
    List<TopUp> getAllTopUps();
    List<TopUp> getUserTopUps(String userId);
}
