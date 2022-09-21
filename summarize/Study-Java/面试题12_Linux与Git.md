---
title: 面试题12_Linux与Git
date: 2022-08-16
author: yincy
---

### 面试题12_Linux与Git

#### 1、常用 Linux 命令

1、查看应用进程：lsof -i:端口号

2、查看应用位置：whereis 应用名

3、查看内存：free -m 或 top

4、查看磁盘存储情况：df -h

5、查看端口占用情况：netstat -anp | grep 端口号

6、查看报告系统运行时长及平均负载：uptime

7、查看进程：ps aux

8、查看当前所有已经使用的端口情况：netstat -nultp

---



#### 2、如何查看测试项目的日志

一般测试的项目里面，有个logs的目录文件，会存放日志文件，有个xxx.out的文件，可以用tail -f 动态实时查看后端日志

先cd 到logs目录(里面有xx.out文件)

tail -f xx.out

这时屏幕上会动态实时显示当前的日志，ctr+c停止

---

#### 3、如何查看最近1000行日志

tail -1000 xx.out

---

