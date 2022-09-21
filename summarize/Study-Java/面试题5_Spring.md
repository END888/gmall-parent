---
title: 面试题5_Spring
date: 2022-08-16
author: yincy
---

### 面试题5_Spring



#### 1、说说你对Spring的理解？

1、Spring是一个为了简化企业级应用开发且非侵入式的轻量级开源框架。Spring主要有两个强大的功能：IOC、AOP  

​	① 控制反转(IOC)：通过工厂模式将我们之前自己手动创建对象的这一过程交给Spring去执行，将创建后的对象都存放到Spring容器中，并进行管理；我们使用时不需要自己去创建，直接从Spring容器中获取对象即可

​	② 依赖注入(DI)：Spring使用Java Bean对象的Set方法或者带参数的构造方法在我们创建对象时，自动从Spring容器中获取所需对象并设置给当前对象的属性。

​	③ 面向切面编程(AOP)：通过代理模式，在程序运行期间动态地将某段代码切入到指定方法的指定位置进行运行的一种编程方式，可以用来做权限验证，事务管理，记录日志等

2、在Spring的容器中管理的都是JavaBean对象，Spring 的 IOC 容器主要有两个，一个是 BeanFactory，还有一个是ApplicationContext，不过我们经常使用的是 ApplicationContext。

---

#### 2、在Spring中有几种配置Bean的方式？

1、基于XML的配置

2、基于注解的配置

3、基于Java的配置

---

#### 3、BeanFactory和ApplicationContext有什么区别？

首先，BeanFactory 和 ApplicationContext 都是 Spring 的两大核心接口，都可以当做 Spring 的容器。

BeanFactory 是 Spring 框架的基础设施，面向 Spring 本身；而 ApplicationContext 面向使用 Spring 框架的开发者，并且Spring官网也是推荐我们使用 ApplicationContext 的（比如可以通过实现 ApplicationContextAware 接口，继承 ApplicationObjectSupport、继承WebApplicationObjectSupport、通过WebApplicationContextUtils 都可以获取到 ApplicationContext）

1、功能上的区别：

BeanFactory 是 Spring 中最底层的接口，是 IOC 的核心，它包含了各种 Bean 的定义、加载、实例化、依赖注入和生命周期的管理，具有 IOC 最基本的功能；而 ApplicationContext 接口是 BeanFactory 的一个子接口，具有 BeanFactory 的所有功能，同时还继承了EnvironmentCapable、ListableBeanFactory、MessageSource、ApplicationEventPublisher、ResourcePatternResolver 接口，功能更加丰富。

2、加载方式的区别：

BeanFactory 是延时加载，也就是说在容器启动时不会实例化 bean，而是在需要使用的时候，才会对该 bean 进行加载实例化；而ApplicationContext 是在容器启动的时候，一次性创建所有的 bean，所以运行的时候速度相对于 BeanFactory 来说比较快；正是因为加载方式的不同，导致 BeanFactory 无法提前发现 Spring存在的配置问题。（如果 bean 的某个属性没有注入，BeanFactory 加载不会抛出异常，直到第一次调用 getBean() 方法时才会抛出异常）但是ApplicationContext 在容器启动时就可以发现 Spring 存在的配置问题，因为它是一次性加载的，有利于检测依赖属性是否注入（同样也是因为它一次性加载的原因，导致占用内存空间，当 bean 较多时，影响程序启动的速度）。

3、创建方式的区别：

BeanFactory 是以编程的方式创建的，而 ApplicationContext 是以声明的方式创建的。

4、注册方式的区别：

BeanFactory 和 ApplicationContext 都支持 BeanPostProcessor、BeanFactoryPostProcessor 的使用，而 BeanFactory 是需要手动注册的，ApplicationContext 是自动注册的。

---

#### 4、Spring框架中的单例bean是线程安全的吗？

不是线程安全的，当多用户同时请求一个服务时，容器会给每一个请求分配一个线程，这时多个线程会并发执行该请求对应的业务逻辑（成员方法），如果该处理逻辑中有对该单例对象状态的修改（比如修改成员属性），则必须考虑线程同步问题。

Spring框架并没有对单例bean进行任何多线程的封装处理。关于单例bean的线程安全和并发问题需要开发者自行去搞定。但实际上，大部分的Spring bean并没有可变的状态(比如Service类和DAO类)，所以在某种程度上说Spring的单例bean是线程安全的。

如果bean有多种状态的话（比如 View Model对象），就需要自行保证线程安全。最浅显的解决办法就是将多态bean的作用域由“**singleton**”变更为“**prototype**”。

---

#### 5、Spring框架中有哪些不同类型的事件？

Spring 提供了以下5种标准的事件：

1、上下文更新事件（ContextRefreshedEvent）：在调用ConfigurableApplicationContext 接口中的refresh()方法时被触发。

2、上下文开始事件（ContextStartedEvent）：当容器调用ConfigurableApplicationContext的start()方法开始/重新开始容器时触发该事件。

3、上下文停止事件（ContextStoppedEvent）：当容器调用ConfigurableApplicationContext的stop()方法停止容器时触发该事件。

4、上下文关闭事件（ContextClosedEvent）：当ApplicationContext被关闭时触发该事件。容器被关闭时，其管理的所有单例Bean都被销毁。

5、请求处理事件（RequestHandledEvent）：在Web应用中，当一个http请求（request）结束触发该事件。

---

#### 6、请解释一下Spring框架有哪些自动装配模式，它们之间有何区别？

1、no：这是 Spring 框架的默认设置，在该设置下自动装配是关闭的，开发者需要自行在 bean 定义中用标签明确的设置依赖关系 。

2、byName：该选项可以根据bean名称设置依赖关系 。 当向一个bean中自动装配一个属性时，容器将根据bean的名称自动在在配置文件中查询一个匹配的bean。 如果找到的话，就装配这个属性，如果没找到的话就报错 。

3、byType：该选项可以根据 bean 类型设置依赖关系 。 当向一个 bean 中自动装配一个属性时，容器将根据 bean 的类型自动在在配置文件中查询一个匹配的 bean。 如果找到的话，就装配这个属性，如果没找到的话就报错 。

4、constructor ：构造器的自动装配和byType模式类似，但是仅仅适用于与有构造器相同参数的bean，如果在容器中没有找到与构造器参数类型一致的bean ，那么将会抛出异常 。

5、default：该模式自动探测使用构造器自动装配或者byType自动装配 。 首先会尝试找合适的带参数的构造器，如果找到的话就是用构造器自动装配，如果在bean内部没有找到相应的构造器或者是无参构造器，容器就会自动选择 byTpe 的自动装配方式 。

---

#### 7、解释一下Spring AOP里面的几个名词？

1、连接点（Join point）：指程序运行过程中所执行的方法。在Spring AOP中，一个连接点总代表一个方法的执行。 

2、切入点（Pointcut）：被拦截用于做增强的那些连接点。

3、通知（Advice）：指要在切入点（Join Point）上执行的动作，即增强的逻辑，比如权限校验和、日志记录等。

4、切面（Aspect）：被抽取出来的公共模块，可以用来横切多个对象。Aspect切面可以看成 Pointcut切点和 Advice通知的结合，一个切面可以由多个切点和通知组成。

5、目标对象（Target）：包含连接点的对象，也称作被通知（Advice）的对象。 由于Spring AOP是通过动态代理实现的，所以这个对象永远是一个代理对象。

6、织入（Weaving）：通过动态代理，在目标对象（Target）的方法（即切入点）中执行增强逻辑（Advice）的过程。

7、引入（Introduction）：添加额外的方法或者字段到被通知的类。Spring允许引入新的接口（以及对应的实现）到任何被代理的对象。

---

#### 8、Spring中AOP的底层是怎么实现的？



> AOP底层为动态代理，AOP指的是：在程序运行期间动态地将某段代码切入到指定方法指定位置进行运行的编程方式。在面向切面编程中，我们将一个个的对象某些类似的方面横向抽取为一个切面，对这个切面进行一些如权限控制、事物管理，记录日志等公用操作处理的过程就是面向切面编程的思想。AOP 底层是动态代理，如果是接口采用JDK 动态代理，如果是类采用CGLIB 实现动态代理

由于我们开发中通常使用的是 AspectJ 的AOP，所以，我就以AspectJ 来进行说明，在使用 AspectJ 的 AOP 的功能的时候，可以通过注解也可以通过 xml 方式开启，由于我们平时中都是以 SpringBoot 注解进行开发的，所以，我就以注解方式来进行说明。

首先需要在配置类上添加 @EnableAspectJ 注解，这个注解呢它又会通过 @Import 注解向容器中导入一个 AspectJAutoProxyRegistrar 组件，而这个组件又会让 Spring 容器中注册一个 AnnotationAwareAspectJAutoProxyCreator 这样一个基于注解驱动的 AspectJ 自动代理创建器的 bean；而这个 AnnotationAwareAspectJAutoProxyCreator 它又间接地继承自 AbstractAutoProxyCreator，AbstractAutoProxyCreator 又实现了 两个接口，一个是 BeanFactoryAware 接口，另外一个就是 SmartInstantiationAwareBeanPostProcessor，而 SmartInstantiationAwareBeanPostProcessor 又继承了 InstantiationAwareBeanPostProcessor 接口，InstantiationAwareBeanPostProcessor 接口又继承了 BeanPostProcessor 接口，这三个接口都会在 Bean 的不同生命周期执行不同的方法，BeanPostProcessor 中有一个 postProcessAfterInitialization() 方法，该方法是在 bean 初始化之后执行的，SmartInstantiationAwareBeanPostProcessor中有 getEarlyBeanReference 方法，该方法会结合 Spring 的第三级缓存 singletonFactories 解决代理对象的循环依赖问题 ，而 AbstractAutoProxyCreator 重写了这两个方法，这两个方法都会返回要创建对象的代理对象，所以在 Spring 容器一启动的时候，会先把 AnnotationAwareAspectJAutoProxyCreator 的 bean 放入 Spring 容器中，之后有对象进行创建的时候，AnnotationAwareAspectJAutoProxyCreator 的方法就会被执行，然后根据要代理的对象创建代理对象，创建代理对象的方式有两种，一种是有接口的情况下，会通过 JDK 动态代理创建代理对象，还有一种是没有接口时会通过 Cglib 动态代理创建代理对象。所以之后我们从 Spring 容器中获取的对象为该对象的代理对象，通过代理对象执行目标方法时，如果是用 Cglib 的方式创建代理对象的话，会被 CglibAopProxy 的 interceptor() 方法所拦截，在该方法中，会获取到目标方法的所对应的拦截器，根据拦截器的执行顺序通过责任链模式进行执行，最终完成 AOP 功能。



---

#### 9、Spring如何管理事务的？

Spring事务管理主要包括3个接口，Spring事务主要由以下三个共同完成的：

1、PlatformTransactionManager：事务管理器，主要用于平台相关事务的管理。

​	 主要包括三个方法：

​	 ① commit：事务提交。

​	 ② rollback：事务回滚。

​	 ③ getTransaction：获取事务状态。

2、TransacitonDefinition：事务定义信息，用来定义事务相关属性，给事务管理器PlatformTransactionManager使用

​	主要包含的方法：

​	 ① getIsolationLevel：获取隔离级别。

​        ② getPropagationBehavior：获取传播行为。

​	 ③ getTimeout获取超时时间。

​	 ④ isReadOnly：是否只读（保存、更新、删除时属性变为false--可读写，查询时为true--只读）

3、TransationStatus：事务具体运行状态，事务管理过程中，每个时间点事务的状态信息。

​	 主要包含的方法：

​	 ① hasSavepoint()：返回这个事务内部是否包含一个保存点。

​	 ② isCompleted()：返回该事务是否已完成，也就是说，是否已经提交或回滚。

​	 ③ isNewTransaction()：判断当前事务是否是一个新事务。

---

#### 10、Spring事务什么情况下会失效？

1、**数据库引擎不支持事务**：这里以 MySQL为例，其MyISAM引擎是不支持事务操作的，InnoDB才是支持事务的引擎，一般要支持事务都会使用 InnoDB。

2、**bean没有被Spring 管理**

3、**方法不是public的**：@Transactional只能用于public的方法上，否则事务会失效。

4、**自身调用问题**

类中没有加@Transactional 注解的方法调用了加了 @Transactional 注解的方法，从而导致加了 @Transactional 注解的方法的事务失效；

解决办法：使用AopContext.currentProxy() 获取代理对象，通过代理对象调用方法

5、**数据源没有配置事务管理器**

6、**异常在方法内部通过try...catch处理掉了**

7、**异常类型错误：事务默认回滚的是：RuntimeException**

---



#### 11、请解释一下Spring Bean的生命周期？

首先会进行 Bean 的实例化，通过反射创建 Bean 对象，然后对 Bean 对象进行属性赋值，接着进行 Bean 的初始化，而在初始化过程中，会先判断当前 Bean 有没有实现相应的 Aware 接口，比如 BeanNameAware、BeanFactoryAware、ApplicationContextAware 等，如果有，则执行相应的方法；接着会调用 Bean 的后置处理器中的 postProcessBeforeInitialization()，然后调用初始化方法，进行初始化，初始化方法可以通过 init-method 或者 @PostConstruct 注解或者实现 InitializingBean 接口，重写afterPropertiesSet() 方法来指定；完了之后，又会执行 Bean 的后置处理器中的 postProcessAfterInitialization() 方法，然后又会判断当前对象是单例还是多例，如果是单例则放入单例缓存池中，如果是多例则返回，接着 Bean 对象被使用，当 Bean 不再需要或者 IOC 容器关闭时，会经过清理阶段，如果 Bean 实现了 DisposableBean 这个接口，那么就会调用其重写的 destroy() 方法，最后，如果这个 Bean 在 Spring 的配置中配置了 destroy-method 属性，指定了销毁的方法时，会自动调用该方法。

---

#### 12、Spring中的AOP是在bean生命周期的哪一步实现的？

在初始化阶段中执行完初始化方法之后

---

#### 13、什么是Spring 循环依赖

BeanA 对象的创建依赖于 BeanB ，而 BeanB 对象的创建也依赖于 BeanA，这样就造成了死循环，如果不做处理的话就会造成栈溢出。

---

#### 14、Spring 的三级缓存

| 名称     | 对象名                | 含义                                                         |
| -------- | --------------------- | ------------------------------------------------------------ |
| 一级缓存 | singletonObjects      | 存放已经经历了完成生命周期的 Bean对象                        |
| 二级缓存 | earlySingletonObjects | 存放早期暴露出来的 Bean 对象，Bean 的生命周期未结束（属性还未完成填充、未完成初始化） |
| 三级缓存 | singletonFactories    | 存放可以生成 Bean 的工厂                                     |

---

#### 15、Spring如何解决循环依赖的？

Spring 中提供了三个map集合用来缓存不同时期的 Bean，分别是一级缓存：singletonObjects，它存储的是完整的 Bean，也就是完成了 Bean 的实例化、属性赋值、初始化；二级缓存：earlyBeanSingletonObjects，它存储的是早期暴露出来的 Bean，虽然完成了 Bean 的实例化，但是还没有进行属性赋值和初始化；三级缓存：singletonFactories，它存储的是根据实例化后的 Bean 构造出来的 ObjectFactory，根据ObjectFactory 可以获取到早期的 Bean 。当 A 中依赖了 B，而 B 又依赖了 A 时，会产生循环依赖；当容器启动的时候会去实例化 A 和 B，首先会先从一级缓存中获取 BeanA，由于是第一次创建，所以会发现获取不到，那么就会将 BeanA 添加到正在创建的集合中，然后进行 BeanA 的实例化，在进行属性赋值之前，会根据实例化完成的 BeanA 构造一个对应的 ObjectFactory，将 ObjectFactory 添加到三级缓存 singletonFactories 中，然后再进行 BeanA 的属性赋值，此时会去一级缓存中获取 BeanB，由于是第一次创建，发现获取不到，那么就会将 BeanB 添加到正在创建的集合中，然后进行 BeanB 的实例化，在进行属性赋值之前，会根据实例化完成的 BeanB 构造一个对应的 ObjectFactory，将 ObjectFactory 添加到 singletonFactories 三级缓存中，然后再进行 BeanB 的属性赋值，此时会去一级缓存中获取 BeanA，而一级缓存中获取不到，由于 BeanA 是正在创建的 Bean，所以又会去二级缓存中获取，二级缓存也获取不到，然后就去三级缓存中获取，在三级缓存中通过 ObjectFactory 获取到了早期的 BeanA 对象，然后把早期的 BeanA 对象放入到二级缓存中，将三级缓存中的 ObjectFactory 移除，把获取到的早期的 BeanA 对象返回，BeanB 根据获取的 BeanA 进行属性赋值、初始化，完了之后把 BeanB 从正在创建的集合中移除，然后把 BeanB 添加到已经创建好的集合中，然后再把三级缓存中 BeanB 所对应的 ObjectFactory 移除，把完整的 BeanB 添加到 singletonObjects 一级缓存中，将 BeanB 返回，BeanA 获取到创建完成的 BeanB 后，完成属性赋值和初始化，再把 BeanA 从正在创建的集合中移除，将 BeanA 添加到已经创建的集合中，最后会把二级缓存中早期的 BeanA 移除，将完整的BeanA 添加到一级缓存中，然后把 BeanA 返回，这样就解决了循环依赖。

---



#### 16、三级缓存的作用：

Spring 的三级缓存的作用就是为了防止在代理对象出现循环依赖时候，导致在给B的A属性进行依赖时注入的A不是A的代理对象，而是A本身，但是最终单例池中存放的是 A 的代理对象，两个对象不是同一个。所以在这种场景下，二级缓存就解决不了，那么这个时候 Spring 就利用第三级缓存 singletonFactories 解决了这个问题。singletonFactories 中存的是根据实例化后的 Bean 构建的 ObjectFactory ，而这个 ObjectFactory 能获取到 早期的 Bean，当调用 getObject() 方法的时候，就会调用 getEarlyBeanReference() ，而在 AOP场景下，会向容器中注入 AnnotationAwareAspectJAutoProxyCreator ，它的父类 AbstractAutoProxyCreator 又重写了 SmartInstantiationAwareBeanPostProcessor 的 getEarlyBeanReference()，所以就会在 getEarlyBeanReference() 方法中创建指定对象的代理对象。当通过三级缓存中的 ObjectFactory 获取对象时，获取的就是 bean 的代理对象，然后将获取到的代理对象放入到二级缓存中，之后会把三级缓存中对应的的 ObjectFactory 移除，所以当 BeanB 拿到 BeanA 的代理对象之后，将代理对象 BeanA 设置给 BeanB ，那么 BeanA 在对象初始化的时候，发现 BeanA 已经创建了代理对象了，此时就不会再次创建代理对象了，而是将代理对象 A 直接返回，这样一来就保证了 BeanB 的 A 属性和容器中一级缓存中缓存的 Bean 对象都是 AOP 代理之后的同一个代理对象了。

---

#### 17、只有一级缓存和三级缓存是否可行？

如果没有 AOP 的话确实可以实现一级和三级缓存来解决循环依赖的问题，但是如果加上 AOP 的话，在AOP 场景下，Spring 容器中会注入一个 AnnotationAwareAspectJAutoProxyCreator 这个组件，而这个组件的父类 AbstractAutoProxyCreator 重写了 SmartInstantiationAwareBeanPostProcessor 的 getEarlyBeanReference() 方法，这个方法会返回指定对象的代理对象，而在三级缓存中调用 ObjectFactory 的 getObject() 方法的时候，会调用 getEarlyBeanReference() 方法，此时就会调用  AnnotationAwareAspectJAutoProxy  中重写的 getEarlyBeanReference() 方法，所以获取的对象其实是该对象的代理对象，而如果没有二级缓存的话，就会导致获取的这个代理对象没有地方放，从而导致每次获取时都会产生一个新的代理对象，所以为了保证始终只有一个代理对象，就需要二级缓存来保存这个代理对象。

---

#### 18、构造方法出现了循环依赖怎么解决？

在构造参数前面加了@Lazy注解之后, 就不会真正的注入真实对象, 该注入对象会被延迟加载 ， 此时注入的是一个代理对象 。



---

#### 19、为什么构造器注入属性无法解决循环依赖问题

这是因为 Spring 中 Bean 的创建过程为先实例化，再进行属性赋值，然后再执行初始化；当使用构造器注入属性的时候，是在该对象进行实例化的时候执行的，但是实例化没有完成，就没有可以暴露的早期 Bean 对象供依赖的那个对象进行实例化，所以就会陷入死循环的状态。

---

#### 20、一级缓存能不能解决循环依赖问题

不能，因为这三个级别的缓存中存储的对象时有区别的，一级缓存中存的是完整的对象，该对象完成了 Bean 的实例化、属性赋值和初始化；二级缓存存储的是早期的对象，也就是虽然进行了实例化，但是还没有完成属性赋值和初始化的；三级缓存中存储的是根据实例化后的 Bean 构造的 ObjectFactory，可以根据 ObjectFactory 可以获取到早期的 Bean 对象；如果只有一级缓存，那么有可能在多线程并发环境下，获取到实例化但还没有完成属性赋值和初始化的对象，此时就会出现问题。

---

#### 21、二级缓存能不能解决循环依赖问题

答法同上面：三级缓存的作用一样

---

#### 22、Spring事务的实现方式以及原理？

Spring 支持编程式事务管理和声明式事务管理；因为编程式事务管理需要通过 TransactionTemplate 来进行实现，而这种方式对业务代码有侵入性，因此在实际开发中很少用到；而声明式事务管理是通过 AOP 实现的，就是执行目标方法的时候，对目标方法进行拦截，将事务处理的功能织入到拦截的方法中，在目标方法开始之前加入一个事务，在执行完目标方法之后根据执行情况进行提交或者回滚。而声明式事务最大的优点在于不需要在业务逻辑代码中编写事务管理的代码，只需要在配置文件中进行相应的事务配置或者通过@Transactional 注解就可以实现事务管理

---

#### 23、Spring 用到了哪些设计模式

1、简单工厂模式：BeanFactory 就是简单工厂模式的体现，根据传入一个唯一标识来获得 bean 对象。

2、工厂方法模式：FactoryBean 就是典型的工厂方法模式。Spring 在使用 getBean() 调用获得该 Bean 时，会自动调用该 Bean 的 getObject() 方法，每个 Bean 都会对应一个 FactoryBean，如 SqlSessionFactory 对应 SqlSessionFactoryBean。

3、单例模式：一个类仅有一个实例，Spring 创建 Bean 实例默认是单例的，当然也可以设置它的 prototype 来该为非单例。

4、适配器模式：SpringMVC 中的适配器 HandlerAdapter；

5、代理模式：Spring 中的 AOP 使用了动态代理，有两种方式：JDK 动态代理、Cglib 动态代理

6、观察者模式：Spring 中的 ApplicationListener

7、模板方法：Spring 容器启动过程中执行 refresh() 方法

9、责任链模式：AOP 在执行目标方法时，执行拦截器链；还有 SpringMVC 中的拦截器

---

#### 24、IOC 容器的初始化流程

1、根据 ResourceLoader 资源加载器将 Bean 的配置信息加载进来（可以是注解、xml、网络等）；

2、通过 BeanDefinitionReader 将配置信息解析成对应的 BeanDefinition；

3、将 BeanDefinition 保存到 DefaultListableBeanFactory 的 beanDefinitionMap 中；

4、注册主配置类

5、refresh（）

1. 准备上下文环境
2. 获取准备好的容器
3. 给工厂里面设置好必要的工具（el表达式解析器、资源解析器、基本的后置处理器等）
4. 后置处理 bean 工厂（子类进行实现）
5. 执行工厂的后置增强
6. 注册 bean 的后置处理器
7. 初始化国际组件
8. 初始化事件多播器
9. 留给子类继续增强处理逻辑
10. 注册监听器
11. 完成工厂初始化
12. 最后的一些清理、事件发送等

---

#### 25、BeanFactory 和 FactoryBean 的区别

BeanFactory 是 Spring 的容器，它包含了 Bean 的定义、加载、实例化、依赖注入、生命周期管理；FactoryBean 是用来创建比较复杂的 Bean，可以通过实现 FactoryBean 重写 getObject() 方法，在 getObject() 方法中向 Spring 容器注入组件，而通过 getBean() 方法从 Spring 容器获取的对象不是 FactoryBean 对象，而是在 getObject() 方法中返回的对象，相当于是 getObejct() 方法代理了 getBean() 方法，如果想得到 FactoryBean  对象，必须使用 "&" + beanName 的方式获取。

---

#### 26、有哪些事务传播行为

| **事务传播行为类型** | **说明**                                                     |
| -------------------- | ------------------------------------------------------------ |
| required             | 支持当前事务，如果不存在，就新建一个。默认                   |
| supports             | 支持当前事务，如果不存在，就不使用事务                       |
| mandatory            | 支持当前事务，如果不存在，抛出异常                           |
| requires_new         | 如果有事务存在，挂起当前事务，创建一个新的事务               |
| not_supported        | 以非事务方式执行操作，如果当前存在事务，就把事务挂起。       |
| never                | 以非事务方式执行，如果当前存在事务，则抛出异常。             |
| nested               | 如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则执行与REQUIRED类似的操作。（外层事务抛出异常回滚，那么内层事务必须回滚，反之内层事务并不影响外层事务） |

