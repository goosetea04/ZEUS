package zeus.zeushop.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TopUp {
    private String topUpId;
    private String userId;
    private double amount;
    private String status; // for values like PENDING, APPROVED, CANCELLED
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
