package zeus.zeushop.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
public class StaffBoard {
    private String staffId;
    private List<TopUp> topUps;

    public void approveTopUp(String topUpId) {
        topUps.stream()
                .filter(topUp -> topUp.getTopUpId().equals(topUpId) && topUp.getStatus().equals("PENDING"))
                .findFirst()
                .ifPresent(topUp -> {
                    topUp.setStatus("APPROVED");
                    topUp.setUpdatedAt(LocalDateTime.now());
                });
    }

    public List<TopUp> filterTopUpsByStatus(String status) {
        return topUps.stream()
                .filter(topUp -> topUp.getStatus().equals(status))
                .collect(Collectors.toList());
    }
}
