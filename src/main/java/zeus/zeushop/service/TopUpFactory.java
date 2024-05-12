package zeus.zeushop.service;

import zeus.zeushop.model.TopUp;

public class TopUpFactory {

    public interface TopUpFactoryInterface {
        TopUp createTopUp(String userId, int amount);
    }

    public static class SmallAmountTopUpFactory implements TopUpFactoryInterface {
        @Override
        public TopUp createTopUp(String userId, int amount) {
            // Small amounts are directly approved
            return new TopUp(userId, amount, "APPROVED");
        }
    }

    public static class BigAmountTopUpFactory implements TopUpFactoryInterface {
        @Override
        public TopUp createTopUp(String userId, int amount) {
            // Big amounts need pending verification
            return new TopUp(userId, amount, "PENDING_VERIFICATION");
        }
    }
}
