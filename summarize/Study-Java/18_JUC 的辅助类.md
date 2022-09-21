---
title: 18_JUC 的辅助类
date: 2022-08-10
---

---



### JUC的辅助类

---



##### 1、CountDownLatch

> **放学关门**

```java
/**
 * CountDownLatch 两个方法，当一个或多个线程调用 await 方法时，这些线程会阻塞。
 *      其他线程调用 countDown 方法会将计数器减一（调用 countDown 方法的线程不会阻塞）；
 *      当计数器的值变为 0 的时候，因 await 方法阻塞的线程会被唤醒，继续执行。
 */
public class CountDownLatchDemo {
    public static void main(String[] args) throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(6);
        for (int i = 1; i <= 6; i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName() + "号离开了");
                countDownLatch.countDown();
            },String.valueOf(i)).start();
        }
        countDownLatch.await();
        System.out.println(Thread.currentThread().getName() + " 锁门了");
    }
}
```



---



##### 2、CyclicBarrier

> **集齐七颗龙珠召唤神龙**

---

```java
/**
 * CyclicBarrier
 *      的字面意思是可循环（Cyclic）使用的屏障（Barrier）。它要做的事情是，
 *      让一组线程到达一个屏障（也可以叫同步点）时被阻塞，
 *      直到最后一个线程到达屏障时，屏障才会开门，所有被平展拦截的线程才会继续干活。
 *      线程进入屏障通过 CyclicBarrier 的 await() 方法。
 */
public class CountDownLatchDemo {
    public static void main(String[] args) throws Exception {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7,()->{
            System.out.println("七颗龙珠已经全部集齐，可以召唤神龙了");
        });
        for (int i = 1; i <= 7; i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName() + "被收集了");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            },i + "号龙珠").start();
        }
    }
}
```



##### 3、Semaphore

> **抢停车位**

---

```java
/**
 *  在信号量上我们定义两种操作：
 *      acquire（获取）当一个线程调用 acquire 操作时，它要么通过成功获取信号量（信号量减一），
 *          要么一直等下去，直到有线程释放信号量，或超时。
 *      release（释放）实际上会将信号量的值加一，然后唤醒等待的线程。
 *
 *      信号量主要用于两个目的：一个是用于多个共享资源的互斥使用，另一个用于并发线程数的控制。
 */
public class CountDownLatchDemo {
    public static void main(String[] args) throws Exception {
        Semaphore semaphore = new Semaphore(3);
        for (int i = 1; i <= 5; i++) {
            new Thread(() -> {
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + "抢车位成功");
                    TimeUnit.SECONDS.sleep(new Random().nextInt(3) + 1);
                    System.out.println(Thread.currentThread().getName() + "离开车位");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            }, i + "号车").start();
        }
    }
}
```



