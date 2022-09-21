---
title: 24_volatile
date: 2022-08-11
author: yincy
---

### volatile

**内存语义：volatile 的写是直接刷新到主内存中；读是直接从主内存中读取**

**作用：保证 volatile 变量的可见性和有序性**

---

##### 1、内存屏障是什么？

是一种屏障指令，它使得 CPU 或编译器对屏障指令的前和后所发出的内存操作执行一个排序的约束，也叫内存栅栏或栅栏指令

---



##### 2、内存屏障能干嘛

- 阻止屏障两边的指令重排序
- 写数据时加入屏障，强制将线程私有工作内存的数据刷回主物理内存
- 读数据时加入屏障，线程私有工作内存的数据失效，重新到主物理内存中获取最新数据

---



##### 3、内存屏障四大指令

```java
public native void loadFence();
public native void storeFence();
public native void fullFence();
```

**1、在每一个 volatile 写操作前插入一个 StoreStore 屏障**

​	禁止重排序：一定是 Store1 的数据写出到主内存完成后，才能让 Store2 及其之后的写出操作的数据，被其他线程看到。保证 Store1 指令写出去的数据，会强制被刷新回到主内存中。



**2、在每一个 volatile 写操作后面插入一个 StoreStore 屏障**

​	禁止重排序：一定是 Store1 的数据写出到主内存完成后，才能让 Load2 来读取数据；

​	同时保证：强制把写缓冲区的数据刷回到主内存中，让工作内存/CPU 高速缓存当中的缓存的数据失效，重新到主内存中获取新的数据



**3、在每一个 volatile 读操作后面插入一个 LoadLoad 屏障**

​	禁止重排序：访问 Load2 的读取操作一定不会重排到 Load1 之前，保证 Load2 在读取的时候，自己缓存内的相应数据失效，Load2 会去主内存中获取最新的数据



**4、在每一个 volatile 读操作后面插入一个 LoadStore 屏障**

​	禁止重排序：一定是 Load1 读取数据完成后，才能让 Store2 及其之后的写出操作的数据，被其他线程看到

---



##### 4、volatile 关键字在系统底层是如何加入内存屏障的？

​	通过反编译后的字节码可以看出来，我们使用 volatile 修饰的变量会在 Class 中的 Field 的flags 添加一个 ACC_VOLATILE。

​	JVM 在把字节码生成机器码的时候，发现操作是 volatile 变量的话，就会根据 JMM 要求，在相应的位置去插入内存屏障指令。

---



##### 5、volatile 的可见性

1、对一个 volatile 修饰的变量进行读操作的话，总是能读取到这个变量的最新的值，也就是这个变量最后被修改的值

2、一个线程修改了 volatile 修饰的变量的值的时候，那么这个变量的新的值，会立即刷新回到主内存中

3、一个线程去读取 volatile 修饰的变量的值的时候，该变量在工作内存中的数据无效，需要重新到主内存中读取最新的数据

---



##### 6、volatile 禁止指令重排

**写指令**：

1、StoreSore 屏障，禁止上面的普通写和下面的 volatile 写操作重排序，前面所有的普通写的操作，数据都已经刷新到主内存；普通写和 volatile 写禁止重排；volatile 写和 volatile 写禁止重排。

2、StoreLoad 屏障，禁止上面的 volatile 写和下面的 volatile 读/写或普通写操作重排序，前面 volatile 写的操作，数据都已经刷新到主内存，volatile 写和普通写禁止重排；volatile 写和 volatile 读/写禁止重排。

**读指令**：

1、LoadLoad 屏障，禁止下面的普通读、volatile 读和上面的volatile 读重排序，volatile 读和普通读禁止重排；volatile 读和 volatile 读禁止重排

2、LoadStore 屏障，禁止上面的 volatile 读和下面的 volatile 写或普通写重排序，volatile 读和普通写禁止重排；volatile 读和 volatile 写禁止重排 

---



##### 7、对比 java.util.concurrent.locks.Lock 来理解

CPU 执行机器码指令的时候，是使用 lock前缀指令来实现 volatile 的功能的。

Lock 指令，相当于内存屏障，功能也类似内存屏障的功能：

1、首先对总线/缓存加锁，然后去执行后面的指令，最后，释放锁，同时把高速缓存的数据刷新回到主内存；

2、在 lock 锁住总线/缓存的时候，其他 cpu 的读写请求就会被阻塞，直到锁释放。Lock 过后的写操作，会让其他 cpu 的高速缓存中响应的数据失效，这样后续这些 cpu 在读取数据的时候，就会从主内存去加载最新的数据

加了 Lock 指令过后的具体表现，就跟 JMM 添加内存屏障后一样。

---



##### 8、一句话总结

1、volatile 写之前的操作，都禁止重排序到 volatile 之后

2、volatile 读之后的操作，都禁止重排序到 volatile 之前

3、volatile 写之后 volatile 读，禁止重排序

---

