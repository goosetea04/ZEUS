package zeus.zeushop.service;

import zeus.zeushop.model.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(Order order);
    List<Order> getOrdersByUserId(Integer userId);
    List<Order> getOrdersBySellerId(Integer sellerId);
    Order getOrderById(Integer id);
    Order updateOrderStatus(Integer id, String status);
}


