package dino.store.shop.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import dino.store.shop.domain.CartItem;
import dino.store.shop.model.CartItemDto;
import dino.store.shop.service.ShopingcartService;

@Service
@SessionScope
public class ShopingcartServiceImpl implements ShopingcartService{
	private Map<Long, CartItem> map = new HashMap<>();

    @Override
    public void add(CartItem item) {
        CartItem existedItem = map.get(item.getProductId());
        if (existedItem != null) {
            existedItem.setQuantity(item.getQuantity() + existedItem.getQuantity());
        } else {
            map.put(item.getProductId(), item);
        }
    }

    @Override
    public void remove(Long productId) {
        map.remove(productId);
    }

    @Override
    public Collection<CartItem> getCartItems() {
        return map.values();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public void update(Long productId, int quantity) {
        CartItem item = map.get(productId);
        if (item != null) {
            item.setQuantity(quantity);
            if (item.getQuantity() <= 0) {
                map.remove(productId);
            }
        } else {
            // Xử lý khi item không tồn tại trong giỏ hàng
            // Ví dụ: throw new IllegalArgumentException("CartItem not found for productId: " + productId);
        }
    }

    @Override
    public double getAmount() {
        return map.values().stream().mapToDouble(item -> item.getQuantity() * item.getUnitPrice()).sum();
    }
    
    
	@Override
	public double getTotalPrice() {
	    double totalPrice = 0;
	    for (CartItem item : map.values()) {
	        totalPrice += item.getUnitPrice() * item.getQuantity();
	    }
	    return totalPrice;
	}

    @Override
    public int getCount() {
        return map.size();
    }
}
