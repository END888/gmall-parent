---
title: 10_多线程卖票(lock)
date: 2022-08-09

---





### 多线程买票--lock

```java
public class ThreadDemo {
    public static void main(String[] args) {
        Ticket ticket = new Ticket();
        new Thread(() -> {for (int i = 0; i < 50; i++) {ticket.sale();}}, "t1").start();
        new Thread(() -> {for (int i = 0; i < 50; i++) {ticket.sale();}}, "t2").start();
        new Thread(() -> {for (int i = 0; i < 50; i++) {ticket.sale();}}, "t3").start();
    }
}
class Ticket{
    private Integer number = 50;
    Lock lock = new ReentrantLock();
    public int getNumber(){
        return this.number;
    }

    public void sale(){
        lock.lock();
        try {
            if (number > 0){
                System.out.println(Thread.currentThread().getName() + "卖出第：" + (this.number--) + "张，还剩：" + this.number);
            }
        }finally {
            lock.unlock();
        }
    }
}
```

