---
title: 面试题3_JUC
date: 2022-08-16
author: yincy
---

### 面试题3_JUC

#### 1、线程的状态转换有什么？（生命周期）

![1661838633351](C:\Users\ycy\Desktop\website-hexo\source\_posts\assets\1661838633351.png)





![1661838700354](C:\Users\ycy\Desktop\website-hexo\source\_posts\assets\1661838700354.png)

#### 2、什么情况下会产生死锁？

**产生死锁的原因**
（1） 因为系统资源不足。
（2） 进程/线程运行推进的顺序不合适。
（3） 资源分配不当等。如果系统资源充足，进程的资源请求都能够得到满足，死锁出现的可能性就很低，否则就会因争夺有限的资源而陷入死锁。其次，进程运行推进顺序与速度不同，也可能产生死锁。
**产生死锁的四个必要条件：**

（1） 互斥条件：一个资源每次只能被一个进程使用。
（2） 请求与保持条件：一个进程因请求资源而阻塞时，对已获得的资源保持不放。
（3） 不剥夺条件:进程已获得的资源，在末使用完之前，不能强行剥夺。
（4） 循环等待条件:若干进程之间形成一种头尾相接的循环等待资源关系。
	这四个条件是死锁的必要条件，只要系统发生死锁，这些条件必然成立，而只要上述条件之一不满足，就不会发生死锁。
**死锁的解除与预防：**
	理解了死锁的原因，尤其是产生死锁的四个必要条件，就可以最大可能地避免、预防和
解除死锁。所以，在系统设计、进程调度等方面注意如何不让这四个必要条件成立，如何确
定资源的合理分配算法，避免进程永久占据系统资源。此外，也要防止进程在处于等待状态
的情况下占用资源。因此，对资源的分配要给予合理的规划。

---

#### 3、进程、线程、、管程

**1、进程**：指的是正在运行的应用程序，是系统进行资源分配和调度的基本单位

**2、线程**：用来执行进程中的每个任务的单位，是程序运行的最小单元

**3、管程**：监视器（Monitor），也就是锁

---

#### 4、并行、并发

**1、并行**：多个线程同时执行操作多个资源

**2、并发**：多个线程同时操作一个资源

---

#### 5、同步、异步

> 同步和异步是指访问数据的机制

**1、同步**：主动请求数据并等待IO操作完成之后才能继续处理其他的任务

**2、异步**：主动请求数据后可以继续处理其他任务，之后会收到IO操作完成的通知

---

#### 6、synchronized 和 Lock 的区别

1. synchronized 是 Java 内置的关键字；Lock 是 java.util.concurrent 包下的一个接口;
2. synchronized 创建的锁是非公平锁；Lock 默认创建的锁是非公平锁，但是可以根据需求创建非公平锁;
3. synchronized 它的上锁和解锁是自动的（执行完同步代码块会自动释放、出现异常会自动释放）；Lock 是需要程序员手动上锁和手动释放锁（在finally中手动释放）
4. synchronized 无法判断是否获取锁的状态；Lock 可以根据 tryLock() 判断是否获取锁的状态
5. synchronized 是不可中断锁，Lock 是可中断锁
6. synchronized 适用于少量的同步代码问题，Lock 适用于大量的同步代码问题

---

#### 7、唤醒线程的方式

1、使用 Object 中的 wait() 方法让线程等待，使用 Object 中的 notify() 方法让线程唤醒

2、使用 JUC 包中的 Condition 的 await() 方法让线程等待，使用 signal() 方法让线程唤醒

3、LockSupport 类可以阻塞当前线程以及唤醒指定被阻塞的线程

---



**总结**

1、Object 和 Condition 使用的限制条件：

> 线程要先获得并持有锁，必须在 synchronized 或 lock 中
>
> 必须要先等待后唤醒，线程才能被唤醒

2、LockSupport：

> 可以不用声明锁
>
> park、uppark 的顺序没有严格要求



---

#### 8、为什么要使用线程池？

1、线程也是系统资源的一种，频繁地创建和销毁线程会消耗系统资源，可以通过线程池对线程进行复用

2、创建线程的这个操作比较耗时，使用线程池可以提高响应速度，当任务到达时，可以不需要等待线程创建而直接执行

3、为了提高系统的稳定性，对线程进行统一的管理

---

#### 9、什么是线程池，线程池有哪些？

线程池就是事先将多个线程对象放到一个容器中，当使用的时候就不用 new 线程而是直接去池中拿线程即可，节省了开辟子线程的时间，提高的代码执行效率

在 JDK 的 java.util.concurrent.Executors 中提供了生成多种线程池的静态方法。

ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(4);

ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(4);

ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();

然后调用他们的 execute 方法即可。



**1、newCachedThreadPool**

创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。这种类型的线程池特点是：

工作线程的创建数量几乎没有限制(其实也有限制的,数目为Interger. MAX_VALUE), 这样可灵活的往线程池中添加线程。

如果长时间没有往线程池中提交任务，即如果工作线程空闲了指定的时间(默认为1分钟)，则该工作线程将自动终止。终止后，如果你又提交了新的任务，则线程池重新创建一个工作线程。

在使用CachedThreadPool时，一定要注意控制任务的数量，否则，由于大量线程同时运行，很有会造成系统瘫痪。

**2、newFixedThreadPool**

创建一个指定工作线程数量的线程池。每当提交一个任务就创建一个工作线程，如果工作线程数量达到线程池初始的最大数，则将提交的任务存入到池队列中。FixedThreadPool是一个典型且优秀的线程池，它具有线程池提高程序效率和节省创建线程时所耗的开销的优点。但是，在线程池空闲时，即线程池中没有可运行任务时，它不会释放工作线程，还会占用一定的系统资源。

**3、newSingleThreadExecutor**

创建一个单线程化的Executor，即只创建唯一的工作者线程来执行任务，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。如果这个线程异常结束，会有另一个取代它，保证顺序执行。单工作线程最大的特点是可保证顺序地执行各个任务，并且在任意给定的时间不会有多个线程是活动的。

**4、newScheduleThreadPool**

创建一个定长的线程池，而且支持定时的以及周期性的任务执行。例如延迟3秒执行。

这4种线程池底层 全部是ThreadPoolExecutor对象的实现，阿里规范手册中规定线程池采用ThreadPoolExecutor自定义的，实际开发也是。

---

#### 10、线程池的核心参数？



```java
ThreadPoolExecutor pool = new ThreadPoolExecutor(3, // 1、常驻核心线程数
                                                 5, // 2、最大线程数
                                                 10, // 3、保持活跃时间
                                                 TimeUnit.MINUTES, // 4、时间单位
                                                 new LinkedBlockingQueue<>(10), // 5、阻塞队列
                                                 Executors.defaultThreadFactory(), // 6、线程工厂
                                                 new ThreadPoolExecutor.AbortPolicy()); // 7、拒绝策略
```

---

#### 11、常见线程安全的并发容器有哪些？

CopyOnWriteArrayList、CopyOnWriteArraySet、ConcurrentHashMap

CopyOnWriteArrayList、CopyOnWriteArraySet采用写时复制实现线程安全

ConcurrentHashMap采用分段锁的方式实现线程安全

---

#### 12、原子类的原理？

AtomicInteger 类利用 CAS (Compare and Swap) + volatile + native 方法来保证原子操作，从而避免 synchronized 的高开销，执行效率大为提升。

CAS 的原理，是拿期望值和原本的值作比较，如果相同，则更新成新的值。UnSafe 类的 objectFieldOffset() 方法是个本地方法，这个方法是用来拿“原值”的内存地址，返回值是 valueOffset；另外，value 是一个 volatile 变量，因此 JVM 总是可以保证任意时刻的任何线程总能拿到该变量的最新值。

---

#### 13、synchronized底层实现是什么？lock底层是什么？有什么区别？

**Synchronized**原理：

方法级的同步是隐式，即无需通过字节码指令来控制的，它实现在方法调用和返回操作之中。JVM可以从方法常量池中的方法表结构(method_info Structure) 中的 ACC_SYNCHRONIZED 访问标志区分一个方法是否同步方法。当方法调用时，调用指令将会 检查方法的 ACC_SYNCHRONIZED 访问标志是否被设置，如果设置了，执行线程将先持有monitor（虚拟机规范中用的是管程一词）， 然后再执行方法，最后再方法完成(无论是正常完成还是非正常完成)时释放monitor。

代码块的同步是利用monitorenter和monitorexit这两个字节码指令。它们分别位于同步代码块的开始和结束位置。当jvm执行到monitorenter指令时，当前线程试图获取monitor对象的所有权，如果未加锁或者已经被当前线程所持有，就把锁的计数器+1；当执行monitorexit指令时，锁计数器-1；当锁计数器为0时，该锁就被释放了。如果获取monitor对象失败，该线程则会进入阻塞状态，直到其他线程释放锁。

**Lock** 原理：

·  Lock的存储结构：一个int类型状态值（用于锁的状态变更），一个双向链表（用于存储等待中的线程） 

·  Lock获取锁的过程：本质上是通过CAS来获取状态值修改，如果当场没获取到，会将该线程放在线程等待链表中。 

·  Lock释放锁的过程：修改状态值，调整等待链表。 

·  Lock大量使用CAS+自旋。因此根据CAS特性，lock建议使用在低锁冲突的情况下。

**Lock与synchronized的区别**：

1. Lock的加锁和解锁都是由java代码配合native方法（调用操作系统的相关方法）实现的，而synchronize的加锁和解锁的过程是由JVM管理的

2. 当一个线程使用synchronize获取锁时，若锁被其他线程占用着，那么当前只能被阻塞，直到成功获取锁。而Lock则提供超时锁和可中断等更加灵活的方式，在未能获取锁的     条件下提供一种退出的机制。

3. 一个锁内部可以有多个Condition实例，即有多路条件队列，而synchronize只有一路条件队列；同样Condition也提供灵活的阻塞方式，在未获得通知之前可以通过中断线程以    及设置等待时限等方式退出条件队列。

4. synchronize对线程的同步仅提供独占模式，而Lock即可以提供独占模式，也可以提供共享模式

---

#### 14、sleep 和 wait 的区别

（1）sleep 是 Thread 类中的一个静态方法，wait 是Object 中的一个成员方法；

（2）sleep 在使用时，必须要指定时间，而 wait 如果不指定时间，则需要使用 notify/notifyAll 来唤醒；

（3）sleep 在任何地方都能睡，而 wait 必须在 synchronized 内才能等；

（4）sleep 不会释放锁，而 wait 会自动释放锁



---

#### 15、synchronized 与 lock 的区别

1、首先synchronized是java内置关键字；Lock是个  java.util.concurrent 包中的一个接口；

2、synchronized会自动释放锁；Lock需在finally中手工释放锁（unlock()方法释放锁）；

3、synchronized的锁可重入、不可中断、非公平，而Lock锁可重入、可判断、可公平（两者皆可）；

4、锁是一个对象，并且锁的信息保存在了对象中；Lock 是通过 int 类型的 state 标识；

5、synchronized 有锁升级；Lock 没有锁升级；

6、synchronized无法判断是否获取锁的状态，Lock可以判断是否获取到锁；

7、用synchronized关键字的两个线程1和线程2，如果当前线程1获得锁，线程2线程等待。如果线程1阻塞，线程2则会一直等待下去，而Lock锁就不一定会等待下去，如果尝试获取不到锁，线程可以不用一直等待就结束了；

8、Lock锁适合大量同步的代码的同步问题，synchronized锁适合代码少量的同步问题。





---

#### 16、线程池底层工作原理

1、在创建了线程池后，开始等待请求。

2、当调用 execute() 方法添加一个请求任务时，线程池会做出如下判断：

- 如果正在运行的线程数量小于 corePoolSize，那么马上创建线程运行这个任务；
- 如果正在运行的线程数量大于或等于 corePoolSize，那么将这个任务放入队列；
- 如果这个时候队列满了且正在运行的线程数量还小于 maxnumPoolSize，那么还是要创建非核心线程立刻运行这个任务；
- 如果队列满了且正在运行的线程数量大于或等于 maxnumPoolSize，那么线程池会启动饱和拒绝策略来执行。

3、当一个线程完成任务时，它会从队列中取下一个任务来执行。

4、当一个线程无事可做超过一定的时间（keepAliveTime）时，线程会判断：

- 如果当前运行的线程数大于 corePoolSize，那么这个线程就被停掉。
- 所以线程池的所有任务完成后，它最终会收缩到 corePoolSize 的大小。



---

#### 17、拒绝策略

1、AbortPolicy（默认的拒绝策略）：

当请求任务数大于最大线程数+阻塞队列的时候，会：java.util.concurrent.RejectedExecutionException

2、CallerRunsPolicy:

当请求任务数大于最大线程数+阻塞队列的时候，超出的任务会返回给调用者让调用者执行

3、DiscardOldestPolicy：

当请求任务数大于最大线程数+阻塞队列的时候，会丢弃阻塞队列中等待最久的任务

4、DiscardPolicy:

当请求任务数大于最大线程数+阻塞队列的时候，会丢弃新来的任务

---

#### 18、大小如何设置？



1. 需要分析线程池执行的任务的特性： CPU 密集型还是 IO 密集型

2. 每个任务执行的平均时长大概是多少，这个任务的执行时长可能还跟任务处理逻辑是否涉及到网络传输以及底层系统资源依赖有关系

如果是 CPU 密集型，主要是执行计算任务，响应时间很快，cpu 一直在运行，这种任务 cpu的利用率很高，那么线程数的配置应该根据 CPU 核心数来决定，CPU 核心数=最大同时执行线程数，加入 CPU 核心数为 4，那么服务器最多能同时执行 4 个线程。过多的线程会导致上下文切换反而使得效率降低。那线程池的最大线程数可以配置为 cpu 核心数+1 如果是 IO 密集型，主要是进行 IO 操作，执行 IO 操作的时间较长，这是 cpu 出于空闲状态，导致 cpu 的利用率不高，这种情况下可以增加线程池的大小。这种情况下可以结合线程的等待时长来做判断，等待时间越高，那么线程数也相对越多。一般可以配置 cpu 核心数的 2 倍。

一个公式：线程池设定最佳线程数目 = （（线程池设定的线程等待时间+线程 CPU 时间）/ 线程 CPU 时间 ）* CPU 数目

这个公式的线程 cpu 时间是预估的程序单个线程在 cpu 上运行的时间（通常使用 loadrunner测试大量运行次数求出平均值）

---



#### 19、ThreadLocal 的原理

ThreadLocal类用于创建一个线程本地变量

在Thread中有一个成员变量ThreadLocals，该变量的类型是ThreadLocalMap,也就是一个Map，它的键是threadLocal，值就是变量的副本，ThreadLocal为每一个使用该变量的线程都提供了一个变量值的副本，每一个线程都可以独立地改变自己的副本，是线程隔离的。通过ThreadLocal的get()方法可以获取该线程变量的本地副本，在get方法之前要先set,否则就要重写initialValue()方法。
ThreadLocal不是用来解决对象共享访问问题的，而主要是提供了保持对象的方法和避免参数传递的方便的对象访问方式。一般情况下，通过ThreadLocal.set() 到线程中的对象是该线程自己使用的对象，其他线程是不需要访问的，也访问不到的。各个线程中访问的是不同的对象。

---

#### 20、ConcurrentHashMap 底层实现原理的理解

​	ConcurrentHashMap （1.7）：它的底层是使用数组加链表来实现的，然后使用了一种分段锁来保证线程安全，它是将数组分成了 16 段，也就是给每个 Segment 来配一把锁，然后再读每个 Segment 的时候就要先获取对应的锁，所以呢它是最多能有 16 个线程并发去操作。

​	ConcurrentHashMap  （1.8）：它跟 HashMap 一样也引入了这种红黑树的这个数据结构，同时在并发处理方面，不再使用分段锁的方式，而是采用 CAS 加 synchronized 关键字的这样方式来实现一种更加细粒度的锁，相当于是把这个锁的控制，控制在了这种更加细粒度的哈希桶的这个级别，然后在写入键值对的时候，这个可以锁住哈希桶的这种链表的这个头节点，就不会影响到其他的哈希桶的写入，从而去提高对并发的这种处理能力。

------

#### 21、ConcurrentHashMap 可以使用 ReentrantLock 作为锁嘛

​	理论上来件的话应该是可以的，但是我认为这个 synchronized 关键字会更好一点吧，因为在 1.6 之后对synchronized 关键字也进行了一些优化，它里面引入了偏向锁、轻量级锁、重量级锁，那么这些在 ReentrantLock 中是没有的，并且随着 JDK 版本的这个升级，这个 synchronized 也在进一步的进行优化，因为这个 ReentrantLock 是使用 Java 代码来实现的，所以在之后的话也很难有特别大的一种提升空间，所以的话，让我选的话，我会优先选择 synchronized，然后再考虑 ReentrantLock。

------

#### 22、synchronized 关键字对于锁的优化的介绍

​	synchronized 它默认是采用的偏向锁，如果在程序运行中始终是只有一个线程去获取synchronized 锁，那么作为锁对象，它会记录一个线程的 ID，然后在下次获取这个 synchronized 的锁的时候，只需要去比较锁对象的线程 ID 就行了，在运行的过程中如果出现第二个线程去获取synchronized 锁的时候，这里就分两种情况：在没有发生并发竞争锁的情况下，这个 synchronized 就会自动升级为轻量级锁，这个时候第二个线程就会尝试以自旋锁的方式来获取锁，因为很快就能拿到锁，所以第二个线程也不会阻塞，但是如果出现了这种两个线程竞争锁的情况的话，这个 synchronized 它会升级为重量级锁，那么这个时候就是只会有一个线程能够获取到锁，那么另外一个线程它会阻塞，然后要等待第一个线程释放锁之后，才能去拿到锁



---



#### 23、HashMap 是线程安全的嘛

​	HashMap 在设计的时候是针对于单线程环境下来设计的，所以在多线程环境下，它不是线程安全的。

------

#### 24、多线程并发环境下，如果需要一个 Hash 结构，你如何实现？

​	如果在多线程并发环境下，我们可以使用 ConcurrentHashMap 来实现这样的一个需求

