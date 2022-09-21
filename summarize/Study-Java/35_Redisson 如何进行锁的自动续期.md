---
title: 35_Redisson 如何进行锁的自动续期
date: 2022-08-20
author: yincy
---



### 35_Redisson 如何进行锁的自动续期

Redisson 的加锁方式有两种：

1. lock():  
   1. lock(): 阻塞式等锁，一直等，直到获取到锁；获取到锁之后，有自动续期；每过 lockWatchDogTimeOut / 3 会自动续期，续期到 lockWatchDogTimeOut ;
   2. lock(leasetime,TimeUnit): 指定锁的释放时间，阻塞式等锁，一直等，直到获取到锁；获取到锁之后，没有自动续期；等到锁的释放时间到了，锁会自动释放；
2. trylock():
   1. trylock(): 会立即尝试获取锁；获取到了，返回 true；获取不到，返回 false；有自动续期；每过 lockWatchDogTimeOut / 3 会自动续期，续期到 lockWatchDogTimeOut ;
   2. trylock(waitTime,TimeUnit)：在指定的等待时间内，一直尝试获取锁；获取到了，返回 true；获取不到，返回 false；有自动续期；每过 lockWatchDogTimeOut / 3 会自动续期，续期到 lockWatchDogTimeOut ;
   3. trylock(waitTime,leaseTime,TimeUnit): 在指定的等待时间内，一直尝试获取锁；获取到了，返回 true；获取不到，返回 false；没有自动续期；等到锁的释放时间到了，锁会自动释放；

---

其实上面的几种加锁方式都是调用的一个方法：

**private <T> RFuture<Long> tryAcquireAsync(long waitTime, long leaseTime, TimeUnit unit, long threadId)** 

参数：

1. waitTime：等待时间；在指定的等待时间内一直尝试获取锁
2. leaseTime:：锁的释放时间；当获取锁后，会根据设置的锁的释放时间，指定什么时候释放自动锁
3. TimeUnit：时间单位
4. threadId：当前线程 id

---

tryAcquireAsync：

1. 先声明一个异步任务：RFuture<Long> ttlRemainingFuture
2. 判断锁的自动释放时间是否不等于-1？【leaseTime != -1】
   1. 不等于 -1：
      1. ttlRemainingFuture = tryLockInnerAsync(waitTime, **leaseTime**, unit, threadId, RedisCommands.EVAL_LONG);
   2. 等于 -1：
      1. ttlRemainingFuture = tryLockInnerAsync(waitTime, **internalLockLeaseTime,TimeUnit.MILLISECONDS**, threadId, RedisCommands.EVAL_LONG);【internalLockLeaseTime：lockWatchdogTimeout：30s】
      2. 当异步任务执行完成后，会根据 剩余时间、返回值 进行判断：
         1. 如果返回值不为 null，则说明执行完毕，退出；
         2. 如果剩余时间为 null（一开始就是 null ）：
            1. 如果锁的自动释放时间为不等于 -1：
               1. 通过unit 将转换后的锁的自动释放时间设置给 internalLockLeaseTime
            2. 如果锁的自动释放时间为等于 -1：【调用 scheduleExpirationRenewal() ---> renewExpiration() 重新设置释放时间 】
               1. 通过一个时间轮 ，设置当锁的自动释放时间为 internalLockLeaseTime 【lockWatchdogTimeout：30s】/ 3 时，再次设置锁的过期时间为看门狗时间，从而实现锁的自动续期。



