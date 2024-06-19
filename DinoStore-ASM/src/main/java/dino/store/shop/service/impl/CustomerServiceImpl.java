package dino.store.shop.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dino.store.shop.domain.Customer;
import dino.store.shop.repository.CustomerRepository;
import dino.store.shop.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	 @Autowired
	    private CustomerRepository customerRepository;

	 @Override
	    public Customer findByNameOrEmail(String nameOrEmail) {
	        return customerRepository.findByNameOrEmail(nameOrEmail, nameOrEmail);
	    }
	 
	 @Override
	    public Customer findByNameAndEmail(String name, String email) {
	        return customerRepository.findByNameAndEmail(name, email);
	    }
	    @Override
		public void save(Customer customer) {
	        customerRepository.save(customer);
	    }

	    @Override
		public Customer findById(Long id) {
	        Optional<Customer> optionalCustomer = customerRepository.findById(id);
	        return optionalCustomer.orElse(null);
	    }

	    @Override
		public Customer findByName(String name) {
	        return customerRepository.findByName(name);
	    }

	    @Override
		public Customer findByfullname(String fullname) {
	        return customerRepository.findByfullname(fullname);
	    }

	    @Override
		public void delete(Customer customer) {
	        customerRepository.delete(customer);
	    }

	    @Override
		public List<Customer> findAll() {
	        return customerRepository.findAll();
	    }

	    @Override
		public List<Customer> saveAll(Iterable<Customer> entities) {
	        return customerRepository.saveAll(entities);
	    }

	    @Override
		public List<Customer> findAllById(Iterable<Long> ids) {
	        return customerRepository.findAllById(ids);
	    }

	    @Override
		public void deleteInBatch(Iterable<Customer> entities) {
	        customerRepository.deleteInBatch(entities);
	    }

	    @Override
		public void deleteAllInBatch(Iterable<Customer> entities) {
	        customerRepository.deleteAllInBatch(entities);
	    }

	    @Override
		public long count() {
	        return customerRepository.count();
	    }

	    @Override
		public boolean existsById(Long id) {
	        return customerRepository.existsById(id);
	    }

	    @Override
		public void deleteAllByIdInBatch(Iterable<Long> ids) {
	        customerRepository.deleteAllByIdInBatch(ids);
	    }

	    @Override
		public boolean exists(Customer customer) {
	        return customerRepository.existsById(customer.getCustomerId());
	    }

	    @Override
		public void deleteAll(Iterable<Customer> entities) {
	        customerRepository.deleteAll(entities);
	    }

	    @Override
		public Customer getOne(Long id) {
	        return customerRepository.getOne(id);
	    }

	    @Override
		public long countCustomer() {
	        return customerRepository.count();
	    }

	    @Override
		public void deleteAllCustomer() {
	        customerRepository.deleteAll();
	    }
	
	    @Override
		public Customer findByNameAndPassword(String name, String password) {
	        return customerRepository.findByNameAndPassword(name, password);
	    }
	    
	
}
