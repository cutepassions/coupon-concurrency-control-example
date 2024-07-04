package org.example.coupon.coupon;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 *  기존 시나리오 서버 (동시성 제어를 하지 않은)
 * */
@RestController
@RequestMapping("/origin")
public class OriginCoupon implements Coupon{

    private int availableCoupons = 5000;
    private int issuedCoupons = 0;

    // 쿠폰 발급 요청
    @PostMapping("/issue")
    @Override
    public ResponseEntity<?> issueCoupon() {

        if (availableCoupons > 0) {
            availableCoupons--;
            issuedCoupons++;
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // 남은 쿠폰 수 조회
    @GetMapping("/available")
    @Override
    public ResponseEntity<Integer> getAvailableCoupons() {
        return ResponseEntity.ok(availableCoupons);
    }

    // 발급된 쿠폰 수 조회
    @GetMapping("/issued")
    @Override
    public ResponseEntity<Integer> getIssuedCoupons() {
        return ResponseEntity.ok(issuedCoupons);
    }
}
