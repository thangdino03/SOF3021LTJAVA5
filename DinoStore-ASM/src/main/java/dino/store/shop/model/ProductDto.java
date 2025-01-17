package dino.store.shop.model;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
	private Long ProductId;
	private String name;
	private int quantity;
	private double unitPrice;
	private String imageUrl;
	
	private MultipartFile imageFile;
	
	
	private String description;
	private double discount;
	private Date enteredDate;
	private short status;
	private Long categoryId;
	
	private Boolean isEdit = false;
}
