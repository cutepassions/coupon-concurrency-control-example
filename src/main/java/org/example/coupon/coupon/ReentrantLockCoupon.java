package org.example.coupon.coupon;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.locks.ReentrantLock;

/**
 *  ReentrantLock  을 사용한 쿠폰 발급 서버
 * */
@RestController
@RequestMapping("/reentrantlock")
public class ReentrantLockCoupon  implements Coupon{

    private int availableCoupons = 5000;
    private int issuedCoupons = 0;

    private final ReentrantLock lock = new ReentrantLock();

    // 쿠폰 발급 요청
    @PostMapping("/issue")
    @Override
    public ResponseEntity<?> issueCoupon() {
        lock.lock();
        try {
            if (availableCoupons > 0) {
                availableCoupons--;
                issuedCoupons++;
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } finally {
            lock.unlock();
        }
    }

    // 남은 쿠폰 수 조회
    @GetMapping("/available")
    @Override
    public synchronized ResponseEntity<Integer> getAvailableCoupons() {
        return ResponseEntity.ok(availableCoupons);
    }

    // 발급된 쿠폰 수 조회
    @GetMapping("/issued")
    @Override
    public synchronized ResponseEntity<Integer> getIssuedCoupons() {
        return ResponseEntity.ok(issuedCoupons);
    }
}
