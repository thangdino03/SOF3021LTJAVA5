package dino.store.shop.controller.admin;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import dino.store.shop.domain.Category;
import dino.store.shop.domain.Customer;
import dino.store.shop.domain.Product;
import dino.store.shop.model.CategoryDto;
import dino.store.shop.model.ProductDto;
import dino.store.shop.repository.ProductReponsitory;
import dino.store.shop.service.CategoryService;
import dino.store.shop.service.ProductService;
import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("admin/products")
public class ProductController {
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ProductService productService;
	@Autowired
	HttpSession session;
	
	@GetMapping("add")
	public String add(Model model) {

		ProductDto productDto = new ProductDto();
        productDto.setEnteredDate(new Date()); // Set current date
        model.addAttribute("product", productDto);
        model.addAttribute("categories", categoryService.findAll());
		
        return "admin/products/addOrEdit";
	}

	@GetMapping("edit/{productId}")
	public ModelAndView edit(ModelMap model, @PathVariable("productId") Long productId) {
		
		Optional<Product>opt= productService.findById(productId); 
		ProductDto dto = new ProductDto();
		if(opt.isPresent()) {
			Product entity = opt.get();
			BeanUtils.copyProperties(entity, dto);
			dto.setIsEdit(true);
			
			 // Set default entered date if not provided
	        if (dto.getEnteredDate() == null) {
	            dto.setEnteredDate(new Date());
	        }
			
			 // Get all categories
	        List<Category> categories = categoryService.findAll();

	        // Convert categories to CategoryDto
	        List<CategoryDto> categoryDtos = categories.stream()
	                .map(category -> {
	                    CategoryDto categoryDto = new CategoryDto();
	                    BeanUtils.copyProperties(category, categoryDto);
	                    return categoryDto;
	                })
	                .collect(Collectors.toList());
	        
			model.addAttribute("product", dto);
			 model.addAttribute("categories", categoryDtos);
			 
			return new ModelAndView("admin/products/addOrEdit", model);
		}
		model.addAttribute("message", "category is not existed");
		
		return new ModelAndView ("forward:admin/products", model);
	}
	
	@GetMapping ("delete/{productId}")
	public ModelAndView delete(ModelMap model, @PathVariable("productId") Long productId) {
	    // Gọi ProductService để xóa sản phẩm dựa trên ID
	    productService.deleteById(productId);
	    
		model.addAttribute("message", "category is deleted! ");
		
		
		return new ModelAndView("forward:/admin/products", model);
	}
	
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

	
//	@PostMapping("saveOrUpdate")
//	public ModelAndView SaveOrUpdate(ModelMap model,
//			@Valid @ModelAttribute("category") CategoryDto dto, BindingResult result) {
//		
//		if(result.hasErrors()) {
//			return new ModelAndView("admin/products/addOrEdit");
//		}
//		
//		Category entity = new Category();
//		BeanUtils.copyProperties(dto, entity);
//		
//		categoryService.save(entity);
//		model.addAttribute("message", "category is saved");
//		return new ModelAndView("forward:/admin/products", model);
//	}
	
	@PostMapping("saveOrUpdate")
	public ModelAndView saveOrUpdateProduct(@ModelAttribute("product") ProductDto productDto, ModelMap model,@RequestParam("categoryId") Long categoryId) {
	    // Convert ProductDto to Product entity
	    Product product = convertToEntity(productDto);
	    
	 // Set image URL
	    product.setImage(productDto.getImageUrl());   
	    // Tìm đối tượng Category dựa trên categoryId
	    Optional<Category> categoryOptional = categoryService.findById(categoryId);
	    if (categoryOptional.isPresent()) {
	        Category category = categoryOptional.get();
	        product.setCategory(category);

	        // Call ProductService to save or update product
	        productService.save(product);

	       
	        // Add a success message
	        model.addAttribute("message", "Sản phẩm đã được lưu.");

	        // Redirect to the product list page
	        return new ModelAndView("redirect:/admin/products", model);
	    } else {
	        // Xử lý khi không tìm thấy Category với ID cụ thể
	        model.addAttribute("message", "Không tìm thấy danh mục với ID cung cấp.");
	        return new ModelAndView("redirect:/admin/products", model);
	    }
	}
	private Product convertToEntity(ProductDto productDto) {
	    Product product = new Product();
	 // Set default entered date if not provided
        if (productDto.getEnteredDate() == null) {
            productDto.setEnteredDate(new Date());
        }
	    BeanUtils.copyProperties(productDto, product);  
	    return product;
	}
	

	
	
	
	@RequestMapping("")
	public String list(ModelMap model) {
		List<Product> list = productService.findAll();
		
		model.addAttribute("products", list);
		
		return "admin/products/list";
	}
	
	@GetMapping("search")
	public String search(ModelMap model, @RequestParam(name = "name", required = false) String name) {
		
		List<Category> list = null;
		if(StringUtils.hasText(name)) {
			list = categoryService.findByNameContaining(name);
		}else {
			list = categoryService.findAll();
		}
		model.addAttribute("products", list);
		
		return "admin/products/search";
	}
	
	
	
	@Autowired
	ProductReponsitory productReponsitory;
	@GetMapping("searchpaginated")
	public String searchAndPage(ModelMap model,
								@RequestParam(name = "min", required = false) Double minPrice,
					            @RequestParam(name = "max", required = false) Double maxPrice,					        
	                            @RequestParam("page") Optional<Integer> page,
	                            @RequestParam("size") Optional<Integer> size) {

		Customer customer = (Customer) session.getAttribute("user");

		if (customer == null) {
		    return "redirect:/account/login";
		}

		
	    int currentPage = page.orElse(1);
	    int pageSize = size.orElse(5);

	    Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("productId"));
	    Page<Product> resultPage;

	    if (minPrice != null && maxPrice != null) {
	        resultPage = productReponsitory.findByUnitPriceBetween(minPrice, maxPrice, pageable);
	        model.addAttribute("min", minPrice);
	        model.addAttribute("max", maxPrice);
	    } else {
	        resultPage = productService.findAll(pageable);
	    }

	    int totalPages = resultPage.getTotalPages();
	    if (totalPages > 0) {
	        int start = Math.max(1, currentPage - 2);
	        int end = Math.min(currentPage + 2, totalPages);

	        if (totalPages > 5) {
	            if (end == totalPages) {
	                start = end - 5;
	            } else if (start == 1) {
	                end = start + 5;
	            }
	        }

	        List<Integer> pageNumbers = IntStream.rangeClosed(start, end)
	                                             .boxed()
	                                             .collect(Collectors.toList());

	        model.addAttribute("pageNumbers", pageNumbers);
	    }

	    model.addAttribute("productPage", resultPage);

	    return "admin/products/searchpaginated";
	}
	
	
//	@GetMapping("productHome")
//	public String listProducts(Model model) {
//	    model.addAttribute("products", productService.findAll());
//	    return "admin/products/productHome";

//	}


}
