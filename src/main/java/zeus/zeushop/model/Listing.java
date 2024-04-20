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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer product_id;

    private String product_name;

    private Integer product_quantity;

    private Float product_price;

    private LocalDateTime endDate;

    @Transient
    public Boolean isFeatured() {
        return LocalDateTime.now().isBefore(endDate);
    }
}