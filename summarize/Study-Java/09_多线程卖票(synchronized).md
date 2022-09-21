---
title: 09_多线程卖票(synchronized)
date: 2022-08-09
---





### 多线程买票--synchronized

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
    public int getNumber(){
        return this.number;
    }

    public synchronized void sale(){
            if (number > 0){
                System.out.println(Thread.currentThread().getName() + "卖出第：" + (this.number--) + "张，还剩：" + this.number);
            }
    }
}
```

