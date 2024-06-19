package dino.store.shop.service;

import java.util.List;

import dino.store.shop.domain.Customer;

public interface CustomerService {

	Customer findByNameOrEmail(String nameOrEmail);
    
	Customer findByNameAndEmail(String name, String email);
	
	void deleteAllCustomer();

	long countCustomer();

	Customer getOne(Long id);

	void deleteAll(Iterable<Customer> entities);

	boolean exists(Customer customer);

	void deleteAllByIdInBatch(Iterable<Long> ids);

	boolean existsById(Long id);

	long count();

	void deleteAllInBatch(Iterable<Customer> entities);

	void deleteInBatch(Iterable<Customer> entities);

	List<Customer> findAllById(Iterable<Long> ids);

	List<Customer> saveAll(Iterable<Customer> entities);

	List<Customer> findAll();

	void delete(Customer customer);

	Customer findByfullname(String fullname);

	Customer findByName(String name);

	Customer findById(Long id);

	void save(Customer customer);

	Customer findByNameAndPassword(String name, String password);
	 
}
