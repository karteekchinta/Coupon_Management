package com.Coupons.Management.Coupons.CouponEntity;

import lombok.Data;

import java.util.List;

@Data
public class Cart {
    private List<CartItem> items;
    private double totalAmount;
    private double discountedTotal;
}
