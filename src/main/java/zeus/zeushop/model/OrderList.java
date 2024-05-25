package zeus.zeushop.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import java.time.LocalDateTime;

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

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Column
    private int quantity;

    @Column
    private Float price;

    @Column
    private Integer sellerId;

    @Column
    private Integer buyerId;

    @OneToOne
    @JoinColumn(name = "cart_item_id")
    private CartItem cartItem;

    // Constructors, getters and setters are managed by Lombok
}