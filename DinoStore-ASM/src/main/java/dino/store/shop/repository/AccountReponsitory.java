package dino.store.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dino.store.shop.domain.Account;

public interface AccountReponsitory extends JpaRepository<Account, String>{

}
