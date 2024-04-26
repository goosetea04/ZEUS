package zeus.zeushop.repository;

import zeus.zeushop.model.TopUp;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


@Repository
public class StaffBoardRepository {

    private final TopUpRepository topUpRepository;

    public StaffBoardRepository(TopUpRepository topUpRepository) {
        this.topUpRepository = topUpRepository;
    }

    public List<TopUp> findAllTopUps() {
        return toList(topUpRepository.findAll());
    }

    public List<TopUp> findAllTopUpsByStatus(String status) {
        List<TopUp> allTopUps = findAllTopUps();
        return allTopUps.stream()
                .filter(topUp -> topUp.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    public boolean approveTopUp(String topUpId) {
        List<TopUp> allTopUps = findAllTopUps();
        for (TopUp topUp : allTopUps) {
            if (topUp.getTopUpId().equals(topUpId) && "PENDING".equalsIgnoreCase(topUp.getStatus())) {
                topUp.setStatus("APPROVED");
                return true;
            }
        }
        return false;
    }

    private List<TopUp> toList(Iterator<TopUp> iterator) {
        List<TopUp> list = new ArrayList<>();
        iterator.forEachRemaining(list::add);
        return list;
    }
}
