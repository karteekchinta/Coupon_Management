package com.Coupons.Management.Coupons.CouponEntity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Data
@Table(name ="coupons")
public class Coupon {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long product_id;
    private String type;
    @ElementCollection
    @CollectionTable(name = "coupon_details", joinColumns = @JoinColumn(name = "coupon_id"))
    @Column(name = "details")
    private List<String> details;

    @Column(name = "repetition_limit")
    private Integer repetitionLimit;

    @ElementCollection
    @CollectionTable(name = "buy_products", joinColumns = @JoinColumn(name = "coupon_id"))
    @Column(name = "product_id")
    private List<Long> buyProducts;

    @ElementCollection
    @CollectionTable(name = "get_products", joinColumns = @JoinColumn(name = "coupon_id"))
    @Column(name = "product_id")
    private List<Long> getProducts;

}
