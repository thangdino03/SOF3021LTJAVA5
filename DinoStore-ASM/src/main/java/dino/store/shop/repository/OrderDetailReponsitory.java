package dino.store.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dino.store.shop.domain.OrderDetail;

public interface OrderDetailReponsitory extends JpaRepository<OrderDetail, Long> {

}
