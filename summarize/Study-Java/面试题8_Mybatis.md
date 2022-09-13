---
title: 面试题8_Mybatis
date: 2022-08-16
author: yincy
---

### 面试题8_Mybatis



---

#### 1、Mybatis 中 #{} 与 ${} 的区别

\#{}是预编译处理，${}是字符串替换；

Mybatis在处理#{}时，会将sql中的#{}替换为?号，调用PreparedStatement的set方法来赋值；

Mybatis在处理${}时，就是把${}替换成变量的值；

使用#{}可以有效的防止SQL注入，提高系统安全性。

---

#### 2、当实体类中的属性名和表中的字段名不一样怎么办

1、在查询时的 SQL 中通过设置查询字段的别名来保持一致

2、使用 resultMap 来定义字段和属性的映射关系

---

#### 3、Mybatis 是如何进行分页的？分页插件的原理是什么？

分页方式：

1、在执行 SQL 语句的时候直接拼接分页参数

2、使用 Mybatis 的分页插件（PageHelper）进行分页

原理：

Mybatis 的分页插件就是通过 JDK 动态代理对目标对象进行增强，PageHelper 实现了 Interceptor 拦截器接口，并拦截 Executor 的 query 方法，在执行前 PageHelper 会对原始 SQL 进行重写 SQL添加对应的物理分页语句和物理分页参数

---

#### 4、如何执行批量插入数据

1、mapper 的接口方法的参数需要定义为集合类型

2、在映射文件中通过 foreach标签遍历集合，获取每一个元素作为 insert 语句的参数值

---

#### 5、xml 映射文件中，除了常见的 select、insert、update、delete 标签之外，还有哪些标签

动态SQL主要是来解决查询条件不确定的情况，在程序运行期间，根据提交的条件动态的完成查询

常用的标签:

<if> : 进行条件的判断

<where>：在<if>判断后的SQL语句前面添加WHERE关键字，并处理SQL语句开始位置的AND 或者OR的问题

<trim>：可以在SQL语句前后进行添加指定字符 或者去掉指定字符.

<set>:  主要用于修改操作时出现的逗号问题

<choose> <when> <otherwise>：类似于java中的switch语句.在所有的条件中选择其一

<foreach>：迭代操作

---

#### 6、Mybatis 实现一对一有几种方式？

1、联合查询：将几个表联合查询，只查询一次；在 resultMap 中使用 association 标签，再在 association 标签中进行关联数据映射

2、嵌套查询：先查询一个表，再根据查询到的结果再去另外一张表中查；在 resultMap 中使用 association 标签，然后在 association  标签的 select 属性中指定另一个查询语句。

---

#### 7、Mybatis 是否支持延迟加载？它的实现原理？

Mybatis 仅支持 association 关联对象和 collection 关联集合对象的延迟加载；association 指的就是一对一，collection 指的就是一对多。可以设置 lazyLoadingEnabled=true 来开启延迟加载，默认情况下延迟加载时关闭的。

实现原理：

使用 Cglib 创建目标对象的代理对象，当调用目标对象方法时，进入拦截器方法，比如调用 order.getUser().getUserName()，拦截器 invoke() 方法发现 order.getUser() 是 null，那么就会单独发送事先保存好的查询关联 User 对象的 SQL，把 User 查询出来，然后调用 Order.setUser(user)，于是 order 的对象 user 属性就有值了，接着完成 order.getUser().getUserName() 方法的调用。

---

#### 8、什么是 Mybatis 的一级缓存和二级缓存

一级缓存是使用在同一个 SqlSession 中，默认是开启的；

二级缓存是使用在不同的 SqlSession，但是是同一个 SqlSessionFactory，默认是关闭的，要进行二级缓存的 POJO 类需要实现 Serializable 接口，当 SQLsession 关闭或者提交的时候，一级缓存中的内容会被存入二级缓存

1、一级缓存失效的情况

- 不是同一个SqlSession(因为一级缓存只能用在同一个SqlSession中)
- 同一个SqlSession但是查询条件发生了变化
- 同一个SqlSession两次查询期间执行了任何一次增删改操作,那么会改变缓存的数据
- 同一个SqlSession两次查询期间手动清空了缓存: 调用sqlSession的clearCache()方法
- 同一个SqlSession两次查询期间提交了事务: 调用sqlSession的commit()方法

---

#### 9、Mybatis 的工作原理

1、读取 MyBatis 配置文件：mybatis-config.xml 为 MyBatis 的全局配置文件，配置了 MyBatis 的运行环境等信息，例如数据库连接信息。

2、加载映射文件。映射文件即 SQL 映射文件，该文件中配置了操作数据库的 SQL 语句，需要在MyBatis 配置文件 mybatis-config.xml 中加载。mybatis-config.xml 文件可以加载多个映射文件，每个文件对应数据库中的一张表。

3、构造会话工厂：通过 MyBatis 的环境等配置信息构建会话工厂 SqlSessionFactory。

4、创建会话对象：由会话工厂创建 SqlSession 对象，该对象中包含了执行 SQL 语句的所有方法。

5、Executor执行器：MyBatis底层定义了一个Executor 接口来操作数据库，它将根据 SqlSession 传递的参数动态地生成需要执行的 SQL 语句，同时负责查询缓存的维护。

6、MappedStatement 对象：在 Executor 接口的执行方法中有一个 MappedStatement 类型的参数，该参数是对映射信息的封装，用于存储要映射的 SQL 语句的 id、参数等信息。

7、输入参数映射：输入参数类型可以是 Map、List 等集合类型，也可以是基本数据类型和 POJO 类型。输入参数映射过程类似于 JDBC 对 preparedStatement 对象设置参数的过程。

8、输出结果映射：输出结果类型可以是 Map、 List 等集合类型，也可以是基本数据类型和 POJO 类型。输出结果映射过程类似于 JDBC 对结果集的解析过程。

---

#### 10、MyBatis 如何获取自动生成的主键值？

在<insert>标签中使用 useGeneratedKeys   和  keyProperty 两个属性来获取自动生成的主键值

---

