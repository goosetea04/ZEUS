package zeus.zeushop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Setter @Getter
@Entity
@Table(name = "listing", schema = "public")
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer product_id;
    private boolean visible;
    private String product_name;
    private Integer product_quantity;
    private Float product_price;
    private Integer seller_id;
    private String product_description;

    private LocalDateTime endDate;

    public Listing() {
        // Default constructor required by JPA
    }

    public Listing(String productName, LocalDateTime endDate) {
        this.product_name = productName;
        this.endDate = endDate;
    }

    @Transient
    public Boolean isFeatured() {
        return endDate != null && LocalDateTime.now().isBefore(endDate);
    }
    public Integer getId() {
        return product_id;
    }
    public void setId(Integer product_id) {
        this.product_id = product_id;
    }

    public boolean isVisible() {
        return visible;
    }
    public void setSellerId(Integer seller_id) {
        this.seller_id = seller_id;
    }
    public Integer getSellerId() {
        return seller_id;
    }

    public void setVisible(boolean visible){
        this.visible = visible;
    }
    public Listing(Long id, String productName, int quantity, String description, double price, Long sellerId, boolean visible) {
        this.product_id = id.intValue(); // Convert Long to Integer
        this.product_name = productName;
        this.product_quantity = quantity;
        this.product_description = description;
        this.product_price = (float) price; // Convert double to Float
        this.seller_id = sellerId.intValue(); // Convert Long to Integer
        this.visible = visible;
    }
}
