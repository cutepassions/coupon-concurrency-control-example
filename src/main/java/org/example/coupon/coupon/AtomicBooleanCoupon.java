package org.example.coupon.coupon;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;


/**
 *  AtomicBoolean 을 사용한 쿠폰 발급 서버
 * */
@RestController
@RequestMapping("/atomic")
public class AtomicBooleanCoupon implements Coupon{

    private int availableCoupons = 5000;
    private int issuedCoupons = 0;

    private final AtomicBoolean locked = new AtomicBoolean(false);


    // 쿠폰 발급 요청
    @PostMapping("/issue")
    @Override
    public ResponseEntity<?> issueCoupon() {
        try {
            if (locked.compareAndSet(false, true)) {
                if (availableCoupons > 0) {
                    availableCoupons--;
                    issuedCoupons++;
                    return ResponseEntity.ok().build();
                } else {
                    return ResponseEntity.badRequest().build();
                }
            } else {
                return ResponseEntity.status(HttpStatus.LOCKED).build();
            }
        } finally {
            if (locked.get()) locked.set(false);
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
