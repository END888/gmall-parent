---
title: 28_ReentrantReadWriteLock
date: 2022-08-12
author: yincy
---

### ReentrantReadWriteLock

```java
/**
 * 题目：10个线程读，10个线程写，操作同一个资源
 *
 * 1、不加锁：不可以，写的时候原子性被破坏
 * 2、加 ReentrantLock锁(或 Synchronized)：写控制了，但是没有并发度，并发性能不好
 * 3、加读写锁：规范写入，写唯一，读并发
 * 
 * 当多个线程需要进行读取一个资源时，所产生的读写问题：
 * 1、无锁状态：一个线程在执行写操作的时候，其他线程就来写，其他线程还能读
 * 2、只对写操作加锁：写操作还没执行完成，就有其他线程来读
 * 3、写操作和读操作都加锁：读操作变成单线程了，读操作无法共享数据
 * 4、使用读写锁（ReentrantReadWriteLock）: 写操作加写锁；读操作加读锁
 *
 * 读写锁：
 *  对于同一个资源，我们涉及多线程的操作，有读、有写、交替进行。
 *  为了保证读写的数据一致性
 *
 *  读读可共享
 *  读写不共享
 *  写写不共享
 *
 *  读的时候希望高并发同时进行，可以共享，可以多个线程同时操作进行中。。。
 *  写的时候为了保证数据一致性，需要独占它
 */
public class ThreadDemo {
    public static void main(String[] args) throws InterruptedException {
        MyCache myCache = new MyCache();
        ExecutorService pool = Executors.newFixedThreadPool(20);
        try {
            for (int i = 0; i < 10; i++) {
                int finalI = i;
                pool.execute(() -> {
                    myCache.write(finalI + "", finalI + "");
                });
            }
            for (int i = 0; i < 10; i++) {
                int finalI = i;
                pool.execute(() -> {
                    myCache.read(finalI + "");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();
        }
    }
}

// 资源类
class MyCache {
    volatile Map<String, String> map = new HashMap<>();
    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public void write(String key, String value) {
        lock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " 开始写...");
            map.put(key, value);
            System.out.println(Thread.currentThread().getName() + " 写完成...");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.writeLock().unlock();
        }
    }

    public void read(String key) {
        lock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " 开始读--------");
            String value = map.get(key);
            System.out.println(Thread.currentThread().getName() + " 读完成--------> :" + value);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.readLock().unlock();
        }
    }
}

```

