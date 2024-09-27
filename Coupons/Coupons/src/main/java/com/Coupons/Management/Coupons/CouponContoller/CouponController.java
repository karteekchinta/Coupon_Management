package com.Coupons.Management.Coupons.CouponContoller;


import com.Coupons.Management.Coupons.CouponEntity.Cart;
import com.Coupons.Management.Coupons.CouponEntity.Coupon;
import com.Coupons.Management.Coupons.CouponService.CouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/coupons")
public class CouponController {
    private CouponService couponService;
    @PostMapping()
    public ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon) {
        Coupon createdCoupon = couponService.createCoupon(coupon);
        return ResponseEntity.ok(createdCoupon);
    }

    @GetMapping
    public ResponseEntity<List<Coupon>> getAllCoupons() {
        List<Coupon> coupons = couponService.getAllCoupons();
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Coupon> getCouponById(@PathVariable Long id) {
        Optional<Coupon> coupon = couponService.getCouponById(id);
        return coupon.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Coupon> updateCoupon(@PathVariable Long id, @RequestBody Coupon couponDetails) {
        Coupon updatedCoupon = couponService.updateCoupon(id, couponDetails);
        return ResponseEntity.ok(updatedCoupon);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/applicable-coupons")
    public ResponseEntity<List<Coupon>> getApplicableCoupons() {
        // Placeholder for cart-related logic to get applicable coupons
        List<Coupon> applicableCoupons = couponService.getApplicableCoupons();
        return ResponseEntity.ok(applicableCoupons);
    }

    @PostMapping("/apply-coupon/{id}")
    public ResponseEntity<Coupon> applyCoupon(@PathVariable Long id) {
        Coupon appliedCoupon = couponService.applyCoupon(id);
        return ResponseEntity.ok(appliedCoupon);
    }
    @PostMapping("/apply-coupon/cart-wise")
    public ResponseEntity<Cart> applyCartWiseCoupon(@RequestParam Long couponId, @RequestBody Cart cart) {
        Coupon coupon = couponService.getCouponById(couponId).orElseThrow(() -> new RuntimeException("Coupon not found."));
        couponService.applyCartWiseCoupon(coupon, cart);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/apply-coupon/product-wise")
    public ResponseEntity<Cart> applyProductWiseCoupon(@RequestParam Long couponId, @RequestBody Cart cart) {
        Coupon coupon = couponService.getCouponById(couponId).orElseThrow(() -> new RuntimeException("Coupon not found."));
        couponService.applyProductWiseCoupon(coupon, cart);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/apply-coupon/bxgy")
    public ResponseEntity<Cart> applyBxGyCoupon(@RequestParam Long couponId, @RequestBody Cart cart) {
        Coupon coupon = couponService.getCouponById(couponId).orElseThrow(() -> new RuntimeException("Coupon not found."));
        couponService.applyBxGyCoupon(coupon, cart);
        return ResponseEntity.ok(cart);
    }

}
