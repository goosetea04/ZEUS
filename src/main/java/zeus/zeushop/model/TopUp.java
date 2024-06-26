package zeus.zeushop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter @Setter
@Entity
@Table(name = "top_ups", schema = "public") // Define the table name and schema
public class TopUp {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private String topUpId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private String status; // Values like PENDING, APPROVED, CANCELLED

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public TopUp() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public TopUp(String userId, int amount, String status) {
        this.userId = userId;
        this.amount = amount;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    public String getFormattedCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
        return formatter.format(this.createdAt);
    }
}