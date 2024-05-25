package zeus.zeushop.service;

import zeus.zeushop.model.OrderList;
import java.util.List;

public interface OrderListService {
    List<OrderList> findOrdersBySellerAndStatus(Integer sellerId, String status);
}
