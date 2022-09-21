---
title: 32_JVM
date: 2022-08-13
author: yincy
---

### JVM

##### 1、请谈谈你对 JVM 的理解？Java 8 的虚拟机有什么更新？

​	JVM 就是 Java 虚拟机，是运行在操作系统之上的，它与硬件没有直接关系；通过 JVM 我们可以运行不同平台和系统的符合 Java 虚拟机的二进制字节码文件，从而实现 Java 的跨平台性。



---



##### 2、什么是 OOM？什么是 StackOverflowError？有哪些方法分析？





---



##### 3、JVM 的常用参数调优你知道哪些？



---



##### 4、谈谈 JVM 中，对类加载器你的认识？

​	类加载器负责加载 class 文件字节码内容加载到内存中，并将这些内容转换为方法区中的运行时数据结构；类加载器只负责类的加载，至于程序能否正常运行，则由执行引擎决定。

---

**类加载**：就是将 class 字节码文件实例化为 Class 对象并进行相关初始化的过程

---



**类加载器的分类**：

启动类加载器（Bootstrap）：C++、无派生类、用于加载 Java 的核心类库；

扩展类加载器（Extension）：Java、派生自 Classloader、父加载器为启动类加载器；

应用程序类加载器（AppClassLoader）：Java、派生自 ClassLoader、父加载器为扩展类加载器；

自定义类加载器：

- 隔离加载类
- 修改加载方式
- 扩展加载源
- 防止源码泄漏

---





##### 5、新生区为啥需要幸存者区（Survivor）？

---

不就是新生代到老年代么，直接Eden到Old不好了吗？为啥要这么复杂？想想如果没有Survivor区，Eden区每进行一次MinorGC存活的对象就会被送到老年代，老年代很快就会被填满。而有很多对象虽然一次MinorGC没有消灭但其实也并不会蹦跶多久，或许第2次第3次就需要被清除。这时候移入老年区，很明显不是一个明智的决定。所以Survivor的存在意义就是减少被送到老年代的对象，进而减少FullGC的发生。Survivor的预筛选保证只有经历15次MinorGC还能在新生代中存活的对象，才会被送到老年代。



##### 6、新生区为啥需要两个 Survivor 区，为什么 8:1:1?

**设置两个Survivor区最大的好处就是解决内存碎片化。**

假设Survivor如果只有一个区域会怎样？MinorGC执行后Eden区被清空了，存活的对象放到了Survivor区，而之前Survivor区中的对象，可能也有一些是需要被清除的。问题来了，这时候我们怎么清除它们？在这种场景下，我们只能标记清除，而我们知道标记清除最大的问题就是内存碎片，在新生代这种经常会消亡的区域，采用标记清除必然会让内存产生严重的碎片化。因为Survivor有2个区域，所以每次MinorGC，会将之前Eden区和From区中的存活对象复制到To区域。第二次MinorGC时，From与To职责兑换，这时候会将Eden区和To区中的存活对象再复制到From区域，以此反复。这种机制最大的好处就是，整个过程中，永远有一个Survivorspace是空的，另一个非空的Survivorspace是无碎片的。那么，Survivor为什么不分更多块呢？比方说分成三个、四个、五个?显然，如果Survivor区再细分下去，每一块的空间就会比较小，容易导致Survivor区满，两块Survivor区是经过权衡之后的最佳方案。

---



##### 7、双亲委派机制

**原理**：如果一个类加载器收到了类加载的请求，它首先不会自己去尝试加载这个类，而是把加载类的请求委托给父类加载器进行加载，由下而上，最终都来到了启动类记载器；然后启动类加载器进行加载，如果自己无法完成这个加载请求，则又会由上而下依次尝试进行加载，直到能有加载器可以完成加载为止。

**好处**：防止内存中出现多份同样的字节码（安全性）

---



##### 8、执行引擎

Execution Engine 执行引擎负责解释命令，提交操作系统执行。

---

##### 9、Native Interface 本地接口

最初是为了融合 C/C++

---

##### 10、Nativ Method Stack 本地方法栈

在本地方法栈中保存 native 方法，在执行引擎执行时加载本地方法库

---

##### 11、程序计数器

每一个线程都有一个程序计数器，是线程私有的，就是一个指针，指向方法区中的方法字节码（用来存储指向下一条指令的地址，也即将要执行的指令代码），由执行引擎读取下一条指令。

它是当前线程所执行的字节码的行号指示器

---

##### 12、Method Area 方法区

作为各个线程共享的运行时内存区域。

存储：类的结构信息（运行时常量池、字段、方法数据、构造方法、普通方法的字节码内容）

具体实现：

​	Java 7 永久代（字符串常量+元信息+字段+静态属性+方法+常量在永久代）

​	Java 8 元空间（字符串常量在堆，元信息+字段+静态属性+方法+常量在元空间）

---

##### 13、Java Stack 栈

主管 Java 程序的运行，生命周期和线程一致，是线程私有的。存储栈桢（一个方法对应一个栈桢）

栈桢：

- 局部变量表
- 操作数栈
- 动态链接
- 方法返回地址

运行原理：

每个方法执行的同时都会创建一个栈桢，每一个方法从调用直到执行完毕的过程，就对应着一个栈桢在虚拟机中入栈到出栈的过程。

栈的大小和具体 JVM的实现有关，通常在 256K~756K之间，约等于1MB左右

栈+堆+方法区的交互关系：HotSpot 是使用指针的方式来访问对象；Java 栈的本地变量表中保存的 Reference 存储的就是对象在 Java 堆中对象的地址；Java 堆中对象会存放访问方法区中元数据的地址。

---

##### 14、Heap 堆

一个 JVM 实例只存在一个堆内存，堆内存的大小是可以调节的。

Java 7逻辑上分为：新生代（伊甸园区、幸存者0区、幸存者1区）、养老代、永久代（和新生代、养老代共在同一个堆内存中）

Java 8逻辑上分为：新生代（伊甸园区、幸存者0区、幸存者1区）、养老代、元空间（使用的是本地内存）

---

**新生区**：

​	新生区是对象的诞生、成长、消亡的区域；一个类在这里产生、应用、最后被垃圾回收器回收。

​	伊甸园区：所有的对象都是在这里被 new 出来的，空间用完时，程序又需要创建对象时会进行 Manor GC，将这里不再被其他对象所引用的对象销毁，然后将剩下的对象移动到幸存者 0 区；若幸存者 0 区也满了，再对该区进行回收，然后移动到 1 区；如果 1 区也满了，再移动到养老区；如果养老区也满了就执行 Full GC（Major GC）；如果还不够，就会报 OOM

​	幸存者区：幸存者0区、幸存者1区

---

**出现 OOM**：

- Java 虚拟机的堆内存设置不够，可以通过参数 -Xms、-Xmx 来调整
- 代码中创建了大量大对象，并且长时间不能被垃圾收集器回收（存在被引用）

---

##### 15、MinorGC 的过程（复制-->清空-->互换）

---



**1、eden、SurvivorFrom 复制到 SurvivorTo，年龄+1**

首先，当 Eden 区满的时候会触发第一次 GC，把还活着的对象拷贝到 SurvivorFrom 区，当 Eden 区再次触发 GC 的时候会扫描 Eden 区和 From 区域，对这两个区域进行垃圾回收，经过这次回收后还存活的对象，直接复制到 To 区域（如果有对象的年龄已经达到了老年的标准，则复制到老年代区），同时把这些对象的年龄+1



**2、清空 eden、SurvivorFrom**

然后，清空 Eden 和 SurvivorFrom 中的对象，也即复制之后有交换，谁空谁是 to



**3、SurvivorTo 和 SurvivorFrom 交换**

最后，SurvivorTo 和 SurvivorFrom 互换，原 SurvivorTo成为下一次 GC 时的 SurvivorFrom 区。部分对象会在 From 和 To 区域中复制来复制去，如此交换 15 次（由 JVM 参数决定 -XX:MaxTenuringThreshold=15，这个参数默认为15），最终如果还是存活，就存入到老年代。



**4、大对象特殊情况**

如果分配的新对象比较大 Eden 取放不下但 Old 区可以放下时，对象会被直接分配到 Old 区



##### 16、堆参数调优

**元空间与永久代之间最大的区别在于：永久代使用的 JVM 的堆内存，但是 Java 8 以后的元空间并不在虚拟机中而是使用本地物理内存。因此，默认情况下，元空间的大小仅受本地内存限制。**

新生区大小：-Xmn （设置初始分配大小，默认为物理内存的 1/64）

Java 堆内存大小：-Xms -Xmx （最大分配内存。默认为物理内存的 1/4）

-XX:+PrintGCDetails （输出详细的GC处理日志）

查看所有 Java 相关的进程：jps -l

查看 Java 指定进程的栈（检查是否发生死锁）：jstack 进程号

使用 Java 的控制台：jconsole 

```java
public static void main(String[] args) throws InterruptedException {
    long totalMemory = Runtime.getRuntime().totalMemory() ;//返回Java虚拟机中的内存总量。
    long maxMemory = Runtime.getRuntime().maxMemory() ;//返回Java虚拟机将尝试使用的最大内存量。
    long freeMemory = Runtime.getRuntime().freeMemory() ;//返回Java虚拟机中的可用内存量。
    System.out.println("TOTAL_MEMORY(-Xms) = " + totalMemory + "（字节）、" + (totalMemory / (double)1024 / 1024) + "MB");
    System.out.println("MAX_MEMORY(-Xmx) = " + maxMemory + "（字节）、" + (maxMemory / (double)1024 / 1024) + "MB");
    System.out.println("freeMemory = " + freeMemory + "（字节）、" + (freeMemory / (double)1024 / 1024) + "MB");

}
```



```java
public static void main(String[] args) throws InterruptedException {
        long totalMemory = Runtime.getRuntime().totalMemory() ;//返回Java虚拟机中的内存总量。
        long maxMemory = Runtime.getRuntime().maxMemory() ;//返回Java虚拟机将尝试使用的最大内存量。
        long freeMemory = Runtime.getRuntime().freeMemory() ;//返回Java虚拟机中的可用内存量。
        System.out.println("TOTAL_MEMORY(-Xms) = " + totalMemory + "（字节）、" + (totalMemory / (double)1024 / 1024) + "MB");
        System.out.println("MAX_MEMORY(-Xmx) = " + maxMemory + "（字节）、" + (maxMemory / (double)1024 / 1024) + "MB");
        System.out.println("freeMemory = " + freeMemory + "（字节）、" + (freeMemory / (double)1024 / 1024) + "MB");

        System.out.println();
        System.out.println("===========111====================");
        byte[] bytes01 = new byte[60 * 1024 * 1024];
        System.out.println("TOTAL_MEMORY(-Xms) = " + Runtime.getRuntime().totalMemory() + "（字节）、" + (Runtime.getRuntime().totalMemory() / (double)1024 / 1024) + "MB");
        System.out.println("MAX_MEMORY(-Xmx) = " + Runtime.getRuntime().maxMemory() + "（字节）、" + (Runtime.getRuntime().maxMemory() / (double)1024 / 1024) + "MB");
        System.out.println("freeMemory = " + Runtime.getRuntime().freeMemory() + "（字节）、" + (Runtime.getRuntime().freeMemory() / (double)1024 / 1024) + "MB");

        System.out.println();
        System.out.println("===========222====================");
        byte[] bytes02 = new byte[30 * 1024 * 1024];
        System.out.println("TOTAL_MEMORY(-Xms) = " + Runtime.getRuntime().totalMemory() + "（字节）、" + (Runtime.getRuntime().totalMemory() / (double)1024 / 1024) + "MB");
        System.out.println("MAX_MEMORY(-Xmx) = " + Runtime.getRuntime().maxMemory() + "（字节）、" + (Runtime.getRuntime().maxMemory() / (double)1024 / 1024) + "MB");
        System.out.println("freeMemory = " + Runtime.getRuntime().freeMemory() + "（字节）、" + (Runtime.getRuntime().freeMemory() / (double)1024 / 1024) + "MB");

        System.out.println();
        System.out.println("===========444====================");
        byte[] bytes03 = new byte[30 * 1024 * 1024];
        System.out.println("TOTAL_MEMORY(-Xms) = " + Runtime.getRuntime().totalMemory() + "（字节）、" + (Runtime.getRuntime().totalMemory() / (double)1024 / 1024) + "MB");
        System.out.println("MAX_MEMORY(-Xmx) = " + Runtime.getRuntime().maxMemory() + "（字节）、" + (Runtime.getRuntime().maxMemory() / (double)1024 / 1024) + "MB");
        System.out.println("freeMemory = " + Runtime.getRuntime().freeMemory() + "（字节）、" + (Runtime.getRuntime().freeMemory() / (double)1024 / 1024) + "MB");

        System.gc();
        //暂停毫秒
        try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }

        System.out.println("################################");
        System.out.println("TOTAL_MEMORY(-Xms) = " + Runtime.getRuntime().totalMemory() + "（字节）、" + (Runtime.getRuntime().totalMemory() / (double)1024 / 1024) + "MB");
        System.out.println("MAX_MEMORY(-Xmx) = " + Runtime.getRuntime().maxMemory() + "（字节）、" + (Runtime.getRuntime().maxMemory() / (double)1024 / 1024) + "MB");
        System.out.println("freeMemory = " + Runtime.getRuntime().freeMemory() + "（字节）、" + (Runtime.getRuntime().freeMemory() / (double)1024 / 1024) + "MB");

    }
```

```java
// Dump文件分析，jvisualvm分析hprof文件
// -Xms8m -Xmx8m -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=D:\myDump
public class ThreadDemo {
    public static void main(String[] args) throws InterruptedException {
        ArrayList<Dog> list = new ArrayList<>();
        while (true){
            list.add(new Dog());
        }
    }
}
class Dog{
    byte[] aByte = new byte[1 * 1024 * 1024];
}
```



