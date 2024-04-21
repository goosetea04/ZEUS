package zeus.zeushop.repository;

import zeus.zeushop.model.TopUp;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

@Repository
public class TopUpRepository {
    private final List<TopUp> topUps = new ArrayList<>();
    public TopUp create(TopUp topUp) {
        topUps.add(topUp);
        return topUp;
    }
    public Iterator<TopUp> findAll() {
        return topUps.iterator();
    }
    public List<TopUp> findByUserId(String userId) {
        List<TopUp> result = new ArrayList<>();
        for (TopUp topUp : topUps) {
            if (topUp.getUserId().equals(userId)) {
                result.add(topUp);
            }
        }
        return result; // return an empty list if no transactions match the userId
    }
    public boolean deleteTopUp(String topUpId) {
        return topUps.removeIf(t -> t.getTopUpId().equals(topUpId));
    }
}