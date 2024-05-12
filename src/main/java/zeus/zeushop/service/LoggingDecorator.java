package zeus.zeushop.service;

import zeus.zeushop.model.TopUp;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingDecorator extends StaffBoardServiceDecorator {
    private static final Logger logger = LoggerFactory.getLogger(LoggingDecorator.class);

    public LoggingDecorator(StaffBoardService decoratedService) {
        super(decoratedService);
    }

    @Override
    public boolean approveTopUp(String topUpId) {
        logger.info("Approving top up with ID: " + topUpId);
        return super.approveTopUp(topUpId);
    }

    @Override
    public List<TopUp> getTopUpsByStatus(String status) {
        logger.info("Getting top ups by status: " + status);
        return super.getTopUpsByStatus(status);
    }

    @Override
    public List<TopUp> getAllTopUps() {
        logger.info("Getting all top ups");
        return super.getAllTopUps();
    }

    @Override
    public List<TopUp> getUserTopUps(String userId) {
        logger.info("Getting top ups for user ID: " + userId);
        return super.getUserTopUps(userId);
    }
}
