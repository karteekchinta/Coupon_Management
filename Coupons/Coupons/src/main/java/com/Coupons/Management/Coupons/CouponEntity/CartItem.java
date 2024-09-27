package com.Coupons.Management.Coupons.CouponEntity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    private Long productId;
    private int quantity;
    private double price;
    private double discountedPrice;

}
