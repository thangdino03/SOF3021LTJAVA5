package dino.store.shop.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dino.store.shop.domain.Product;

@Repository
public interface ProductReponsitory extends JpaRepository<Product, Long> {
	List<Product> findByNameContaining(String name);
	Page<Product> findByNameContaining(String name, Pageable pageable);
	
	Page<Product> findAll(Pageable pageable);
	public Page<Product> findByUnitPriceBetween(Double min, Double max, Pageable pageable);
	
}
