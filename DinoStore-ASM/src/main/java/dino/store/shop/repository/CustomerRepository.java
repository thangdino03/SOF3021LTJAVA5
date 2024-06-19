package dino.store.shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dino.store.shop.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {


	
Customer findByName(String name);
    
   Customer findByfullname(String fullname);
    
    Page<Customer> findAll(Pageable pageable);
    Customer findByNameOrEmail(String name, String email);

    
    Customer findByNameAndPassword(String name, String password);
    
//    Customer findByNameAndEmail(String name, String email);
   
    @Query("SELECT c FROM Customer c WHERE c.name = :name AND c.email = :email")
    Customer findByNameAndEmail(@Param("name") String name, @Param("email") String email);


}
