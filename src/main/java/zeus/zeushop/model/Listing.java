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

    private String product_name;

    private Integer product_quantity;

    private Float product_price;
    private String seller_id;
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
}