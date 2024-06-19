package dino.store.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import dino.store.shop.domain.Customer;
import dino.store.shop.domain.Order;

public interface OrderReponsitory extends JpaRepository<Order, Long>{
	 @Query("SELECT o FROM Order o WHERE o.customer = :customer")
	    List<Order> findByCustomer(Customer customer);
	 
	// Xóa đơn hàng theo OrderId
	    void deleteById(Long orderId);
}
