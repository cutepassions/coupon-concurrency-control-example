package org.example.coupon.coupon;

import org.springframework.http.ResponseEntity;


/**
 *  쿠폰 인터페이스
 * */
public interface Coupon {

    // 쿠폰 발급 요청
    ResponseEntity<?> issueCoupon();
    // 남은 쿠폰 수 조회
    ResponseEntity<Integer> getAvailableCoupons();
    // 발급된 쿠폰 수 조회
    ResponseEntity<Integer> getIssuedCoupons();
}
