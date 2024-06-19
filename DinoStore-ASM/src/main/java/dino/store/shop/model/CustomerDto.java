package dino.store.shop.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
	private Long CustomerId;
	private String name;
	private String email;
	private String password;
	private String phone;
	private boolean isadmin = false;
	private String fullname;
}
