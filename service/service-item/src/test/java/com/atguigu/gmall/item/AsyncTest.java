package com.atguigu.gmall.item;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AsyncTest {
    static ExecutorService executor = Executors.newFixedThreadPool(4);

    /**
     * 多任务组合
     * allof、anyof
     * @param args
     * @throws Exception
     */
    public static void allof(String[] args) throws Exception {
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " ==> f1");
        }, executor);
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " ==> f2");
        }, executor);
        CompletableFuture<Void> future3 = CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " ==> f3");
        }, executor);

        // 等到所有的任务都执行完成后再继续往下执行
        CompletableFuture.allOf(future1,future2,future3).join();

        // 只要有一个任务执行完成就继续执行，没有执行完的也以异步的方式继续执行
//        CompletableFuture.anyOf(future1,future2,future3).join();
        System.out.println(Thread.currentThread().getName() + " ==> main");
        executor.shutdown();
    }

    public static void jieshoa(String[] args) throws Exception {
        //1、异步编排 CompletableFuture jdk8的一个新特性
        //把异步任务编排起来【skuId】
        //查基本信息(skuId)、查图片(skuId)、查分类(c3Id)、
        //查销售属性(skuId,spuId)、查实时价格(skuId)、查skuvaluejson(spuId)
        //1）、等所有的异步任务全部执行完成，查到结果以后，把整个数据聚合返回
        //2）、所有异步任务之间关系比较复杂
        //查基本信息(skuId): skuInfo(spuId、c3Id)
        //   -- 查分类(c3Id):
        //   -- 查销售属性(skuId,spuId)
        //   -- 查skuvaluejson(spuId)
        //查图片(skuId)
        //查实时价格(skuId)
        //  同一层级异步执行，父子层级等待。
        //  以前的技术来解决异步之间的编排关系。想想都复杂



        //1、启动异步任务
        //    - CompletableFuture.runAsync()   ：CompletableFuture<Void> future
        //    - CompletableFuture.supplyAsync()：
        //2、编排他们的关系
        //     future.whenComplete()
    }


    /**
     * thenXXX编排任务
     *
     * CompletableFuture<Void>
     * CompletableFuture<Integer>
     *
     *     1、thenRun()\thenRunAsync() future.thenXXXX() ： 接下来干什么 CompletableFuture<Void>
     *          thenRun(runnable)：              不用异步跑任务，而是用主线程
     *          thenRunAsync(runnable):          用异步跑任务，使用默认ForkJoin线程池
     *          thenRunAsync(runnable,executor)
     *        接下来干活用不到上一步的结果，自己运行完也不返回任何结果 CompletableFuture<Void>
     *     2、thenAccept()\thenAcceptAsync()： CompletableFuture<Void>
     *          thenAccept(consumer):       拿到上一步结果，不用异步跑任务，而是用主线程
     *          thenAcceptAsync(consumer):  拿到上一步结果，用异步跑任务，使用默认ForkJoin线程池
     *          thenAcceptAsync(consumer,executor)  拿到上一步结果，用异步跑任务，使用指定线程池
     *     3、thenApply()\thenApplyAsync()：  拿到上一步结果，还能自己返回新结果
     *          thenApply(function)：        拿到上一步结果，不用异步跑任务，而是用主线程，并返回自己的计算结果
     *          thenApplyAsync(function)：   拿到上一步结果，用异步跑任务，用默认线程池，并返回自己的计算结果
     *          thenApplyAsync(function,executor)： 拿到上一步结果，用异步跑任务，用指定线程池，并返回自己的计算结果
     *
     *  thenRun： 不接收上一次结果，无返回值
     *  thenAccept：接收上一次结果，无返回值
     *  thenApply： 接收上一次结果，有返回值
     */

    /**
     * 启动一个异步任务没有返回值
     */
    public static void runAsync(String[] args) {

        // 1、启动一个异步任务没有返回值
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            System.out.println("runAsync---------");
        }, executor);

        // 1.1、没有参数，没有返回值
        future1.thenRunAsync(()->{
            System.out.println("thenRunAsync---------");
        });

        executor.shutdown();

    }

    /**
     * 启动一个异步任务有返回值
     */
    public static void supplyAsync(String[] args) throws InterruptedException {

        // 2、启动一个异步任务有返回值
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("supplyAsync---------");
            return 1;
        },executor);

        // 2.1、没有参数，没有返回值
        future2.thenRunAsync(()->{
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("【thenRunAsync】" + Thread.currentThread().getName() + " ===>");
        },executor);

        // 2.2、有参数，没有返回值
        future2.thenAcceptAsync(r->{
            System.out.println("【thenAcceptAsync】" + Thread.currentThread().getName() + " ===> 上一步的返回结果：" + r);
        },executor);

        // 2.3、有参数、有返回值
        future2.thenApplyAsync(r->{
            System.out.println("【thenApplyAsync】" + Thread.currentThread().getName() + " ===> 上一步的返回结果：" + r);
            return r + 5;
        },executor);

        executor.shutdown();
    }
}
