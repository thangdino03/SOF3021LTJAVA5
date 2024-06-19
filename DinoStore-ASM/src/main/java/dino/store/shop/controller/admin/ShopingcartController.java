package dino.store.shop.controller.admin;


import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import dino.store.shop.domain.CartItem;
import dino.store.shop.domain.Customer;
import dino.store.shop.domain.Order;
import dino.store.shop.domain.OrderDetail;
import dino.store.shop.domain.Product;
import dino.store.shop.repository.CustomerRepository;
import dino.store.shop.repository.OrderDetailReponsitory;
import dino.store.shop.repository.OrderReponsitory;
import dino.store.shop.repository.ProductReponsitory;
import dino.store.shop.service.ProductService;
import dino.store.shop.service.ShopingcartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/shoppingCart")
public class ShopingcartController {
	 
	 @Autowired
	  ProductService productService;
	 @Autowired
	  ShopingcartService shopingcartService;

	 @Autowired
	 ProductReponsitory productReponsitory;
	 @Autowired
	 OrderReponsitory orderReponsitory;
	 @Autowired
	 OrderDetailReponsitory detailReponsitory;
	 @Autowired
	 CustomerRepository customerRepository;
	 @Autowired
	 HttpSession session;

	 public ShopingcartController(ShopingcartService shopingcartService) {
	        this.shopingcartService = shopingcartService;
	    }

	    @GetMapping("/list")
	    public String list(Model model) {
	    	Customer customer = (Customer) session.getAttribute("user");
	    

	    	if (customer == null) {
	    	    return "redirect:/account/login";
	    	}

	        // Lấy danh sách các mặt hàng trong giỏ hàng từ Service
	        Iterable<CartItem> cartItems = shopingcartService.getCartItems();
	        
	        // Lấy tổng số tiền và số lượng các mặt hàng từ Service
	        double total = shopingcartService.getTotalPrice();
	        int noOfItems = shopingcartService.getCount();
	        
	        // Thêm các thuộc tính vào model để hiển thị trên view
	        model.addAttribute("cartItems", cartItems);
	        model.addAttribute("total", total);
	        model.addAttribute("noOfItems", noOfItems);
	        
	        return "shoppingCart/list"; // Trả về view Thymeleaf để hiển thị
	    }

	    @GetMapping("/add/{productId}")
	    public String addCartItem(@PathVariable Long productId) {
	    	Customer customer = (Customer) session.getAttribute("user");
	        CartItem cartItem = new CartItem();
	        productService.findById(productId).ifPresent(product -> {
	            BeanUtils.copyProperties(product, cartItem);
	            cartItem.setQuantity(1);
	            shopingcartService.add(cartItem);
	        });
	        return "redirect:/shoppingCart/list";
	    }

	    @GetMapping("/remove/{productId}")
	    public String removeCartItem(@PathVariable Long productId) {
	        shopingcartService.remove(productId);
	        return "redirect:/shoppingCart/list";
	    }

	    @PostMapping("/update")
	    public String updateCartItem(@RequestParam Long productId, @RequestParam Integer quantity) {
	        shopingcartService.update(productId, quantity);
	        return "redirect:/shoppingCart/list";
	    }

	    @GetMapping("/clear")
	    public String clearCart() {
	        shopingcartService.clear();
	        return "redirect:/shoppingCart/list";
	    }
	    
	   
	    


	    
//	    @PostMapping("/placeOrder")
//	    public ModelAndView placeOrder(@RequestParam("selectedProducts") List<Long> selectedProducts,
//	                                   @RequestParam("quantity") List<Integer> quantities) {
//
//	        // Validate inputs
//	        if (selectedProducts == null || quantities == null || selectedProducts.size() != quantities.size()) {
//	            return new ModelAndView("shoppingCart/error").addObject("message", "Invalid input data.");
//	        }
//
//	        // Create a new Order object
//	        Order order = new Order();
//	        order.setOrderDate(new Date());
//	        order.setStatus((short) 1); // Assuming status 1 means the order is being processed
//
//	        double totalAmount = 0.0;
//	        Set<OrderDetail> orderDetails = new HashSet<>();
//
//	        try {
//	            // Loop through selected products and quantities
//	            for (int i = 0; i < selectedProducts.size(); i++) {
//	                Long productId = selectedProducts.get(i);
//	                Integer quantity = quantities.get(i);
//
//	                // Fetch product information from productId
//	                Product product = productReponsitory.findById(productId).orElse(null);
//
//	                if (product != null) {
//	                    // Create an OrderDetail object for each product
//	                    OrderDetail orderDetail = new OrderDetail();
//	                    orderDetail.setProduct(product);
//	                    orderDetail.setQuantity(quantity);
//	                    orderDetail.setUnitPrice(product.getUnitPrice()); // Assuming unit price is fetched from Product
//
//	                    // Add orderDetail to orderDetails set
//	                    orderDetails.add(orderDetail);
//
//	                    // Calculate the total amount for the order
//	                    totalAmount += quantity * product.getUnitPrice();
//	                } else {
//	                    // Handle the case where a product is not found (optional)
//	                    return new ModelAndView("shoppingCart/error").addObject("message", "Product not found.");
//	                }
//	            }
//
//	            // Set total amount for the order
//	            order.setAmount(totalAmount);
//
//	            // Save the order to the database first to get the orderId
//	            orderReponsitory.save(order);
//
//	            // Loop through orderDetails and set the order to each detail
//	            for (OrderDetail orderDetail : orderDetails) {
//	                orderDetail.setOrder(order);
//	            }
//
//	            // Save all orderDetails to the database
//	            detailReponsitory.saveAll(orderDetails);
//
//	        } catch (Exception e) {
//	            // Handle exceptions (e.g., database errors)
//	            return new ModelAndView("shoppingCart/error").addObject("message", "An error occurred while placing the order.");
//	        }
//
//	        // Redirect or display the appropriate view
//	        return new ModelAndView("shoppingCart/orderSuccess");
//	    }

	    

	    @PostMapping("/placeOrder")
	    public ModelAndView placeOrder(@RequestParam("selectedProducts") List<Long> selectedProducts,
	                                   @RequestParam("quantity") List<Integer> quantities) {

	        // Lấy customer từ session
	    	Customer customer = (Customer) session.getAttribute("user");
	        
	        // Validate inputs
	        if (selectedProducts == null || quantities == null || selectedProducts.size() != quantities.size()) {
	            return new ModelAndView("shoppingCart/error").addObject("message", "Invalid input data.");
	        }

	        // Create a new Order object
	        Order order = new Order();
	        order.setOrderDate(new Date());
	        order.setStatus((short) 1); // Assuming status 1 means the order is being processed
	        order.setCustomer(customer); // Đặt customer cho order
	        
	        double totalAmount = 0.0;
	        Set<OrderDetail> orderDetails = new HashSet<>();

	        try {
	            // Loop through selected products and quantities
	            for (int i = 0; i < selectedProducts.size(); i++) {
	                Long productId = selectedProducts.get(i);
	                Integer quantity = quantities.get(i);

	                // Fetch product information from productId
	                Product product = productReponsitory.findById(productId).orElse(null);

	                if (product != null) {
	                    // Create an OrderDetail object for each product
	                    OrderDetail orderDetail = new OrderDetail();
	                    orderDetail.setProduct(product);
	                    orderDetail.setQuantity(quantity);
	                    orderDetail.setUnitPrice(product.getUnitPrice()); // Assuming unit price is fetched from Product

	                    // Add orderDetail to orderDetails set
	                    orderDetails.add(orderDetail);

	                    // Calculate the total amount for the order
	                    totalAmount += quantity * product.getUnitPrice();
	                } else {
	                    // Handle the case where a product is not found (optional)
	                    return new ModelAndView("shoppingCart/error").addObject("message", "Product not found.");
	                }
	            }

	            // Set total amount for the order
	            order.setAmount(totalAmount);

	            // Save the order to the database first to get the orderId
	            orderReponsitory.save(order);

	            // Loop through orderDetails and set the order to each detail
	            for (OrderDetail orderDetail : orderDetails) {
	                orderDetail.setOrder(order);
	            }

	            // Save all orderDetails to the database
	            detailReponsitory.saveAll(orderDetails);

	        } catch (Exception e) {
	            // Handle exceptions (e.g., database errors)
	            return new ModelAndView("shoppingCart/error").addObject("message", "An error occurred while placing the order.");
	        }

	        // Redirect or display the appropriate view
	        return new ModelAndView("shoppingCart/orderSuccess");
	    }
	    
	    
	    
//	    @PostMapping("/placeOrder")
//	    public ModelAndView placeOrder(@RequestParam("selectedProducts") List<Long> selectedProducts,
//	                                   @RequestParam("quantity") List<Integer> quantities) {
//
//	        // Lấy customer từ session
//	        Customer customer = (Customer) session.getAttribute("user");
//	        
//	        if (customer == null) {
//	            // Nếu customer không tồn tại trong session, trả về trang lỗi
//	            return new ModelAndView("shoppingCart/error").addObject("message", "Customer not logged in.");
//	        }
//
//	        // Validate inputs
//	        if (selectedProducts == null || quantities == null || selectedProducts.size() != quantities.size()) {
//	            return new ModelAndView("shoppingCart/error").addObject("message", "Invalid input data.");
//	        }
//
//	        // Create a new Order object
//	        Order order = new Order();
//	        order.setOrderDate(new Date());
//	        order.setStatus((short) 1); // Assuming status 1 means the order is being processed
//	        order.setCustomer(customer); // Đặt customer cho order
//	        
//	        double totalAmount = 0.0;
//	        Set<OrderDetail> orderDetails = new HashSet<>();
//
//	        try {
//	            // Loop through selected products and quantities
//	            for (int i = 0; i < selectedProducts.size(); i++) {
//	                Long productId = selectedProducts.get(i);
//	                Integer quantity = quantities.get(i);
//
//	                // Fetch product information from productId
//	                Product product = productReponsitory.findById(productId).orElse(null);
//
//	                if (product != null) {
//	                    // Create an OrderDetail object for each product
//	                    OrderDetail orderDetail = new OrderDetail();
//	                    orderDetail.setProduct(product);
//	                    orderDetail.setQuantity(quantity);
//	                    orderDetail.setUnitPrice(product.getUnitPrice()); // Assuming unit price is fetched from Product
//
//	                    // Add orderDetail to orderDetails set
//	                    orderDetails.add(orderDetail);
//
//	                    // Calculate the total amount for the order
//	                    totalAmount += quantity * product.getUnitPrice();
//	                } else {
//	                    // Handle the case where a product is not found (optional)
//	                    return new ModelAndView("shoppingCart/error").addObject("message", "Product not found.");
//	                }
//	            }
//
//	            // Set total amount for the order
//	            order.setAmount(totalAmount);
//
//	            // Save the order to the database first to get the orderId
//	            orderReponsitory.save(order);
//
//	            // Loop through orderDetails and set the order to each detail
//	            for (OrderDetail orderDetail : orderDetails) {
//	                orderDetail.setOrder(order);
//	            }
//
//	            // Save all orderDetails to the database
//	            detailReponsitory.saveAll(orderDetails);
//
//	        } catch (Exception e) {
//	            // Log the exception for debugging
//	            e.printStackTrace();
//	            // Handle exceptions (e.g., database errors)
//	            return new ModelAndView("shoppingCart/error").addObject("message", "An error occurred while placing the order: " + e.getMessage());
//	        }
//
//	        // Redirect or display the appropriate view
//	        return new ModelAndView("shoppingCart/orderSuccess");
//	    }


}
