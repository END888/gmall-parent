---
title: 面试题7_SpringBoot
date: 2022-08-16
author: yincy
---

### 面试题7_SpringBoot

---



#### 1、SpringBoot 的自动装配原理

在 SpringBoot 项目的主启动类的 main() 方法中，通过 SpringApplication.run() 方法在运行 Spring 容器时，会创建一个 SpringApplication 对象，在构造方法中会根据上下文初始化器获取 SpringFactories 实例，而在获取时，又会通过类加载器去加载 SpringFactories 声明的全类名，而 SpringFactories 的资源路径就是 SpringFactoriesLoader 类中定义的 factories_recourse_location 即："META-INF/spring.factories"；在 spring-boot-autoconfiguration 包下的 "META-INF/spring.factories" 中有一个 EnableAutoConfiguration，它里面就定义了许多自动配置的全类名，就比如我们常见的：RedisAutoConfiguration、ElasticsearchDataAutoConfiguration、AopAutoConfiguration、RabbitAutoConfiguration等。而这些自动配置类中又会通过 @Import 导入一些其他相关的组件，比如 RedisAutoConfiguration 自动配置类中又通过 @Import 导入了 Lettuce、Jedis 的连接配置，也会通过 @EnableConfigurationProperties 注解将用到的配置类注入进来，比如 RedisProperties，它上面就通过 @ConfigurationProperties 注解指定配置属性，所以我们在进行使用某个场景的时候，就只需要在配置文件中配置就好了，它会自动将配置绑定到配置类中；当然也会通过像 @Conditional 注解让某些组件在指定场景下生效，就比如 @ConditionalOnMissingBean，如果我们引入了某个场景启动器，然后又自己配置了相关的组件，那么 @ConditionalOnMissingBean 注解就不会将容器中已经存在的组件，而是使用我们自己定义的。当然，我们也可以自定义 starter 实现自动装配。



---

#### 2、SpringBoot 提供了哪些核心功能

1、项目以 jar 包方式运行

通过引入 spring-boot-maven-plugin 插件可以将 springboot 项目打包成一个可以直接运行的 jar 包，运行方式和常规 jar 包一样：

```shell
java -jar xxx.jar
```

启动后可以直接运行内嵌的 web 容器（tomcat、jetty），根据具体引入的依赖来确定到底该启动哪种 web 容器

2、使用 starter 依赖

使用 starter来封装依赖，简化项目引入相关依赖的复杂度

3、自动配置

---

#### 3、SpringBoot 中的 starter 是什么

每一个 starter 就是一个对应不同场景的启动器，它里面包含了该场景下相关的所有依赖，不用再关系各个依赖的依赖关系，比如想要使用 redis，就只需要引入 redis 对应的 starter 即可。

---

#### 4、SpringBoot 如何定义多套不同的环境配置

提供多套配置文件，根据规范对配置文件进行命名，然后在 application.properties 文件中指定当前环境 spring.profiles.acticve=test，这个时候读取的就是 application-test.properties 文件。

---

#### 5、SpringBoot 的核心注解都是啥

1、@SpringBootApplication：标注在启动类上

2、@SpringBootConfiguration：组合 @Configuration ，可以作为配置类

3、@EnableAutoConfiguration：启用 Spring 的启动配置，当然也可以通过 exclude 属性来关闭某个自动配置，比如关闭数据源的自动配置

4、@ComponentScan：Spring 组件扫描

---

#### 6、SpringBoot 有哪几种运行方式

1、直接通过 java -jar xxx.jar 运行

2、将 xxx.jar 制作成 Docker 镜像，然后通过 Docker容器运行，当然也可以使用 docker-compose 对多个容器进行统一编排

---

#### 7、SpringBoot 打成的 jar 和普通的 jar 有什么区别

SpringBoot 项目打成的 jar 是可执行的 jar，可以直接通过 java -jar xxx.jar 命令来运行，但是不可以作为普通 jar 被其他项目依赖，即使依赖了也无法使用里面的类；因为它和普通的 jar 结构不同，普通的 jar 包，解压后直接就是包名，包里面就是写的代码，而 SpringBoot 打的 jar 在解压后，在 \BOOT-INF\classes 目录下才是我们的代码，所以没有办法被直接引用。

---

#### 8、SpringBoot 中如何

实现定时任务

1、Spring Task

在启动类上添加 @EnableScheduling 开启定时任务支持，然后在 指定的方法上使用 @Scheduled 来指定定时任务的执行规则

2、Quartz（不推荐使用）

---

#### 9、谈谈怎么理解 SpringBoot 框架？

Spring Boot 是 Spring 开源组织下的子项目，是 Spring 组件一站式解决方案，主要是简化了使用 Spring 的难度，简省了繁重的配置，提供了各种启动器，开发者能快速上手。



**Spring Boot的优点**

l、独立运行

Spring Boot而且内嵌了各种servlet容器，Tomcat、Jetty等，现在不再需要打成war包部署到容器中，Spring Boot只要打成一个可执行的jar包就能独立运行，所有的依赖包都在一个jar包内。

2、简化配置

spring-boot-starter-web启动器自动依赖其他组件，简少了maven的配置。除此之外，还提供了各种启动器，开发者能快速上手。

3、自动配置

Spring Boot能根据当前类路径下的类、jar包来自动配置bean，如添加一个spring-boot-starter-web启动器就能拥有web的功能，无需其他配置。

4、无代码生成和XML配置

Spring Boot配置过程中无代码生成，也无需XML配置文件就能完成所有配置工作，这一切都是借助于条件注解完成的，这也是Spring4.x的核心功能之一。

5、应用监控

Spring Boot提供一系列端点可以监控服务及应用，做健康检测。

**Spring Boot缺点：**

Spring Boot虽然上手很容易，但如果你不了解其核心技术及流程，所以一旦遇到问题就很棘手，而且现在的解决方案也不是很多，需要一个完善的过程。



---

#### 10、SpringBoot 与 SpringCloud 的关系

Spring Boot 是 Spring 的一套快速配置脚手架，可以基于Spring Boot 快速开发单个微服务，Spring Cloud是一个基于Spring Boot实现的开发工具；Spring Boot专注于快速、方便集成的单个微服务个体，Spring Cloud关注全局的服务治理框架； Spring Boot使用了默认大于配置的理念，很多集成方案已经帮你选择好了，能不配置就不配置，Spring Cloud很大的一部分是基于Spring Boot来实现，必须基于Spring Boot开发。

 可以单独使用Spring Boot开发项目，但是Spring Cloud离不开 Spring Boot。

---

