package zeus.zeushop.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class Transaction {
    private Long transactionId;
    private Long userId;
    private TransactionType type;
    private double amount;
    private TransactionStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

enum TransactionType {
    TOP_UP,
    PAYMENT
}

enum TransactionStatus {
    PENDING,
    APPROVED,
    REJECTED
}
