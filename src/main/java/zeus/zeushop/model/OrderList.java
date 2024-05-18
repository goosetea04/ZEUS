package zeus.zeushop.model;

import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter @Setter
@Entity
@Table(name = "order_list", schema = "public")
public class OrderList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "listing_id")
    private Listing listing;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private Float price;

    @Column(name = "status")
    private String status;

    public OrderList() {
        // Default constructor required by JPA
    }

    public OrderList(Listing listing, int quantity, float price) {
        this.listing = listing;
        this.quantity = quantity;
        this.price = price;
    }

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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}