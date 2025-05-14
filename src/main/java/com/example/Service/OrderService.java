package com.example.Service;

import com.example.Entity.Order;
import com.example.Entity.User;
import com.example.Repository.OrderRepository;
import com.example.Repository.UserRepository;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;
    
    public List<Order> findAllByUserId(int userId){
    	return orderRepository.findAllByUserId(userId);
    }
    
    public List<Order> findAllByUserAndStatus(int userId, String status){
    	return orderRepository.findAllByUserIdAndStatus(userId, status);
    }
    
    public void save(Order order) {
    	orderRepository.save(order);
    }

//    public Order createOrder(int userId, String name, String url, String description, String language, String slug,
//            Double totalAmount) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + userId));
//
//        Order order = new Order();
//        order.setName(name);
//        order.setUrl(url);
//        order.setDescription(description);
//        order.setLanguage(language);
//        order.setSlug(slug);
//        order.setTotalAmount(totalAmount);
//        order.setItemAmount(0.0); // Có thể tính toán itemAmount sau
//        order.setQuantity(1); // Lúc tạo mới, số lượng là 1
//        order.setQuantityDone(0); // Ban đầu là 0
//        order.setTurnQuantity(0);
//        order.setTurnQuantityDone(0);
//        order.setStatus("PENDING"); // Trạng thái ban đầu là chờ xử lý
//        order.setCreatedAt(new Date());
//        order.setUser(user);
//
//        return orderRepository.save(order); // Lưu đơn vào cơ sở dữ liệu
//    }

//    // Xử lý thanh toán và áp dụng giảm giá theo rank
//    public Order processOrderPayment(int orderId) {
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + orderId));
//
//        // Lấy người dùng liên quan
//        User user = order.getUser();
//
//        // Tính toán giảm giá dựa trên rank của người dùng
//        double discountPercentage = user.getBonusAmount(); // Lấy % giảm giá từ rank
//        double discountAmount = (order.getTotalAmount() * discountPercentage) / 100;
//
//        // Áp dụng giảm giá vào tổng số tiền của đơn hàng
//        double finalAmount = order.getTotalAmount() - discountAmount;
//
//        // Cập nhật lại tổng số tiền của đơn hàng
//        order.setTotalAmount(finalAmount);
//
//        // Cập nhật trạng thái đơn hàng thành đã thanh toán
//        order.setStatus("PAID");
//
//        // Lưu đơn hàng đã thay đổi
//        orderRepository.save(order);
//
//        return order;
//    }
}
