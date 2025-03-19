package com.vanlang.webbanhang.controller;

import com.vanlang.webbanhang.model.CartItem;
import com.vanlang.webbanhang.model.Order;
import com.vanlang.webbanhang.service.CartService;
import com.vanlang.webbanhang.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @GetMapping("/checkout")
    public String checkout() {
        return "cart/checkout"; // Chuyển hướng đến template checkout.html trong thư mục cart
    }

    @PostMapping("/submit")
    public String submitOrder(
            @RequestParam String customerName,
            @RequestParam String shippingAddress,
            @RequestParam String phoneNumber,
            @RequestParam String email,
            @RequestParam String notes,
            @RequestParam String paymentMethod) {

        List<CartItem> cartItems = cartService.getCartItems();
        if (cartItems.isEmpty()) {
            return "redirect:/cart"; // Redirect nếu giỏ hàng rỗng
        }
        orderService.createOrder(customerName, shippingAddress, phoneNumber, email, notes, paymentMethod, cartItems);
        return "redirect:/order/confirmation"; // Redirect đến trang xác nhận đơn hàng
    }

    @GetMapping("/confirmation")
    public String orderConfirmation(Model model) {
        model.addAttribute("message", "Your order has been successfully placed.");
        return "cart/order-confirmation"; // Chuyển hướng đến template order-confirmation.html trong thư mục cart
    }

    @GetMapping
    public String listOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        long orderCount = orderService.countOrders(); // Đếm số lượng đơn hàng
        model.addAttribute("orders", orders);
        model.addAttribute("orderCount", orderCount); // Thêm số lượng đơn hàng vào model
        return "order/order-list"; // Chuyển hướng đến template order-list.html trong thư mục order
    }

    @GetMapping("/edit/{id}")
    public String editOrder(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "order/edit-order"; // Chuyển hướng đến template edit-order.html trong thư mục order
    }

    @PostMapping("/update")
    public String updateOrder(@ModelAttribute Order order) {
        orderService.updateOrder(order);
        return "redirect:/order"; // Redirect đến danh sách đơn hàng sau khi cập nhật
    }

    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable("id") Long id) {
        orderService.deleteOrderById(id);
        return "redirect:/order";
    }
}
