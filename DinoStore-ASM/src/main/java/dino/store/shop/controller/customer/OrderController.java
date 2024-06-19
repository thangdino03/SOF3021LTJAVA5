package dino.store.shop.controller.customer;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import dino.store.shop.domain.Customer;
import dino.store.shop.domain.Order;
import dino.store.shop.repository.OrderReponsitory;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("orders")
public class OrderController {
	@Autowired
    private OrderReponsitory orderRepository;

    @Autowired
    private HttpSession session;

    @GetMapping("/list")
    public String listOrders(Model model) {
        // Lấy customer từ session
        Customer customer = (Customer) session.getAttribute("user");

        // Kiểm tra nếu customer không tồn tại hoặc không có đơn hàng
        if (customer == null) {
            return "redirect:/account/login"; // Hoặc trả về trang thông báo lỗi
        }

        // Lấy danh sách đơn hàng của customer từ repository
        List<Order> orders = orderRepository.findByCustomer(customer);

        // Đưa danh sách đơn hàng vào model để hiển thị trên view
        model.addAttribute("orders", orders);

        return "orders/list"; // Điều hướng đến trang hiển thị danh sách đơn hàng
    }


    @GetMapping("/detail")
    public String viewOrderDetail(@RequestParam("orderId") Long orderId, Model model) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            model.addAttribute("order", order);
            return "orderDetails";
        } else {
            model.addAttribute("message", "Order not found.");
            return "error";
        }
    }
    
    @GetMapping("/orderDetails/{id}")
    public String viewOrderDetails(@PathVariable("id") Long orderId, Model model) {
        // Lấy thông tin đơn hàng từ repository
        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        // Kiểm tra nếu đơn hàng không tồn tại
        if (!optionalOrder.isPresent()) {
            return "redirect:/orders"; // Hoặc trả về trang thông báo lỗi
        }

        // Lấy đơn hàng từ Optional
        Order order = optionalOrder.get();

        // Đưa đơn hàng vào model để hiển thị trên view
        model.addAttribute("order", order);

        return "orders/details"; // Điều hướng đến trang hiển thị chi tiết đơn hàng
    }
    
//    @DeleteMapping("/delete/{orderId}")
//    @ResponseBody
//    public String deleteOrder(@PathVariable("orderId") Long orderId) {
//        try {
//            orderRepository.deleteById(orderId);
//            return "Đã xóa đơn hàng thành công";
//        } catch (Exception e) {
//            return "Lỗi khi xóa đơn hàng: " + e.getMessage();
//        }
//    }
    @PostMapping("/delete/{orderId}")
    @ResponseBody
    public String deleteOrder(@RequestParam("orderId") Long orderId) {
        try {
            orderRepository.deleteById(orderId);
            return "Đã xóa đơn hàng thành công";
        } catch (Exception e) {
            return "Lỗi khi xóa đơn hàng: " + e.getMessage();
        }
    }

    
    
    
    @GetMapping("/doubledetail/{orderId}")
    public String showOrderDetails(@PathVariable("orderId") Long orderId, Model model) {
        // Retrieve order by orderId
        Order order = orderRepository.findById(orderId)
                                    .orElseThrow(() -> new RuntimeException("Order not found"));

        // Set order and orderDetails to model
        model.addAttribute("order", order);
        model.addAttribute("orderDetails", order.getOrderDetails()); // Đảm bảo orderDetails đã được fetch

        return "orders/orderDetail";
    }

}
