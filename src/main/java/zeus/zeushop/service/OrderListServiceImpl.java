package zeus.zeushop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zeus.zeushop.model.OrderList;
import zeus.zeushop.repository.OrderListRepository;
import java.util.List;

@Service
public class OrderListServiceImpl implements OrderListService {
    @Autowired
    private OrderListRepository orderListRepository;

    @Override
    public List<OrderList> findOrdersBySellerAndStatus(Integer sellerId, String status) {
        return orderListRepository.findBySellerIdAndPaymentStatus(sellerId, status);
    }
}
