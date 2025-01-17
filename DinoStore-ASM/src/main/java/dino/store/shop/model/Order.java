package dino.store.shop.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
	private int OrderId;
	private Date orderDate;
	private int customerId;
	private double amount;
	private short status;
}
