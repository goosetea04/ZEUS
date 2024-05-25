package zeus.zeushop.service.strategies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zeus.zeushop.model.TopUp;
import zeus.zeushop.model.User;
import zeus.zeushop.repository.TopUpRepository;
import zeus.zeushop.repository.UserRepository;

import java.math.BigDecimal;

@Component
public class ApproveTopUpStrategy implements AdministrativeAction<TopUp> {
    @Autowired
    private TopUpRepository topUpRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean execute(TopUp topUp) {
        if ("PENDING".equals(topUp.getStatus())) {
            User user = userRepository.findByUsername(topUp.getUserId());
            if (user != null) {
                user.setBalance(user.getBalance().add(BigDecimal.valueOf(topUp.getAmount())));
                userRepository.save(user);
                topUp.setStatus("APPROVED");
                topUpRepository.save(topUp);
                return true;
            }
        }
        return false;
    }
}
