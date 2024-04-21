package zeus.zeushop.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
public class TopUp {
    private String topUpId;
    private String userId;
    private int amount;
    private String status; // for values like PENDING, APPROVED, CANCELLED
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
