package zeus.zeushop.service;

import zeus.zeushop.model.TopUp;
import java.util.List;

public abstract class StaffBoardServiceDecorator implements StaffBoardService {
    protected StaffBoardService decoratedService;

    public StaffBoardServiceDecorator(StaffBoardService decoratedService) {
        this.decoratedService = decoratedService;
    }

    @Override
    public boolean approveTopUp(String topUpId) {
        return decoratedService.approveTopUp(topUpId);
    }

    @Override
    public List<TopUp> getTopUpsByStatus(String status) {
        return decoratedService.getTopUpsByStatus(status);
    }

    @Override
    public List<TopUp> getAllTopUps() {
        return decoratedService.getAllTopUps();
    }

    @Override
    public List<TopUp> getUserTopUps(String userId) {
        return decoratedService.getUserTopUps(userId);
    }
}
