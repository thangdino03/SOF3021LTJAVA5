package dino.store.shop.controller.customer;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import dino.store.shop.domain.Customer;
import dino.store.shop.domain.Product;
import dino.store.shop.repository.ProductReponsitory;
import dino.store.shop.service.CategoryService;
import dino.store.shop.service.ProductService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/products")
public class cusProductController {

	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	ProductReponsitory productReponsitory;
	
	@Autowired
	HttpSession session;
	
	@RequestMapping("home")
	public String list(ModelMap model) {
		

		
		List<Product> list = productService.findAll();
		
		model.addAttribute("products", list);
		
		return "admin/products/productHome";
	}
	

	
//	@GetMapping("productHome")
//	public String listProducts(Model model) {
//	    model.addAttribute("products", productService.findAll());
////	    return "admin/products/productHome";
//	    return "site/products/productHome";
//	}

	
	@GetMapping("view/{productId}")
	
	public String viewProduct(Model model, @PathVariable("productId") Long productId) {
		Customer customer = (Customer) session.getAttribute("user");
		

		if (customer == null) {
		    return "redirect:/account/login";
		}

	    // Tìm sản phẩm dựa trên productId
	    Optional<Product> productOptional = productService.findById(productId);

	    if (productOptional.isPresent()) {
	        // Nếu sản phẩm tồn tại, lấy thông tin sản phẩm
	        Product product = productOptional.get();
	        
	        // Đặt các thuộc tính của sản phẩm vào model để hiển thị trên trang view
	        model.addAttribute("product", product);

	        // Trả về tên của view để hiển thị
//	        return "admin/products/productDetail";
	        return "admin/products/productDetail";
	        
	    } else {
	        // Nếu không tìm thấy sản phẩm, có thể xử lý lỗi hoặc chuyển hướng về trang danh sách sản phẩm
	        model.addAttribute("message", "Không tìm thấy sản phẩm với ID cung cấp.");
//	        return "redirect:/admin/products";
	        return "redirect:/admin/products";
	    }
	}

	 



}
