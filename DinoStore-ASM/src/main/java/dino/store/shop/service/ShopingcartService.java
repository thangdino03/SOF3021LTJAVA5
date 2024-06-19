package dino.store.shop.service;

import java.util.Collection;

import dino.store.shop.domain.CartItem;



public interface ShopingcartService {
	void add(CartItem item);

    void remove(Long productId);

    Collection<CartItem> getCartItems();

    void clear();

    void update(Long productId, int quantity);

    double getAmount();

    int getCount();

	double getTotalPrice();
}
