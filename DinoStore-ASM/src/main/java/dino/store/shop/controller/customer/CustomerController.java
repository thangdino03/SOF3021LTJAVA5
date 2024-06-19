package dino.store.shop.controller.customer;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import dino.store.shop.domain.Customer;
import dino.store.shop.model.CustomerDto;
import dino.store.shop.repository.CustomerRepository;
import dino.store.shop.service.CustomerService;
import dino.store.shop.service.MailService;
import java.util.stream.IntStream;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("account")
public class CustomerController {

	  @Autowired
	    private CustomerService customerService;
	  

	    @Autowired
	    private HttpSession session;

	    @Autowired
	    CustomerRepository customerRepository;
	    @Autowired
	    private MailService mailService;
	    
	    @GetMapping("/login")
	    public String showLoginForm() {
	        return "account/login";
	    }

	    @PostMapping("/login")
	    public String processLogin(@RequestParam String name, @RequestParam String password, Model model) {
	        Customer customer = customerService.findByNameAndPassword(name, password);
	        if (customer != null) {
	            session.setAttribute("user", customer);
	            return "redirect:/products/home";
	        } else {
	            model.addAttribute("message", "Invalid username or password");
	            return "account/login";
	        }
	    }

	    
	    
	    
	    
	    
	    @GetMapping("/register")
	    public String showRegistrationForm(Model model) {
	        model.addAttribute("customerDto", new CustomerDto());
	        return "account/register";
	    }

	    @PostMapping("/register")
	    public String register(@ModelAttribute("customerDto") CustomerDto customerDto, Model model) {
	        Customer customer = new Customer();
	        try {
	            BeanUtils.copyProperties(customerDto, customer);
	            customerService.save(customer);
	            model.addAttribute("message", "Registration successful");
	        } catch (Exception e) {
	            e.printStackTrace();
	            model.addAttribute("message", "Error during registration: " + e.getMessage());
	            return "account/register";
	        }
	        return "account/login";
	    }

	    @GetMapping("/profile")
	    public String showProfile(Model model, HttpSession session) {
	        Customer customer = (Customer) session.getAttribute("user");
	        if (customer == null) {
	            return "redirect:/account/login";
	        }

	        // Tạo và đưa customerDto vào model để render template
	        CustomerDto customerDto = new CustomerDto();
	        // Sao chép thông tin từ customer vào customerDto
	        BeanUtils.copyProperties(customer, customerDto);

	        model.addAttribute("customerDto", customerDto);
	        return "account/profile";
	    }

	    @PostMapping("/profile")
	    public String updateProfile(@ModelAttribute("customerDto") @Valid CustomerDto customerDto,
	                                BindingResult bindingResult, Model model, HttpSession session) {
	        if (bindingResult.hasErrors()) {
	            return "account/profile";
	        }

	        Customer customer = (Customer) session.getAttribute("user");
	        if (customer == null) {
	            return "redirect:/account/login";
	        }

	        // Kiểm tra nếu password null hoặc rỗng, giữ lại password cũ
	        if (customerDto.getPassword() == null || customerDto.getPassword().isEmpty()) {
	            customerDto.setPassword(customer.getPassword());
	        }

	        // Cập nhật đối tượng customer với dữ liệu từ customerDto
	        BeanUtils.copyProperties(customerDto, customer, "customerId");

	        try {
	            customerService.save(customer); // Lưu thông tin cập nhật vào cơ sở dữ liệu
	            model.addAttribute("message", "Profile updated successfully");
	        } catch (Exception e) {
	            model.addAttribute("message", "Error updating profile: " + e.getMessage());
	            return "account/profile";
	        }

	        return "account/profile";
	    }


	   

	    @GetMapping("/changepassword")
	    public String showChangePasswordForm(Model model) {
	        Customer customer = (Customer) session.getAttribute("user");
	        if (customer == null) {
	            return "redirect:/account/login";
	        }
	        model.addAttribute("customer", customer);
	        return "account/changePassword";
	    }

	    @PostMapping("/changepassword")
	    public String changePassword(@RequestParam("oldPassword") String oldPassword,
	                                 @RequestParam("newPassword") String newPassword,
	                                 @RequestParam("confirmPassword") String confirmPassword,
	                                 Model model) {
	        Customer customer = (Customer) session.getAttribute("user");

	        if (customer == null) {
	            return "redirect:/account/login";
	        }

	        // Kiểm tra xác thực mật khẩu cũ
	        if (!customer.getPassword().equals(oldPassword)) {
	            model.addAttribute("message", "Mật khẩu cũ không đúng");
	            return "account/changePassword";
	        }

	        // Kiểm tra xác nhận mật khẩu mới
	        if (!newPassword.equals(confirmPassword)) {
	            model.addAttribute("message", "Mật khẩu mới và xác nhận mật khẩu không khớp");
	            return "account/changePassword";
	        }

	        // Cập nhật mật khẩu mới cho customer
	        customer.setPassword(newPassword);

	        try {
	            customerService.save(customer); // Lưu thông tin cập nhật vào cơ sở dữ liệu
	            model.addAttribute("message", "Đổi mật khẩu thành công");
	            return "redirect:/account/profile";
	        } catch (Exception e) {
	            model.addAttribute("message", "Lỗi khi cập nhật mật khẩu: " + e.getMessage());
	            return "account/changePassword";
	        }
	    }

	   

	    

	    @GetMapping("/edit")
	    public String edit() {
	        return "account/edit";
	    }

	    @GetMapping("/logout")
	    public String logout() {
	        session.removeAttribute("user");
	        return "redirect:/products/home";
	    }
	    
	    
	    
	    
	    @GetMapping("/forgotpassword")
	    public String showForgotPasswordForm() {
	        return "account/forgotpassword";
	    }

	    @PostMapping("/forgotpassword")
	    public String processForgotPassword(@RequestParam("name") String name,
	                                        @RequestParam("email") String email,
	                                        Model model) {
	        Customer customer = customerService.findByNameAndEmail(name, email);

	        if (customer == null) {
	            model.addAttribute("message", "Không tìm thấy người dùng với tên đăng nhập và email này");
	            return "account/forgotpassword";
	        }

	        try {
	            // Gửi email chứa mật khẩu đến email của khách hàng
	            sendPasswordByEmail(customer);

	            model.addAttribute("message", "Mật khẩu đã được gửi đến email của bạn");
	            return "redirect:/account/login";
	        } catch (MessagingException e) {
	            e.printStackTrace();
	            model.addAttribute("message", "Đã xảy ra lỗi khi gửi email. Vui lòng thử lại sau.");
	            return "account/forgotpassword";
	        }
	    }

	    private void sendPasswordByEmail(Customer customer) throws MessagingException {
	        // Tạo nội dung email
	        String recipientAddress = customer.getEmail();
	        String subject = "Lấy lại mật khẩu cho " + customer.getFullname() ;
	        String content = "Xin chào " + customer.getName()+ " - " + customer.getFullname() + ", ==>"
	                        + "\n ==> Mật khẩu của bạn là: " + customer.getPassword() + ".\n\n"
	                        + "\n ==> Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi ^_^ ";

	        // Gửi email
	        mailService.sendSimpleMessage(recipientAddress, subject, content);
	    }
	    
//	    @GetMapping("/register")
//	    public String showRegistrationForm(Model model) {
//	        model.addAttribute("customerDto", new CustomerDto());
//	        return "account/register";
//	    }
//
//	    @PostMapping("/register")
//	    public String register(@ModelAttribute("customerDto") CustomerDto customerDto, Model model) {
//	        Customer customer = new Customer();
//	        try {
//	            BeanUtils.copyProperties(customerDto, customer);
//	            customerService.save(customer);
//	            model.addAttribute("message", "Registration successful");
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	            model.addAttribute("message", "Error during registration: " + e.getMessage());
//	            return "account/register";
//	        }
//	        return "account/login";
//	    }
	    
	    @GetMapping("/admin/customerlist")
	    public String searchAndPage(ModelMap model) {

	        // Lấy danh sách tất cả khách hàng
	        List<Customer> customers = customerRepository.findAll();

	        // Đưa danh sách khách hàng vào model để trả về view
	        model.addAttribute("customerList", customers);

	        return "admin/customers/searchpaginated";
	    }
	    
	    // Phương thức để xóa khách hàng
	    @GetMapping("/delete/{id}")
	    public String deleteCustomer(@PathVariable("id") Long id) {
	        customerRepository.deleteById(id);
	        return "redirect:/account/admin/customerlist"; 
	        
	        
	        
	    }


	    
}
