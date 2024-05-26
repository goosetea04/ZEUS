package zeus.zeushop.service;

import zeus.zeushop.model.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(Order order);
    List<Order> getOrdersByUserId(Long userId);
    Order getOrderById(Long id);
    Order updateOrderStatus(Long id, String status);
}




