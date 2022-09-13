---
title: 29_BlockingQueue
date: 2022-08-12
author: yincy
---

### BlockingQueue

**BlockingQueue（阻塞队列）是 Collection 集合接口的一个子接口。当队列为空时，从队列中获取元素会阻塞；当队列满了的时候，从队列中添加元素会阻塞**

---



**应用场景**：

可以利用阻塞队列，省去阻塞和唤醒线程的操作，不用关注这些细节，减少程序的复杂度

---

**核心方法**

```java
/**
 * 阻塞队列：ArrayBlockQueue
 *  1、抛出异常方法：add、remove、element
 */
public class ThreadDemo {
    public static void main(String[] args) throws InterruptedException {
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(3);
        queue.add("a");
        queue.add("b");
        queue.add("c");
        System.out.println("------------------");
        // queue.add("d"); // 当阻塞队列满了的时候，再添加元素时：java.lang.IllegalStateException
        System.out.println(queue.remove());
        System.out.println(queue.remove());
        System.out.println(queue.remove());
        System.out.println("-------------------");
        // queue.remove(); // 当阻塞队列中没有元素的时候，再删除元素时：java.util.NoSuchElementException
        System.out.println(queue.element()); // 获取队列中的头元素，如果队列为空：java.util.NoSuchElementException
    }
}
```

```java
/**
 * 阻塞队列：ArrayBlockQueue
 *  2、判断：offer、poll、peek
 */
public class ThreadDemo {
    public static void main(String[] args) throws InterruptedException {
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(3);
        System.out.println(queue.offer("a"));
        System.out.println(queue.offer("b"));
        System.out.println(queue.offer("c"));
        System.out.println("------------------");
        // System.out.println(queue.offer("d")); // 返回是否添加成功，如果阻塞队列满了，则添加失败，返回false
        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println("-------------------");
        // System.out.println(queue.poll()); // 删除队列中的头元素并返回，如果队列为空，则返回null
        System.out.println(queue.peek()); // 获取队列中的头元素，如果队列为空返回null
    }
}
```

```java
/**
 * 阻塞队列：ArrayBlockQueue
 *  3、阻塞：put、take
 */
public class ThreadDemo {
    public static void main(String[] args) throws InterruptedException {
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(3);

        queue.put("a");
        queue.put("b");
        queue.put("c");
        System.out.println("------------------");
        // queue.put("d"); // 当队列已经满了的话，再添加会阻塞
        new Thread(()->{
            try {
                System.out.println(queue.take());
                System.out.println(queue.take());
                System.out.println(queue.take());
                System.out.println("-------------------");
                System.out.println(queue.take()); // 当队列为空的话，再删除会阻塞
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"A").start();
        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(3);
                queue.put("f");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"B").start();
    }
}
```

```java
/**
 * 阻塞队列：ArrayBlockQueue
 *  4、指定操作时间并判断：put、take
 */
public class ThreadDemo {
    public static void main(String[] args) throws InterruptedException {
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(3);
        System.out.println(queue.offer("a"));
        System.out.println(queue.offer("b"));
        System.out.println(queue.offer("c"));
        new Thread(()->{
            try {
                System.out.println(queue.offer("d", 3, TimeUnit.SECONDS));// 3秒内，如果队列中没有满，添加成功，返回true，否则返回false
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"A").start();
        new Thread(()->{
            System.out.println(queue.poll());
            System.out.println(queue.poll());
            System.out.println(queue.poll());
            System.out.println("-----------------");
            try {
                System.out.println(queue.poll(2,TimeUnit.SECONDS)); // 2秒内，如果队列不为空，则删除头元素并返回
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"B").start();
    }
}
```

---



**常用的阻塞队列**

```java
// 1、由数组结构组成的有界阻塞队列
ArrayBlockingQueue<String> queue1 = new ArrayBlockingQueue<String>(10);// 需要指定阻塞队列容量
// 2、由链表结构组成的有界阻塞队列
LinkedBlockingQueue<String> queue2 = new LinkedBlockingQueue<>(); // 默认使用2的31次方-1的容量
// 3、单个元素的阻塞队列
SynchronousQueue<String> queue3 = new SynchronousQueue<>();
```

