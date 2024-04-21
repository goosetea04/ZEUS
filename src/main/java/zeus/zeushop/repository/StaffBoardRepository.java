package zeus.zeushop.repository;

import zeus.zeushop.model.TopUp;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import java.util.Spliterators;

@Repository
public class StaffBoardRepository {

    private final TopUpRepository topUpRepository;

    // Constructor to inject the TopUpRepository dependency
    public StaffBoardRepository(TopUpRepository topUpRepository) {
        this.topUpRepository = topUpRepository;
    }

    // Method to get all top-ups
    public List<TopUp> findAllTopUps() {
        return toList(topUpRepository.findAll());
    }

    // Method to find all top-ups by status
    public List<TopUp> findAllTopUpsByStatus(String status) {
        List<TopUp> allTopUps = findAllTopUps();
        return allTopUps.stream()
                .filter(topUp -> topUp.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    // Method to approve a pending top-up
    public boolean approveTopUp(String topUpId) {
        List<TopUp> allTopUps = findAllTopUps();
        for (TopUp topUp : allTopUps) {
            if (topUp.getTopUpId().equals(topUpId) && "PENDING".equalsIgnoreCase(topUp.getStatus())) {
                topUp.setStatus("APPROVED");
                return true;
            }
        }
        return false; // Top-up not found or not pending
    }

    // Utility method to convert Iterator to List
    private List<TopUp> toList(Iterator<TopUp> iterator) {
        List<TopUp> list = new ArrayList<>();
        iterator.forEachRemaining(list::add);
        return list;
    }
}
