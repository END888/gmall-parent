---
title: 12_线程间通信(synchronized)
date: 2022-08-09
---

---

### 12_线程间通信(synchronized)

```java
// 资源类
class AirCondition{
    private int number = 0;
    // +1 操作
    public synchronized void increment() throws InterruptedException {
        // 判断
        while (number != 0){
            this.wait();
        }
        // 干活
        System.out.println(Thread.currentThread().getName() + "加一，当前number：" + (++number));
        // 唤醒
        this.notifyAll();
    }
    // -1 操作
    public synchronized void decrement() throws InterruptedException {
        // 判断
        while (number == 0){
            this.wait();
        }
        // 干活
        System.out.println(Thread.currentThread().getName() + "减一，当前number：" + (--number));
        // 唤醒
        this.notifyAll();
    }
}
public class ThreadDemo {
    public static void main(String[] args) {
        AirCondition condition = new AirCondition();
        // 生产者线程1
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    condition.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"生产者1").start();
        // 消费者线程1
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    condition.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"消费者1").start();

        // 生产者线程2
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    condition.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"生产者2").start();
        // 消费者线程2
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    condition.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"消费者2").start();
    }
}
```

