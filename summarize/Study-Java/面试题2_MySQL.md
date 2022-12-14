---
title: 面试题2_MySQL
date: 2022-08-16
author: yincy
---

### 面试题2_MySQL



------

#### 1、什么是事务？

在一次 SQL 连接会话过程中执行的所有 SQL，要么都成功，要么都失败

------



#### 2、事务的四大特性

1、原子性（Atomicity）：事务开始后所有操作，要么全部做完，要么全部不做，不可能停滞在中间环节。事务执行过程中出错，会回滚到事务开始前的状态，所有的操作就像没有发生一样。也就是说事务是一个不可分割的整体，就像化学中学过的原子，是物质构成的基本单位

2、一致性（Consistency）：事务开始前和结束后，数据库的完整性约束没有被破坏 。比如A向B转账，不可能A扣了钱，B却没收到。

3、隔离性（Isolation）：同一时间，只允许一个事务请求同一数据，不同的事务之间彼此没有任何干扰。比如A正在从一张银行卡中取钱，在A取钱的过程结束前，B不能向这张卡转账。

4、持久性（Durability）：事务完成后，事务对数据库的所有更新将被保存到数据库，不能回滚。

------



#### 3、事务的隔离级别

| 读数据一致性及允许的并发副作用隔离级别 | 读数据一致性                             | 脏读 | 不可重复读 | 幻读 |
| -------------------------------------- | ---------------------------------------- | ---- | ---------- | ---- |
| 读未提交（Read uncommitted）           | 最低级别，只能保证不读取物理上损坏的数据 | 是   | 是         | 是   |
| 读已提交（Read committed）             | 语句级                                   | 否   | 是         | 是   |
| 可重复读（Repeatable read）            | 事务级                                   | 否   | 否         | 是   |
| 可序列化（Serializable）               | 最高级别，事务级                         | 否   | 否         | 否   |

------



#### 4、脏读、不可重复读、幻读

1、脏读

> 一个事务读取到其他事务未提交的数据

2、不可重复读

> 读取到已提交事务的数据，而两次读取过程中数据被别人修改过了，导致两次读取的结果不一致

3、幻读

> 一个事务读取到另一个事务新提交的新数据，导致两次读取数据不一致

在Innodb引擎下MySQL里执行的事务,默认情况下不会发生脏读、不可重复读和幻读的问题

------



#### 5、MySQL 的逻辑架构

1、连接层

2、服务层

3、引擎层

4、存储层

------



#### 6、查看 MySQL 已提供的存储引擎

```shell
show engines;
```

------



#### 7、查看 MySQL 当前默认的存储引擎

```shell
show variables like '%storage_engine%'
```

------



#### 8、InnoDB 和 MyISAM 的区别

| 对比项 | MyISAM                     | InnoDB                                                       |
| ------ | -------------------------- | ------------------------------------------------------------ |
| 主外键 | 不支持                     | 支持                                                         |
| 事务   | 不支持                     | 支持                                                         |
| 行表锁 | 表锁（不支持高并发）       | 行锁（支持高并发）                                           |
| 缓存   | 只缓存索引，不缓存真实数据 | 不仅缓存索引还要缓存真实数据，对内存要求较高，而且内存大小对性能有决定性的影响 |
| 表空间 | 小                         | 大                                                           |
| 关注点 | 查询性能                   | 事务                                                         |



------



#### 9、索引是什么？

索引就是一个排好序的可以快速查找的一个B+树数据结构

------



#### 10、InnoDB 为什么要使用 B+ 树？

如果 InnoDB 不使用 B+ 树，首先，如果使用数组进行存储的话，由于数组中的元素在内存空间上是连续的，并且支持随机访问，所以查找或修改指定下标元素的时间复杂度为 O(1)，效率很高，但是如果要插入和删除数组中的某个元素的时候，就需要移动数组中的元素，所以它在插入和删除指定下标元素的时间复杂度为 O(n)，这样分析下来，数组的查询和修改效率高但是插入和删除效率却很低，性能不稳定，所以不能使用数组；

如果使用 Hash 表存储元素的话，由于 Hash 表它的增删改查的时间复杂度均为 O(1)，但是 Hash 表存储的元素是无序的，无法对元素进行排序和范围查询，并且 InnoDB 也并不支持 Hash 表来存储索引，所以不能使用 Hash 表；

如果使用普通二叉树的话，由于二叉树查找元素时使用二分查找法，所以它在进行查找方面的时间复杂度为 O(logn)，但是由于普通二叉树可能会出现数据倾斜从而导致普通二叉树退化为链表，所以不能使用普通二叉树；

如果使用平衡二叉树的话，由于平衡二叉树能够通过旋转的方式来防止出现数据倾斜，所以它的性能稳定，平均访问、搜索、插入、删除的时间复杂度均为 O(logn)，但是平衡二叉树的深度会很容易随着元素的增长而增长，导致我们查询叶子节点的次数增多，而树的深度又决定了对磁盘的IO次数，所以不能使用平衡二叉树；

如果使用B树的话，就可以降低树的高度，从而减少磁盘IO次数；而 InnoDB 使用的是 B+ 树，是因为 B+ 树相较于 B 树而言，B+ 树的所有数据都存储在叶子节点上，非叶子节点上仅存储键值，而B树不仅叶子节点上可以存储数据，它的非叶子节点上也可以存储数据，但是由于InnoDB中的磁盘页的默认大小是16kB，空间有限，为了能够在一个磁盘页中存储更多的键值，从而再次降低树的深度减少磁盘IO，这样一来数据查询的效率也会提高，并且B+树的所有数据均存储在叶子节点，而且数据是按照顺序以链表的方式连接着的，所以使用B+树可以让 InnoDB 更高效地进行范围查找、排序查找、分组查找以及去重查找。

------

#### 11、索引的优点

1、提高数据检索的效率，降低数据库的IO成本；

2、降低数据排序的成本，降低了CPU的消耗

---



#### 11、索引的缺点

1、创建索引和维护索引需要耗费时间，并且随着数据量的增加，所耗费的时间也会增加

2、索引需要占磁盘空间，除了数据表占数据空间之外，每一个索引还要占一定的物理空间，存储在磁盘上，如果有大量的索引，索引文件就可能比数据文件更快达到最大文件尺寸

3、虽然索引大大提高了查询速度，同时却会降低更新表的速度。当对表中的数据进行增加、删除和修改的时候，索引也要动态地维护，这样就降低了数据的维护速度。



------

#### 12、如何避免索引失效

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



------



#### 13、InnoDB 的行锁到底锁了什么

InnoDB的行锁，是通过锁住索引来实现的，如果加锁查询的时候没有使用到索引，会将整个聚簇索引都锁住，相当于锁表了。

命中索引锁行，没有命中锁表，问题会扩大化，小心



---

#### 14、如何查看死锁

1、查看表被锁状态和结束死锁：

```sql
use 数据库名; // 切换到具体的数据库

show engine innodb status;	// 查询数据库是否发生死锁

```

2、查看数据表被锁状态

```sql
show open table where In_use>0;
```

3、分析锁表的SQL，通过SQL日志，分析相应SQL，给表加索引，常用字段加索引，表关联子弹加索引等方式对SQL进行优化



4、查看运行的所有事务

```sql
SELECT * FROM  information_schema.innodb_trx;

kill 线程id  # 线程id就是查询出来的 trx_mysql_thread_id
```

5、查询进程

```sql
show processlist;
```



---

#### 15、有没有设计过数据库表？你是如何设计的？



| 第一范式 | 每一列属性(字段)不可分割的,字段必须保证原子性两列的属性值相近或者一样的,尽量合并到一列或者分表,确保数据不冗余 |
| -------- | ------------------------------------------------------------ |
| 第二范式 | 每一行的数据只能与其中一行有关 即 主键  一行数据只能做一件事情或者表达一个意思,只要数据出现重复,就要进行表的拆分 |
| 第三范式 | 数据不能存在传递关系,每个属性都跟主键有直接关联而不是间接关联 |

---

#### 16、聚簇索引与非聚簇索引有什么区别



聚簇索引一个表只能有一个，聚簇索引是将表的数据行都存放在索引树的叶子节点上，InnoDB的聚簇索引实际上是将索引和数据保存在同一个B+Tree中，InnoDB通过主键聚集数据，如果没有定义主键，InnoDB会选择一个唯一且非空的索引，如果没有这样的索引，InnoDB会隐式定义一个主键来作为聚簇索引。



非聚簇索引的叶子节点中保存的不是指向数据行的物理指针，而是行的主键值，当通过非聚簇索引查找数据行的时候，存储引擎需要在非聚簇索引中找到相应的叶子节点，获得行的主键值，然后使用主键去聚簇索引中查找数据行，这就需要两次B+Tree查找。

都是B+树的数据结构

聚簇索引:将数据存储与索引放到了一块、并且是按照一定的顺序组织的，找到索引也就找到了数据，数据的物理存放顺序与索引顺序是一致的，即:只要索引是相邻的，那么对应的数据一定也是相邻地存放在磁盘上的

非聚簇索引叶子节点不存储数据、存储的是数据行地址，也就是说根据索引查找到数据行的位置再取磁盘查找数据，这个就有点类似一本书的目录，比如我们要找第三章第一节，那我们先在这个目录里面找，找到对应的页码后再去对应的页码看文章。

优势:

1、查询通过聚簇索引可以直接获取数据，相比非聚簇索引需要第二次查询(非覆盖索引的情况下)效率要高

2、聚簇索引对于范围查询的效率很高，因为其数据是按照大小排列的

3、聚簇索引适合用在排序的场合，非聚簇索引不适合



---

#### 17、B+tree 与 B-tree 的区别





---

#### 18、以 mysql 为例，linux 如何排查问题

类似提问方式:如果线上环境出现问题比如网站卡顿重则瘫痪 如何是好?

--->linux--->mysql/redis/nacos/sentinel/sluth--->可以从以上提到的技术点中选择一个自己熟悉单技术点进行分析

以mysql为例

1,架构层面 是否使用主从

2,表结构层面 是否满足常规的表设计规范(大量冗余字段会导致查询会变得很复杂)

3,sql语句层面(⭐)

前提:由于慢查询日志记录默认是关闭的,所以开启数据库mysql的慢查询记录 的功能 从慢查询日志中去获取哪些sql语句时慢查询  默认10S ,从中获取到sql语句进行分析

3.1 explain 分析一条sql

 

还可能这样去提问：sql语句中哪些位置适合建索引/索引建立在哪个位置

Select id,name,age from user where id=1 and name=”xxx” order by age

总结:  查询字段  查询条件(最常用)   排序/分组字段



---

#### 19、如何处理慢查询

慢查询的优化首先要搞明白慢的原因是什么? 

① 是查询条件没有命中索引?

② 是load了不需要的数据列?

③ 还是数据量太大?

所以优化也是针对这三个方向来的：

1、分析语句的执行计划,然后获得其使用索引的情况,之后修改语句或者修改索引,使得语句可以尽可能的命中索引。

2、分析语句，看看是否load了额外的数据，可能是查询了多余的行并且抛弃掉了，可能是加载了许多结果中并不需要的列，对语句进行分析以及重写。

3、如果是表中的数据量是否太大导致查询慢，可以进行横向或者纵向的分表.

---

#### 20、MySQL的主从复制原理

1、master将写操作记录到二进制日志（binary log）。这些记录过程叫做二进制日志事件，binary log events；

2、slave将master的binary log events拷贝到它的中继日志（relay log）；

从服务器I/O线程将主服务器的二进制日志读取过来记录到从服务器本地文件，然后SQL线程会读取relay-log日志的内容并应用到从服务器，从而使从服务器和主服务器的数据保持一致！

3、slave重做中继日志中的事件，将改变应用到自己的数据库中。 MySQL复制是**异步的且串行化的，而且重启后从接入点开始复制。**

复制的最大问题（**延时**）

---



复制的基本原则：

每个slave只有一个master

每个slave只能有一个唯一的服务器ID

每个master可以有多个salve

---

#### 21、MySQL 的内部结构

大体来说，MySQL可以分为**server层**和**存储引擎层**两部分。

① server层包括连接器、查询缓存、分析器、优化器、执行器等，涵盖MySQL的大多数核心服务功能

② 存储引擎层：**存储引擎层负责数据的存储和提取**。其架构模式是插件式的，支持InnoDB、MyISAM、Memory等多个存储引擎

**连接器**：连接器负责跟客户端建立连接、获取权限、维持和管理连接。

**查询缓存**：连接建立完成后，你就可以执行select语句了，此时会先进行查询缓存(缓存是key-value格式；key是sql语句，value是sql语句的查询结果)。

**分析器**：

​	1、词法分析： MySQL需要识别出里面的字符串分别是什么，代表什么。

​	2、语法分析：根据词法分析的结果，语法分析器会根据语法规则，判断你输入的这个SQL语句是否满足MySQL语法。

**优化器**：优化器是在表里面有多个索引的时候，决定使用哪个索引；或者在一个语句有多表关联（join）的时候，决定各个表的连接顺序。

**执行器**：调用存储引擎接口，执行sql语句，得到结果

---

#### 22、常见的索引约束有哪些？

1、UNIQUE：唯一索引

​	表示唯一的，不允许重复的索引，如果该字段信息保证不会重复例如身份证号用作索引时，可设置为UNIQUE。

2、FULLTEXT: 全文索引

​	表示全文搜索，在检索长文本的时候，效果最好，短文本建议使用普通索引,但是在检索的时候数据量比较大的时候，现将数据放入一个没有全局索引的表中，然后在用Create Index创

​	建的Full Text索

​	引，要比先为一张表建立Full Text然后在写入数据要快的很多。FULLTEXT 用于搜索很长一篇文章的时候，效果最好。用在比较短的文本，如果就一两行字的，普通的 INDEX 也可以。

3、SPATIAL: 空间索引

​	空间索引是对空间数据类型的字段建立的索引，MYSQL中的空间数据类型有4种，分别是GEOMETRY、POINT、LINESTRING、POLYGON。MYSQL使用

SPATIAL关键字进行扩展，使得能够用于

创建正规索引类型的语法创建空间索引。创建空间索引的列，必须将其声明为NOT NULL，空间索引只能在存储引擎为MYISAM的表中创建。

如果没有指定索引约束，此时创建的索引就是普通索引。而一般情况下只需要创建普通索引。

---

#### 23、常见的索引类型有哪些？

索引是在MySQL的存储引擎层中实现的，而不是在服务器层实现的。所以每种存储引擎的索引都不一定完全相同，也不是所有的存储引擎都支持所有的索引类型的。

MySQL目前提供了以下4种索引： 

- [x] BTREE 索引： 最常见的索引类型，大部分索引都支持 B 树索引。
- [x] HASH 索引：只有Memory引擎支持 ， 使用场景简单 。
- [x] R-tree 索引（空间索引）：空间索引是MyISAM引擎的一个特殊索引类型，主要用于地理空间数据类型，通常使用较少，不做特别介绍。
- [x] Full-text （全文索引） ：全文索引也是MyISAM的一个特殊索引类型，主要用于全文索引，InnoDB从Mysql5.6版本开始支持全文索引。

各种存储引擎对索引的支持：

| 索引        | InnoDB引擎      | MyISAM引擎 | Memory引擎 |
| ----------- | --------------- | ---------- | ---------- |
| BTREE索引   | 支持            | 支持       | 支持       |
| HASH 索引   | 不支持          | 不支持     | 支持       |
| R-tree 索引 | 不支持          | 支持       | 不支持     |
| Full-text   | 5.6版本之后支持 | 支持       | 不支持     |

我们平常所说的索引，如果没有特别指明，都是指B+树（多路搜索树，并不一定是二叉的）结构组织的索引。

---

#### 24、唯一索引比普通索引快吗,  为什么？

唯一索引不一定比普通索引快, 还可能慢。

1、**查询时**, 在未使用 limit 1 的情况下, 在匹配到一条数据后, 唯一索引即返回, 普通索引会继续匹配下一条数据, 发现不匹配后返回. 如此看来唯一索引少了一次匹配, 

但实际上这个消耗微乎其微。

2、**更新时**, 这个情况就比较复杂了. 普通索引将记录放到 change buffer 中语句就执行完毕了。而对唯一索引而言, 它必须要校验唯一性, 因此, 必须将数据页读入内

存确定没有冲突, 然后才能继续操作。

对于写多读少的情况 , 普通索引利用 change buffer 有效减少了对磁盘的访问次数, 因此普通索引性能要高于唯一索引.

---

#### 25、什么情况下设置了索引但无法使用？



1、如果条件中有or，即使其中有条件带索引也不会使用(这也是为什么尽量少用or的原因)，要想使用or，又想让索引生效，只能将or条件中的每个列都加上索引

2、对于多列索引，不是使用的第一部分，则不会使用索引

3、like查询以%开头

4、如果列类型是字符串，那一定要在条件中将数据使用引号引用起来,否则不使用索引

5、如果mysql估计使用全表扫描要比使用索引快,则不使用索引

6、组合索引要遵循 最左匹配原则

解决方案 ： 通过覆盖索引来解决 

---

#### 26、在建立索引的时候,都有哪些需要考虑的因素呢？

① 建立索引的时候一般要考虑到字段的使用频率,经常作为条件进行查询的字段比较适合。

② 如果需要建立联合索引的话,还需要考虑联合索引中的顺序。

③ 此外也要考虑其他方面,比如防止过多的索引对表造成太大的压力

---

#### 27、创建的索引有没有被使用到?或者说怎么才可以知道这条语句运行很慢的原因?

MySQL提供了**explain**命令来查看语句的执行计划,MySQL在执行某个语句之前,会将该语句过一遍查询优化器,之后会拿到对语句的分析,也就是执行计划,其中包含了许多信息. 可以通过其中和索引有关的信息来分析是否命中了索引,例如possilbe_key,key,key_len等字段,分别说明了此语句可能会使用的索引,实际使用的索引以及使用的索引长度。

---

#### 28、如何优化SQL？

（1）尽量选择较小的列

（2）将where中用的比较频繁的字段建立索引

（3）select子句中避免使用‘*’

（4）避免在索引列上使用计算、not in 和<>等操作

（5）当只需要一行数据的时候使用limit 1

（6）保证单表数据不超过200W，适时分割表。针对查询较慢的语句，可以使用explain 来分析该语句具体的执行情况。

（7）避免改变索引列的类型。

（8）选择最有效的表名顺序，from字句中写在最后的表是基础表，将被最先处理，在from子句中包含多个表的情况下，你必须选择记录条数最少的表作为基础表。

（9）避免在索引列上面进行计算。

（10）尽量缩小子查询的结果

---

#### 29、超大分页怎么处理？

一般分页查询时，通过创建覆盖索引能够比较好地提高性能。一个常见又非常头疼的问题就是 limit 1000000 , 10，此时需要MySQL排序前1000010 记录，仅仅返回1000000 - 1000010 的记录，其他记录丢弃，查询排序的代价非常大 。

优化思路一：**在索引上完成排序分页操作，最后根据主键关联回原表查询所需要的其他列内容。**

优化思路二：**该方案适用于主键自增的表，可以把limit 查询转换成某个位置的查询 。**

---

#### 30、MySQL数据库作发布系统的存储，一天五万条以上的增量，预计运维三年,怎么优化？

1、设计良好的数据库结构， 允许部分数据冗余， 尽量避免join查询， 提高效率。

2、选择合适的表字段数据类型和存储引擎， 适当的添加索引。

3、MySQL 库主从读写分离。

4、找规律分表， 减少单表中的数据量 ，提高查询速度。

5、添加缓存机制， 比如 memcached， redis等。

6、不经常改动的页面， 生成静态页面。

7、书写高效率的SQL。比如 SELECT * FROM TABEL 改为 SELECT field_1, field_2, field_3 FROM TABLE。

---

#### 31、MySQL中有哪几种锁？

从对数据操作的粒度分 ： 

1） 表锁：操作时，会锁定整个表。

2） 行锁：操作时，会锁定当前操作行。

3） 页面锁：会锁定一部分的数据



从对数据操作的类型分：

1） 读锁（共享锁）：针对同一份数据，多个读操作可以同时进行而不会互相影响。

2） 写锁（排它锁）：当前操作没有完成之前，它会阻断其他写锁和读锁。

---

#### 32、关系数据库中连接池的机制是什么？

前提：为数据库连接建立一个缓冲池。

（1）从连接池获取或创建可用连接

（2）使用完毕之后，把连接返回给连接池

（3）在系统关闭前，断开所有连接并释放连接占用的系统资源

（4）能够处理无效连接，限制连接池中的连接总数不低于或者不超过某个限定值。

其中有几个概念需要大家理解：

最小连接数是连接池一直保持的数据连接。如果应用程序对数据库连接的使用量不大，将会有大量的数据库连接资源被浪费掉。

最大连接数是连接池能申请的最大连接数。如果数据连接请求超过此数，后面的数据连接请求将被加入到等待队列中，这会影响之后的数据库操作。

如果最小连接数与最大连接数相差太大，那么，最先的连接请求将会获利，之后超过最小连接数量的连接请求等价于建立一个新的数据库连接。不过，这些大于最小连接数的数据库连接在使用完不会马上被释放，它将被放到连接池中等待重复使用或是空闲超时后被释放。

上面的解释，可以这样理解：数据库池连接数量一直保持一个不少于最小连接数的数量，当数量不够时，数据库会创建一些连接，直到一个最大连接数，之后连接数据库就会等待。

---

#### 33、SQL 的select 语句完整的执行顺序

SQL Select 语句完整的执行顺序：

（1）from 子句组装来自不同数据源的数据；

（2）where 子句基于指定的条件对记录行进行筛选；

（3）group by 子句将数据划分为多个分组；

（4）使用聚集函数进行计算；

（5）使用 having 子句筛选分组；

（6）计算所有的表达式； 

（7）select 的字段；

（8）使用order by 对结果集进行排序。

---

#### 34、悲观锁和乐观锁怎么实现？ 

**悲观锁：**select...for update是MySQL提供的实现悲观锁的方式。

​		例如：select price from item where id=100 for update

此时在items表中，id为100的那条数据就被我们锁定了，其它的要执行select price from items where id=100 for update的事务必须等本次事务提交之后才能执行。这样我们可以保证当前的数据不会被其它事务修改。MySQL有个问题是select...for update语句执行中所有扫描过的行都会被锁上，因此在MySQL中用悲观锁务必须确定走了索引，而不是全表扫描，否则将会将整个数据表锁住。

**乐观锁：**乐观锁相对悲观锁而言，它认为数据一般情况下不会造成冲突，所以在数据进行提交更新的时候，才会正式对数据的冲突与否进行检测，如果发现冲突了，则让返回错误信息，让用户决定如何去做。

利用数据版本号（version）机制是乐观锁最常用的一种实现方式。一般通过为数据库表增加一个数字类型的 “version” 字段，当读取数据时，将version字段的值一同读出，数据每更新一次，对此version值+1。当我们提交更新的时候，判断数据库表对应记录的当前版本信息与第一次取出来的version值进行比对，如果数据库表当前版本号与第一次取出来的version值相等，则予以更新，否则认为是过期数据，返回更新失败。

举例：

//1: 查询出商品信息

select (quantity,version) from items where id=100;

//2: 根据商品信息生成订单

insert into orders(id,item_id) values(null,100);

//3: 修改商品的库存

update items set quantity=quantity-1,version=version+1 where id=100 and version=#{version};

---

#### 35、你们公司有哪些数据库设计规范

**（一）基础规范**

1、表存储引擎必须使用InnoD，表字符集默认使用utf8，必要时候使用utf8mb4

解读：

（1）通用，无乱码风险，汉字3字节，英文1字节

（2）utf8mb4是utf8的超集，有存储4字节例如表情符号时，使用它

2、禁止使用存储过程，视图，触发器，Event

解读：

（1）对数据库性能影响较大，互联网业务，能让站点层和服务层干的事情，不要交到数据库层

（2）调试，排错，迁移都比较困难，扩展性较差

3、禁止在数据库中存储大文件，例如照片，可以将大文件存储在对象存储系统，数据库中存储路径

4、禁止在线上环境做数据库压力测试

5、测试，开发，线上数据库环境必须隔离

**（二）命名规范**

1、库名，表名，列名必须用小写，采用下划线分隔

解读：abc，Abc，ABC都是给自己埋坑

2、库名，表名，列名必须见名知义，长度不要超过32字符

解读：tmp，wushan谁知道这些库是干嘛的

3、库备份必须以bak为前缀，以日期为后缀

4、从库必须以-s为后缀

5、备库必须以-ss为后缀

**（三）表设计规范**

1、单实例表个数必须控制在2000个以内

2、单表分表个数必须控制在1024个以内

3、表必须有主键，推荐使用UNSIGNED整数为主键

潜在坑：删除无主键的表，如果是row模式的主从架构，从库会挂住

4、禁止使用外键，如果要保证完整性，应由应用程式实现

解读：外键使得表之间相互耦合，影响update/delete等SQL性能，有可能造成死锁，高并发情况下容易成为数据库瓶颈

5、建议将大字段，访问频度低的字段拆分到单独的表中存储，分离冷热数据

**（四）列设计规范**

1、根据业务区分使用tinyint/int/bigint，分别会占用1/4/8字节

2、根据业务区分使用char/varchar

解读：

（1）字段长度固定，或者长度近似的业务场景，适合使用char，能够减少碎片，查询性能高

（2）字段长度相差较大，或者更新较少的业务场景，适合使用varchar，能够减少空间

3、根据业务区分使用datetime/timestamp

解读：前者占用5个字节，后者占用4个字节，存储年使用YEAR，存储日期使用DATE，存储时间使用datetime

4、必须把字段定义为NOT NULL并设默认值

解读：

（1）NULL的列使用索引，索引统计，值都更加复杂，MySQL更难优化

（2）NULL需要更多的存储空间

（3）NULL只能采用IS NULL或者IS NOT NULL，而在=/!=/in/not in时有大坑

5、使用INT UNSIGNED存储IPv4，不要用char(15)

6、使用varchar(20)存储手机号，不要使用整数

解读：

（1）牵扯到国家代号，可能出现+/-/()等字符，例如+86

（2）手机号不会用来做数学运算

（3）varchar可以模糊查询，例如like ‘138%’

7、使用TINYINT来代替ENUM

解读：ENUM增加新值要进行DDL操作

**（五）索引规范**

1、唯一索引使用uniq_[字段名]来命名

2、非唯一索引使用idx_[字段名]来命名

3、单张表索引数量建议控制在5个以内

解读：

（1）互联网高并发业务，太多索引会影响写性能

（2）生成执行计划时，如果索引太多，会降低性能，并可能导致MySQL选择不到最优索引

（3）异常复杂的查询需求，可以选择ES等更为适合的方式存储

4、组合索引字段数不建议超过5个

解读：如果5个字段还不能极大缩小row范围，八成是设计有问题

5、不建议在频繁更新的字段上建立索引

6、非必要不要进行JOIN查询，如果要进行JOIN查询，被JOIN的字段必须类型相同，并建立索引

解读：踩过因为JOIN字段类型不一致，而导致全表扫描的坑么？

7、理解组合索引最左前缀原则，避免重复建设索引，如果建立了(a,b,c)，相当于建立了(a), (a,b), (a,b,c)

**（六）SQL规范**

1、禁止使用select *，只获取必要字段

解读：

（1）select *会增加cpu/io/内存/带宽的消耗

（2）指定字段能有效利用索引覆盖

（3）指定字段查询，在表结构变更时，能保证对应用程序无影响

2、insert必须指定字段，禁止使用insert into T values()

解读：指定字段插入，在表结构变更时，能保证对应用程序无影响

3、隐式类型转换会使索引失效，导致全表扫描

4、禁止在where条件列使用函数或者表达式

解读：导致不能命中索引，全表扫描

5、禁止负向查询以及%开头的模糊查询

解读：导致不能命中索引，全表扫描

6、禁止大表JOIN和子查询

7、同一个字段上的OR必须改写问IN，IN的值必须少于50个

8、应用程序必须捕获SQL异常

解读：方便定位线上问题

说明：本规范适用于并发量大，数据量大的典型互联网业务，可直接参考。

---

#### 36、SQL 语句优化案例

**例1：where 子句中可以对字段进行 null 值判断吗？**

可以，比如 select id from t where num is null 这样的 sql 也是可以的。但是最好不要给数据库留NULL，尽可能的使用 NOT NULL 填充数据库。不要以为 NULL 不需要空间，比如：char(100) 型，在字段建立时，空间就固定了， 不管是否插入值（NULL 也包含在内），都是占用 100 个字符的空间的，如果是 varchar 这样的变长字段，null 不占用空间。可以在 num 上设置默认值 0，确保表中 num 列没有 null 值，然后这样查询：select id from t where num= 0。

**例2：如何优化?下面的语句？**

select * from admin left join log on admin.admin_id = log.admin_id where log.admin_id>10

优化为：select * from (select * from admin where admin_id>10) T1 lef join log on T1.admin_id = log.admin_id。

使用 JOIN 时候，应该用小的结果驱动大的结果（left join 左边表结果尽量小如果有条件应该放到左边先处理， right join 同理反向），同时尽量把牵涉到多表联合的查询拆分多个 query（多个连表查询效率低，容易到之后锁表和阻塞）。

**例3：limit 的基数比较大时使用 between**

例如：select * from admin order by admin_id limit 100000,10

优化为：select * from admin where admin_id between 100000 and 100010 order by admin_id。

**例4：尽量避免在列上做运算，这样导致索引失效**

例如：select * from admin where year(admin_time)>2014

优化为： select * from admin where admin_time> '2014-01-01′

---

