package com.vanlang.webbanhang.service;

import com.vanlang.webbanhang.model.CartItem;
import com.vanlang.webbanhang.model.Order;
import com.vanlang.webbanhang.model.OrderDetail;
import com.vanlang.webbanhang.repository.OrderDetailRepository;
import com.vanlang.webbanhang.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private CartService cartService; // Assuming you have a CartService

    @Transactional
    public Order createOrder(String customerName, String shippingAddress, String phoneNumber, String email, String notes, String paymentMethod, List<CartItem> cartItems) {
        Order order = new Order();
        order.setCustomerName(customerName);
        order.setShippingAddress(shippingAddress);
        order.setPhoneNumber(phoneNumber);
        order.setEmail(email);
        order.setNotes(notes);
        order.setPaymentMethod(paymentMethod);
        order = orderRepository.save(order);

        for (CartItem item : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(item.getProduct());
            detail.setQuantity(item.getQuantity());
            orderDetailRepository.save(detail);
        }

        // Optionally clear the cart after order placement
        cartService.clearCard();
        return order;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public void updateOrder(Order order) {
        orderRepository.save(order);
    }

    public void deleteOrderById(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new IllegalStateException("Order with ID " + id + " does not exist.");
        }

        // Xóa các chi tiết đơn hàng liên quan trước
        orderDetailRepository.deleteByOrderId(id);

        // Sau đó xóa đơn hàng
        orderRepository.deleteById(id);
    }

    public long countOrders() {
        return orderRepository.count();
    }
}
