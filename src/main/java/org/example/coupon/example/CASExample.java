package org.example.coupon.example;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 여러 스레드가 하나의 값에 동시에 접근하는 예제
 */
public class CASExample {
    private static final AtomicInteger atomicInt = new AtomicInteger(0);

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            int oldValue = atomicInt.get();
            System.out.println("Thread 1 - Initial Value: " + oldValue);

            boolean updated = atomicInt.compareAndSet(oldValue, oldValue + 1);
            System.out.println("Thread 1 - CAS Success: " + updated + ", New Value: " + atomicInt.get());
        });

        Thread t2 = new Thread(() -> {
            int oldValue = atomicInt.get();
            System.out.println("Thread 2 - Initial Value: " + oldValue);

            boolean updated = atomicInt.compareAndSet(oldValue, oldValue + 1);
            System.out.println("Thread 2 - CAS Success: " + updated + ", New Value: " + atomicInt.get());
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Final Value: " + atomicInt.get());
    }
}
