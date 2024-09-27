package com.Coupons.Management.Coupons.CouponService;
import com.Coupons.Management.Coupons.CouponEntity.Cart;
import com.Coupons.Management.Coupons.CouponEntity.CartItem;
import com.Coupons.Management.Coupons.CouponEntity.Coupon;
import com.Coupons.Management.Coupons.CouponRepository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    public Optional<Coupon> getCouponById(Long id) {
        return couponRepository.findById(id);
    }

    public Coupon updateCoupon(Long id, Coupon couponDetails) {
        return couponRepository.findById(id).map(coupon -> {
            coupon.setType(couponDetails.getType());
            coupon.setDetails(couponDetails.getDetails());
            coupon.setRepetitionLimit(couponDetails.getRepetitionLimit());
            return couponRepository.save(coupon);
        }).orElse(null);
    }

    public void deleteCoupon(Long id) {
        couponRepository.deleteById(id);
    }

    //The  logic for fetching and applying applicable coupons for a cart
    public List<Coupon> getApplicableCoupons() {
        // logic for determining applicable coupons based on cart
        return couponRepository.findAll();
    }

    // logic for applying a specific coupon
    public Coupon applyCoupon(Long id) {

        return couponRepository.findById(id).orElse(null);
    }
    public Coupon applyCartWiseCoupon(Coupon coupon, Cart cart) {
        double totalCartAmount = cart.getTotalAmount();
        double discountThreshold = Double.parseDouble(coupon.getDetails().get(0)); // Assuming threshold is at index 0
        double discountPercentage = Double.parseDouble(coupon.getDetails().get(1)); // Assuming discount percentage is at index 1


        if (totalCartAmount < discountThreshold) {
            throw new RuntimeException("Cart total is less than the required threshold for this coupon.");
        }


        if (totalCartAmount == discountThreshold) {
            double discount = totalCartAmount * discountPercentage / 100;
            cart.setDiscountedTotal(totalCartAmount - discount);
        }


        if (totalCartAmount > discountThreshold) {
            double discount = totalCartAmount * discountPercentage / 100;
            cart.setDiscountedTotal(totalCartAmount - discount);
        }

        return coupon;
    }

    //  Handling for Product-wise Coupon
    public Coupon applyProductWiseCoupon(Coupon coupon, Cart cart) {
        Long productId = Long.parseLong(coupon.getDetails().get(0)); // Assuming product ID is at index 0
        double productDiscountPercentage = Double.parseDouble(coupon.getDetails().get(1)); // Assuming discount percentage is at index 1

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .orElse(null);


        if (item == null) {
            throw new RuntimeException("Product not found in cart for this coupon.");
        }


        if (item.getQuantity() < 1) {
            throw new RuntimeException("Insufficient quantity for applying the coupon.");
        }

        double productPrice = item.getPrice();
        double discount = productPrice * productDiscountPercentage / 100;
        item.setDiscountedPrice(productPrice - discount);

        return coupon;
    }

    // Handling for BxGy Coupon
    public Coupon applyBxGyCoupon(Coupon coupon, Cart cart) {

        List<Long> buyProducts = Arrays.stream(coupon.getDetails().get(0).split(","))
                .map(Long::parseLong)
                .toList();

        // Convert the second string (get products) into a list of Longs
        List<Long> getProducts = Arrays.stream(coupon.getDetails().get(1).split(","))
                .map(Long::parseLong)
                .toList();


        int repetitionLimit = coupon.getRepetitionLimit();

        int buyCount = 0;
        int getCount = 0;
        for (CartItem item : cart.getItems()) {
            if (buyProducts.contains(item.getProductId())) {
                buyCount += item.getQuantity();
            }
            if (getProducts.contains(item.getProductId())) {
                getCount += item.getQuantity();
            }
        }

        if (buyCount < 2) {
            throw new RuntimeException("Not enough buy items for this BxGy coupon.");
        }
        if (buyCount / 2 > repetitionLimit) {
            throw new RuntimeException("Coupon repetition limit exceeded.");
        }
        if (getCount == 0) {
            throw new RuntimeException("No eligible free products available in the cart.");
        }

        // Apply discount to the "get" items
        for (CartItem item : cart.getItems()) {
            if (getProducts.contains(item.getProductId())) {
                item.setDiscountedPrice(0);  // Free product
            }
        }

        return coupon;
    }

    }


