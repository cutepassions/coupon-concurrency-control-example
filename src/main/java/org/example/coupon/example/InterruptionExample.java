package org.example.coupon.example;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;


/**
 *  ReentrantLock 과 Semaphore 를 사용하여 다른 스레드가 권한을 획득할 수 있는지 확인하는 예제
 */
public class InterruptionExample {
    private static final Semaphore semaphore = new Semaphore(1);
    private static int sharedResource = 0;
    private static final ReentrantLock lock = new ReentrantLock(); // 변경된 부분

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                lock.lock();
//                semaphore.acquire();
                System.out.println("Thread 1: Acquired !!");
                sharedResource++;
                // 작업 수행
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
//                semaphore.release();
                System.out.println("Thread 1: Released !!");
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(50);
//                semaphore.release(); // Incorrectly releasing semaphore
                lock.unlock();
                System.out.println("Thread 2: Incorrectly released !!");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Final value of sharedResource: " + sharedResource);
    }
}