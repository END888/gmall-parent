---
title: 14_synchronized 和 Lock 的区别
date: 2020-08-08
author: yincy

---

### synchronized 和 Lock 的区别

---



1. synchronized 是 Java 内置的关键字；Lock 是 java.util.concurrent 包下的一个接口;
2. synchronized 创建的锁是非公平锁；Lock 默认创建的锁是非公平锁，但是可以根据需求创建非公平锁;
3. synchronized 它的上锁和解锁是自动的（执行完同步代码块会自动释放、出现异常会自动释放）；Lock 是需要程序员手动上锁和手动释放锁（在finally中手动释放）
4. synchronized 无法判断是否获取锁的状态；Lock 可以根据 tryLock() 判断是否获取锁的状态
5. synchronized 是不可中断锁，Lock 是可中断锁
6. synchronized 适用于少量的同步代码问题，Lock 适用于大量的同步代码问题

