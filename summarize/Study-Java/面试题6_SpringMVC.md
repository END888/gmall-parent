---
title: 面试题6_SpringMVC
date: 2022-08-16
author: yincy
---

### 面试题6_SpringMVC



---

#### 1、SpringMVC 的工作流程

1. 用户发送请求到服务器，请求被 SpringMVC 的前端控制器(DispatcherServlet) 截获

2. DispatcherServlet对请求的URL（统一资源定位符）进行解析，得到URI(请求资源标识符)，然后根据 URI 去寻找映射路径
   1. 可以找到，进行下一个环节

   2. 找不到，判断是否配置了 mvc:default-servlet-handler/
      1. 配置了
         1. 根据 uri 去寻找静态资源
            1. 找到了，直接访问
            2. 找不到，依然是 404
      2. 没有配置，报 404 的异常

3. 然后根据URL，调用HandlerMapping获得该Handler配置的所有相关的对象，包括Handler对象以及Handler对象对应的拦截器，这些对象都会被封装到一个HandlerExecutionChain对象当中返回

4. 根据 HandlerExecutionChain 获取Handler 所对应的 HandlerAdapter；

5. 开始执行拦截器的 preHandle() （多个拦截器是正向循环调用）

6. 通过HandlerAdapter的 handle 方法执行Handler(Controller)

   1. 调用RequestMappingHandlerAdapter的invokeHandlerMethod():
      1. 处理器适配器在处理请求前的工作:
         - WebDataBinderFactory binderFactory = getDataBinderFactory(handlerMethod): 获取数据绑定工厂，用来做数据绑定(请求参数与POJO的绑定)

         - ModelFactory modelFactory = getModelFactory(handlerMethod, binderFactory): 用于初始化Model，设置方法的参数解析器、返回值解析器

         - 在填充Handler的入参过程中，根据配置，spring将帮助做一些额外的工作

           消息转换：将请求的消息，如json、xml等数据转换成一个对象，将对象转换为指定的响应信息。

           数据转换：对请求消息进行数据转换，如String转换成Integer、Double等。 

           数据格式化：对请求的消息进行数据格式化，如将字符串转换为格式化数字或格式化日期等。

           数据验证：验证数据的有效性如长度、格式等，验证结果存储到BindingResult或Error中。

      2. 如果没有异常，则正常返回ModelAndView，对象中应该包含视图名或视图模型
      3. 如果出现异常，返回的ModelAndView是null

7. 如果Controller有异常的话，会在 catch  中产生一个异常对象，拦截器的 postHandle不会执行，它只有没有异常才会执行(多个拦截器是逆向循环调用)

8. 前端控制器会去寻找视图解析器(ThymeleafViewResolver),进行视图解析

9. 就会解析出一个View对象(ThymeleafView),对View对象做渲染工作

10. 执行拦截器的afterCompletion（多个拦截器是逆向循环调用）

11. 将视图给浏览器展示

---

#### 2、SpringMVC 的主要组件

1、前端控制器（DispatcherServlet）：接收用户请求，给用户返回结果

2、处理器映射器（HandlerMapping）：根据请求的url路径，通过注解或者 xml 配置，寻找匹配的 Handler

3、处理器适配器（HandlerAdapter）：Handler 的适配器，调用 handler 的方法处理请求

4、处理器（Handler）：执行相关的请求处理逻辑，并返回相应的数据和视图信息，将其封装到 ModelAndView 对象中

5、视图（View）：接口类，实现类可支持不同的 View 类型（JSP、FreeMarker、Excel 等）

---

#### 3、SpringMVC 中常用的注解有哪些

1、@Controller：用于标识此类是一个控制器

2、@RequestBody：标注在方法上表示该方法的返回值会直接返回给请求的发送者；标注在类上，表示该类的所有方法的返回值都会直接返回给请求的发送者。

3、@RestController：是 @Controller和 @RequestBody 的合成注解

4、@RequestMapping：声明在方法上，表示该方法能够处理对应的请求；声明在类上，表示该类能够处理对应的请求，但具体还是看类中的方法

5、@RequestParam：将请求中的请求参数绑定到对应的方法的参数上

6、@PathVariable：将路径上对应的路径参数绑定到对应的方法的参数上

7、@ControllerAdvice：声明在类上，表示该类用来进行全局的异常处理，处理后的返回结果会转为 json 响应

8、@ExceptionHandler：作用在方法上，用来指定该方法能捕获的哪些异常类型

---

#### 4、SpringMVC 的拦截器和过滤器的区别

1、过滤器是 servlet 容器中的对象，拦截器是 SpringMVC 容器的对象

2、过滤器是实现 Filter 接口，拦截器是实现 HandlerInterceptor 接口

3、过滤器是用来设置 request、response 参数、属性、修改字符编码，侧重于对数据的过滤；拦截器是用来验证请求的，能截断请求

4、过滤器是在拦截器之前执行的

5、过滤器有一个过滤方法，而拦截器有三个拦截方法

6、过滤器可以处理 jsp、js、html 等；拦截器是侧重拦截 Controller 的对象，如果你的请求不能被 DispatcherServlet 接收，这个请求不会执行拦截器的内容

7、拦截器拦截普通方法执行，过滤器过滤 servlet 请求响应

8、过滤器是基于函数回调实现的；拦截器是基于 Java 的反射机制，属于面向切面编程（AOP）的一种运用

---

#### 5、什么是REST

通过 URL 就知道用户要什么资源，通过 HTTP method 就知道要干什么，通过 HTTP status code 就知道结果怎么样

---

#### 6、REST 的优势

1、风格统一，不会出现各种命名不规范的代码

2、面向资源，一目了然

3、充分利用 HTTP 协议本身语义

---

#### 7、SpringMVC 的 Controller 是否是线程安全的？如何解决？

Controller 默认是单例对象，在多线程环境下，全局变量会出现线程安全问题

解决方案：

1、将全局变量都转为局部变量，通过方法参数来传递

2、使用 ThreadLocal

3、将Controller 的作用域从单例改为原型

---

#### 8、SpringMVC 如何设定重定向和转发

1、在返回值前加 "forward" 就可以实现转发

2、在返回值前面加 "redirect:" 就可以实现重定向 

---

#### 9、SpringMVC 怎么和 Ajax 相互调用的

1、导入 JSON 数据转换工具（比如 jackson）

2、开启 mvc 的注解驱动

3、在 Controller 方法上添加 @ResponseBody 注解，然后直接返回给前端要响应的对象数据

---

#### 10、SpringMVC 如何完成文件上传

1、前端：

1. 请求方式必须是 POST
2. 表单的 "enctype=multipart/form-data"
3. 表单中至少要有一个文件上传表单项 file

2、后端接收：

1. 导入 commons-fileupload
2. 配置文件上传解析器
3. 在 Controller 方法的形参中定义 MultipartFile，形参名必须和要上传的文件表单的 name 属性值一致（当然也可以通过 @RequestParam、@RequestPart 指定 name 的属性值）

---

#### 11、SpringMVC 如何获取 request、response、session、cookie

1、在 Controller 方法的形参中直接定义 HttpServletRequest、HttpServletResponse、HttpSession；像 cookie 这种在请求头中的数据可以通过 @RequestHeader、@CookieValue 获取

2、通过 @Autowired 注入 HttpServletRequest、HttpServletResponse、HttpSession

---

#### 12、SpringMVC 如何处理异常

可以直接使用 SpringMVC 中的全局异常处理器对异常进行统一处理（@ControllerAdvice、@ExceptionHandler）

