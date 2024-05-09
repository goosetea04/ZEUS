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
@Table(name = "listing", schema = "public")
public class ListingSell {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer product_id;
    private boolean visible;
    private String product_name;
    private Integer product_quantity;
    private Float product_price;
    private String product_description;
    private LocalDateTime endDate;

    public ListingSell() {
        // Default constructor required by JPA
    }

    public ListingSell(String productName, LocalDateTime endDate) {
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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible){
        this.visible = visible;
    }


//    private String id;
//    private String name;
//    private String description;
//    private int stock;
//    private int price;
//
//    public ListingSell() {
//        id = UUID.randomUUID().toString();
//    }
}