package org.example.coupon.example;

import java.util.concurrent.atomic.AtomicReference;

public class ABAExample {
    static class Node {
        int value;
        Node next;

        Node(int value) {
            this.value = value;
        }
    }

    private final AtomicReference<Node> top = new AtomicReference<>();

    public void push(int value) {
        Node newNode = new Node(value);
        Node oldTop;
        do {
            oldTop = top.get();
            newNode.next = oldTop;
        } while (!top.compareAndSet(oldTop, newNode));
    }

    public int pop() {
        Node oldTop;
        Node newTop;
        do {
            oldTop = top.get();
            if (oldTop == null) {
                throw new RuntimeException("Stack is empty");
            }
            newTop = oldTop.next;
        } while (!top.compareAndSet(oldTop, newTop));
        return oldTop.value;
    }

    public static void main(String[] args) throws InterruptedException {
        ABAExample stack = new ABAExample();
        stack.push(1);
        stack.push(2);
        stack.push(3);

        Thread t1 = new Thread(() -> {
            Node oldTop;
            Node newTop;

            // Step 1: Read the top value
            do {
                oldTop = stack.top.get();
                System.out.println("Thread 1 - Step 1: Current top: " + oldTop.value);

                try {
                    // Simulate context switch and allow ABA problem
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                newTop = oldTop.next;
            } while (!stack.top.compareAndSet(oldTop, newTop));

            System.out.println("Thread 1 popped: " + oldTop.value);
        });

        Thread t2 = new Thread(() -> {
            // Step 2: Pop the top value
            int oldValue = stack.pop();
            System.out.println("Thread 2 popped: " + oldValue);

            // Step 3: Push and pop values to simulate ABA problem
            stack.push(4);
            System.out.println("Thread 2 pushed: 4");
            stack.push(3);
            System.out.println("Thread 2 pushed: 3");

            stack.pop(); // Popping 3
            System.out.println("Thread 2 popped after push: 3");
            stack.pop(); // Popping 4
            System.out.println("Thread 2 popped after push: 4");
        });

        t1.start();
        Thread.sleep(50); // Ensure t1 runs first and is paused in the middle of pop
        t2.start();

        t1.join();
        t2.join();
    }
}
