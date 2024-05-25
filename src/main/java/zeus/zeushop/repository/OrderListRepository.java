package zeus.zeushop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zeus.zeushop.model.OrderList;

import java.util.List;

@Repository
public interface OrderListRepository extends JpaRepository<OrderList, Long> {
    List<OrderList> findBySellerIdAndPaymentStatus(Integer sellerId, String status);
}