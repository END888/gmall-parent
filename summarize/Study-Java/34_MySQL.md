---
title: 34_MySQL
date: 2022-08-20
author: yincy
---



### MySQL



------

##### 1、什么是事务？

在一次 SQL 连接会话过程中执行的所有 SQL，要么都成功，要么都失败

------



##### 2、事务的四大特性

1、(Atomicity) 原子性

2、(Consistency) 一致性

3、(Isolation) 独立性

4、(Durability) 持久性

------



##### 3、事务的隔离级别

| 读数据一致性及允许的并发副作用隔离级别 | 读数据一致性                             | 脏读 | 不可重复读 | 幻读 |
| -------------------------------------- | ---------------------------------------- | ---- | ---------- | ---- |
| 读未提交（Read uncommitted）           | 最低级别，只能保证不读取物理上损坏的数据 | 是   | 是         | 是   |
| 读已提交（Read committed）             | 语句级                                   | 否   | 是         | 是   |
| 可重复读（Repeatable read）            | 事务级                                   | 否   | 否         | 是   |
| 可序列化（Serializable）               | 最高级别，事务级                         | 否   | 否         | 否   |

------



##### 4、脏读、不可重复读、幻读

1、脏读

> 一个事务读取到其他事务未提交的数据

2、不可重复读

> 读取到已提交事务的数据，而两次读取过程中数据被别人修改过了，导致两次读取的结果不一致

3、幻读

> 一个事务读取到另一个事务新提交的新数据，导致两次读取数据不一致

在Innodb引擎下MySQL里执行的事务,默认情况下不会发生脏读、不可重复读和幻读的问题

------



##### 5、MySQL 的逻辑架构

1、连接层

2、服务层

3、引擎层

4、存储层

------



##### 6、查看 MySQL 已提供的存储引擎

```shell
show engines;
```

------



##### 7、查看 MySQL 当前默认的存储引擎

```shell
show variables like '%storage_engine%'
```

------



##### 8、InnoDB 和 MyISAM 的区别

| 对比项   | MyISAM                     | InnoDB                                                       |
| -------- | -------------------------- | ------------------------------------------------------------ |
| 主外键   | 不支持                     | 支持                                                         |
| 事务     | 不支持                     | 支持                                                         |
| 行表锁   | 表锁（不支持高并发）       | 行锁（支持高并发）                                           |
| 缓存     | 只缓存索引，不缓存真实数据 | 不仅缓存索引还要缓存真实数据，对内存要求较高，而且内存大小对性能有决定性的影响 |
| 表空间   | 小                         | 大                                                           |
| 关注点   | 查询性能                   | 事务                                                         |
| 默认安装 | Y                          | Y                                                            |

------



##### 9、SQL 的执行顺序

**书写顺序**

```sql
select 
	<select_list>
from
	<left_table><join_type>
join <right_table> on <join_condition>
where
	<where_codition>
group by
	<group_by_list>
having
	<having_condition>
order by
	<order_by_condition>
limit <limit_number>
```

**机读顺序**

```sql
from <left_table>
on <join_condition>
<join_type> join <right_table>
where <where_condition>
group by <group_by_list>
having <having_condition>
select
distinct <select_list>
order by <order_by_condition>
limit <limit_number>
```



------



##### 10、SQL join

```sql

```

---



##### 11、索引是什么？

索引就是一个排好序的可以快速查找的一个B+树数据结构

---

##### 12、索引分类

1、唯一索引

2、单值索引

3、复合索引

---



##### 13、创建、删除、查看索引



1、创建索引

```sql
# 方式1
create index 索引名 on 表名(字段1，字段2...);
# 方式2
alter table 表名 add index 索引名(字段1，字段2...);
```

2、删除索引

```sql
drop index 索引名 on 表名
```

3、查看索引

```sql
show index from 表名;
```



---



##### 14、InnoDB 为什么要使用 B+ 树？

如果 InnoDB 不使用 B+ 树，首先，如果使用数组进行存储的话，由于数组中的元素在内存空间上是连续的，所以查找或修改指定下标元素的时间复杂度为 O(1)，查找元素的效率很高，但是如果要删除数组中的某个元素的时候，就需要将该位置后的元素前移；要向数组中插入某个元素的时候，又要将该位置后的元素后移，所以它在插入和删除指定下标元素的时间复杂度为 O(n)，这样分析下来，数组的查询和修改效率高但是插入和删除效率却很低，性能不稳定，所以不能使用数组；

如果使用 Hash 表存储元素的话，由于 Hash 表它的增删改查的时间复杂度均为 O(1)，但是 Hash 表存储的元素是无序的，无法对元素进行排序和范围查询，并且 InnoDB 也并不支持 Hash 表来存储索引，所以不能使用 Hash 表；

如果使用普通二叉树的话，由于二叉树查找元素时使用二分查找法，所以它在进行查找方面的时间复杂度为 O(logn)，但是由于普通二叉树可能会出现数据倾斜从而导致普通二叉树退化为链表，所以不能使用普通二叉树；

如果使用平衡二叉树的话，由于平衡二叉树能够通过旋转的方式来防止出现数据倾斜，所以它的性能稳定，平均访问、搜索、插入、删除的时间复杂度均为 O(logn)，但是平衡二叉树的深度会很容易随着元素的增长而增长，导致我们查询叶子节点的次数增多，而树的深度又决定了对磁盘的IO次数，所以不能使用平衡二叉树；

如果使用B树的话，就可以降低树的高度，从而减少磁盘IO次数；而 InnoDB 使用的是 B+ 树，是因为 B+ 树相较于 B 树而言，B+ 树的所有数据都存储在叶子节点上，非叶子节点上仅存储键值，而B树不仅叶子节点上可以存储数据，它的非叶子节点上也可以存储数据，但是由于InnoDB中的磁盘页的默认大小是16kB，空间有限，为了能够在一个磁盘页中存储更多的键值，从而再次降低树的深度减少磁盘IO，这样一来数据查询的效率也会提高，并且B+树的所有数据均存储在叶子节点，而且数据是按照顺序以链表的方式连接着的，所以使用B+树可以让 InnoDB 更高效地进行范围查找、排序查找、分组查找以及去重查找。

---



##### 15、索引的缺点

1、创建索引和维护索引需要耗费时间，并且随着数据量的增加，所耗费的时间也会增加

2、索引需要占磁盘空间，除了数据表占数据空间之外，每一个索引还要占一定的物理空间，存储在磁盘上，如果有大量的索引，索引文件就可能比数据文件更快达到最大文件尺寸

3、虽然索引大大提高了查询速度，同时却会降低更新表的速度。当对表中的数据进行增加、删除和修改的时候，索引也要动态地维护，这样就降低了数据的维护速度。

---



##### 15、Explain 执行计划

1、id（查询序列号）

- 相同由上到下
- 不同大者优先
- 相同+不同：大者优先在前，由上到下在后

2、select_type（查询类型）

- simple（简单查询）
- primary（外层查询）
- subQuery（子查询）
- derived（临时表查询）
- union（联合查询后面的查询）
- union result（联合结果查询）

3、table（查的哪张表）

4、type

- system
- const
- eq_ref
- ref
- range
- index
- null

5、possible_keys（可能用到的索引）

6、key（实际使用的索引）

7、key_len（索引中使用的字节数）

8、ref（显示索引的哪一列被使用了）

9、rows（查询时必须检查的行数，值越小越好）

10、filtered（满足查询的比例）

11、Extra（其他信息）

- Using filesort（无法利用索引排序的操作称之为文件排序，应尽量避免）
- Using temporary（使用了临时表保存中间结果，MySQL 在对查询结果排序时使用临时表，常见于排序和分组查询）
- Using index
- Using where

---

##### 16、如何避免索引失效

1、全值匹配我最爱

2、最佳左前缀法则    

3、不在索引列上做任何操作（计算、函数、(自动or手动)类型转换），会导致索引失效而转向全表扫描

4、存储引擎不能使用索引中范围条件右边的列

5、尽量使用覆盖索引(只访问索引的查询(索引列和查询列一致))，减少select 

6、注意null/not null对索引的可能影响

7、like以通配符开头('%abc...')mysql索引失效会变成全表扫描的操作

8、字符串不加单引号索引失效

9、mysql 在使用不等于(!= 或者<>)的时候有时候无法使用索引会导致全表扫描

10、少用or,用它来连接时会索引失效

11、上面9/10情况如何处理？最后使用索引的手段：覆盖索引

> 【优化口诀】select * 前提下
>
> 全职匹配我最爱，最左前缀要遵守；
> 带头大哥不能死，中间兄弟不能断；
> 索引列上少计算，范围之后全失效；
> LIKE百分写最右，覆盖索引不写*；
> 不等空值还有OR，索引影响要注意；
> VAR引号不可丢， SQL优化有诀窍。

---



##### 17、关联查询优化

A left  join B，A驱动表，B是被驱动表，我们在被驱动表上做索引，左驱右写右；

A right join B，B驱动表，A是被驱动表，我们在被驱动表上做索引，右驱左写左；



---

##### 18、排序分组优化

1、order by 关键字优化

```sql
key a_b_c(a,b,c)

order by能使用索引最左前缀
-order by a
-order by a,b
-order by a,b,c
-order by a desc,b desc,c desc

如果 where 使用索引的最左前缀定义为常量，则 order by 能使用索引
-where a = const order by b,c
-where a = const and b = const order by c
-where a = const order by b,c
-where a = const and b > const order by b,c

不能使用索引进行排序
-order by a asc,b desc,c desc //排序不一致
-where g = const order by b,c //丢失a索引
-where a = const order by c //丢失b索引
-where a = const order by a,d //d不是索引的一部分
-where a in (...) order by b,c //对于排序来说，多个相等条件也是范围查询
```



2、group by 关键字优化

- group by实质是先排序后进行分组，遵照索引建的最佳左前缀
- where高于having，能写在where限定的条件就不要去having限定了。
- group by 使用索引的原则几乎跟order by一致 ，唯一区别是groupby 即使没有过滤条件用到索引，也可以直接使用索引。

---



##### 19、分页优化

```sql
/*记住了上次的分页的最后一条数据的id是100，这边就直接跳过100，从101开始扫描表*/
SELECT 
  a.empno,
  a.ename,
  a.job,
  a.sal,
  b.deptno,
  b.dname 
FROM
  emp a 
  LEFT JOIN dept b 
    ON a.deptno = b.deptno 
WHERE a.id > 100 ORDER BY a.id LIMIT 30;    


/*记住了上次的分页的最后一条数据的id是4950000，这边就直接跳过4950000，从4950001开始扫描表*/
SELECT 
  a.empno,
  a.ename,
  a.job,
  a.sal,
  b.deptno,
  b.dname 
FROM
  emp a 
  LEFT JOIN dept b 
    ON a.deptno = b.deptno 
WHERE a.id > 4950000 ORDER BY a.id LIMIT 30;
```

> **原理**：记住上次查找结果的主键位置，避免使用偏移量 offset

---



##### 20、InnoDB 的行锁到底锁了什么

InnoDB的行锁，是通过锁住索引来实现的，如果加锁查询的时候没有使用到索引，会将整个聚簇索引都锁住，相当于锁表了。

命中索引锁行，没有命中锁表，问题会扩大化，小心

