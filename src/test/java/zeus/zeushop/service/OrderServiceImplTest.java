package zeus.zeushop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import zeus.zeushop.model.Order;
import zeus.zeushop.repository.OrderRepository;
import zeus.zeushop.repository.OrderItemRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateOrder() {
        Order order = new Order();
        when(orderRepository.save(order)).thenReturn(order);
        Order createdOrder = orderService.createOrder(order);
        assertEquals(order, createdOrder);
        verify(orderRepository).save(order);
    }

    @Test
    void testGetOrdersByUserId() {
        Long userId = 1L;
        List<Order> expectedOrders = Arrays.asList(new Order(), new Order());
        when(orderRepository.findByUserId(userId)).thenReturn(expectedOrders);
        List<Order> orders = orderService.getOrdersByUserId(userId);
        assertEquals(expectedOrders, orders);
        verify(orderRepository).findByUserId(userId);
    }

    @Test
    void testGetOrderById() {
        Long orderId = 1L;
        Order expectedOrder = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(expectedOrder));
        Order order = orderService.getOrderById(orderId);
        assertEquals(expectedOrder, order);
        verify(orderRepository).findById(orderId);
    }

    @Test
    void testUpdateOrderStatus() {
        Long orderId = 1L;
        String status = "APPROVED";
        Order existingOrder = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(existingOrder)).thenReturn(existingOrder);

        Order updatedOrder = orderService.updateOrderStatus(orderId, status);
        assertEquals(status, updatedOrder.getStatus());
        verify(orderRepository).save(existingOrder);
    }

    @Test
    void testUpdateOrderStatusNotFound() {
        Long orderId = 1L;
        String status = "APPROVED";
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        Order result = orderService.updateOrderStatus(orderId, status);
        assertNull(result);
        verify(orderRepository, never()).save(any(Order.class));
    }
}
