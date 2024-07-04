package org.example.coupon;

import org.example.coupon.coupon.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 쿠폰 발급 서버 테스트
 * */

@SpringBootTest
class CouponApplicationTests {


    // 기존 쿠폰 발급 서버 테스트
    @Test
    void testOriginCouponIssue() throws InterruptedException {
        testCouponIssue(new OriginCoupon());
    }

    // Synchronized 를 사용한 쿠폰 발급 서버 테스트
    @Test
    void testSynchronizedCouponIssue() throws InterruptedException {
        testCouponIssue(new SynchronizedCoupon());
    }

    // ReentrantLock 을 사용한 쿠폰 발급 서버 테스트
    @Test
    void testReentrantLockCouponIssue() throws InterruptedException {
        testCouponIssue(new ReentrantLockCoupon());
    }

    // Semaphore 를 사용한 쿠폰 발급 서버 테스트
    @Test
    void testSemaphoreCouponIssue() throws InterruptedException {
        testCouponIssue(new SemaphoreCoupon());
    }

    // AtomicBoolean 를 사용한 쿠폰 발급 서버 테스트
    @Test
    void testAtomicBooleanCouponIssue() throws InterruptedException {
        testCouponIssue(new AtomicBooleanCoupon());
    }


    private void testCouponIssue(Coupon coupon) throws InterruptedException {
        ExecutorService thread = Executors.newFixedThreadPool(50); // 50개 스레드 사용
        final int numberOfAttempts = 30000; // 30000번의 쿠폰 발급 시도

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(numberOfAttempts);

        for (int i = 0; i < numberOfAttempts; i++) {
            thread.submit(() -> {
                try{
                    startLatch.await();
                    coupon.issueCoupon();
                }  catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        endLatch.await();
        thread.shutdown();

        if (thread.awaitTermination(1, TimeUnit.MINUTES)){
            System.out.println("현재 컨트롤러 : " + coupon.getClass().getSimpleName());
            System.out.println("사용 가능한 쿠폰 수 " + coupon.getAvailableCoupons());
            System.out.println("발급된 쿠폰 수 " + coupon.getIssuedCoupons() + "\n");
        }

    }
}