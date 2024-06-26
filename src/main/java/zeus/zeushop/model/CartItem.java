package zeus.zeushop.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
@Entity
@Table(name = "cart_item")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal price;  // Direct price field added

    @ManyToOne
    @JoinColumn(name = "listing_id")
    private Listing listing;

    @ManyToOne
    @JoinColumn(name = "payment_id")  // This is the new part
    private Payment payment;

    private int quantity;
    @Column(name = "buyer_id")
    private Integer buyerId;

    @Column(name = "status")
    private String status; // Values could be PENDING, APPROVED, REJECTED

    // Constructors
    public CartItem() {
        // Default constructor required by JPA
    }

    public CartItem(Listing listing, int quantity) {
        this.listing = listing;
        this.quantity = quantity;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setBuyerId(Integer buyer_id) {
        this.buyerId = buyer_id;
    }
    public Integer getBuyerId() {
        return buyerId;
    }
}
