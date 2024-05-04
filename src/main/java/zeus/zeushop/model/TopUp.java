package zeus.zeushop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

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

    // Default constructor required by JPA
    public TopUp() {
        this.createdAt = LocalDateTime.now(); // Initialize created time at the moment of creation
        this.updatedAt = LocalDateTime.now(); // Initialize updated time at the moment of creation
    }

    // Additional constructors for business logic or initialization
    public TopUp(String userId, int amount, String status) {
        this.userId = userId;
        this.amount = amount;
        this.status = status;
        this.createdAt = LocalDateTime.now(); // Initialize created time at the moment of creation
        this.updatedAt = LocalDateTime.now(); // Initialize updated time at the moment of creation
    }
}