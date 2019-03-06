![](https://ws3.sinaimg.cn/large/006tKfTcly1fqrle104hwj31i6104aig.jpg)

## 前言

本文接着上文[应用限流](http://crossoverjie.top/2017/08/11/sbc4/)进行讨论。

之前谈到的限流方案只能针对于单个 JVM 有效，也就是单机应用。而对于现在普遍的分布式应用也得有一个分布式限流的方案。

基于此尝试写了这个组件：

[https://github.com/crossoverJie/distributed-redis-tool](https://github.com/crossoverJie/distributed-redis-tool)


## DEMO

以下采用的是

[https://github.com/crossoverJie/springboot-cloud](https://github.com/crossoverJie/springboot-cloud)

来做演示。

在 Order 应用提供的接口中采取了限流。首先是配置了限流工具的 Bean:

```java
@Configuration
public class RedisLimitConfig {


    @Value("${redis.limit}")
    private int limit;


    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    @Bean
    public RedisLimit build() {
        RedisClusterConnection clusterConnection = jedisConnectionFactory.getClusterConnection();
        JedisCluster jedisCluster = (JedisCluster) clusterConnection.getNativeConnection();
        RedisLimit redisLimit = new RedisLimit.Builder<>(jedisCluster)
                .limit(limit)
                .build();

        return redisLimit;
    }
}
```

接着在 Controller 使用组件：

```java
    @Autowired
    private RedisLimit redisLimit ;

    @Override
    @CheckReqNo
    public BaseResponse<OrderNoResVO> getOrderNo(@RequestBody OrderNoReqVO orderNoReq) {
        BaseResponse<OrderNoResVO> res = new BaseResponse();

        //限流
        boolean limit = redisLimit.limit();
        if (!limit){
            res.setCode(StatusEnum.REQUEST_LIMIT.getCode());
            res.setMessage(StatusEnum.REQUEST_LIMIT.getMessage());
            return res ;
        }

        res.setReqNo(orderNoReq.getReqNo());
        if (null == orderNoReq.getAppId()){
            throw new SBCException(StatusEnum.FAIL);
        }
        OrderNoResVO orderNoRes = new OrderNoResVO() ;
        orderNoRes.setOrderId(DateUtil.getLongTime());
        res.setCode(StatusEnum.SUCCESS.getCode());
        res.setMessage(StatusEnum.SUCCESS.getMessage());
        res.setDataBody(orderNoRes);
        return res ;
    }
    
```

为了方便使用，也提供了注解:

```java
    @Override
    @ControllerLimit
    public BaseResponse<OrderNoResVO> getOrderNoLimit(@RequestBody OrderNoReqVO orderNoReq) {
        BaseResponse<OrderNoResVO> res = new BaseResponse();
        // 业务逻辑
        return res ;
    }
```
该注解拦截了 http 请求，会再请求达到阈值时直接返回。

普通方法也可使用:

```java
@CommonLimit
public void doSomething(){}
```

会在调用达到阈值时抛出异常。

为了模拟并发，在 [User](https://github.com/crossoverJie/springboot-cloud/blob/master/sbc-user/user/src/main/java/com/crossoverJie/sbcuser/controller/UserController.java#L72-L91) 应用中开启了 10 个线程调用 Order(**限流次数为5**) 接口(也可使用专业的并发测试工具 JMeter 等)。



```java
    @Override
    public BaseResponse<UserResVO> getUserByFeign(@RequestBody UserReqVO userReq) {
        //调用远程服务
        OrderNoReqVO vo = new OrderNoReqVO();
        vo.setAppId(1L);
        vo.setReqNo(userReq.getReqNo());

        for (int i = 0; i < 10; i++) {
            executorService.execute(new Worker(vo, orderServiceClient));
        }

        UserRes userRes = new UserRes();
        userRes.setUserId(123);
        userRes.setUserName("张三");

        userRes.setReqNo(userReq.getReqNo());
        userRes.setCode(StatusEnum.SUCCESS.getCode());
        userRes.setMessage("成功");

        return userRes;
    }
    

    private static class Worker implements Runnable {

        private OrderNoReqVO vo;
        private OrderServiceClient orderServiceClient;

        public Worker(OrderNoReqVO vo, OrderServiceClient orderServiceClient) {
            this.vo = vo;
            this.orderServiceClient = orderServiceClient;
        }

        @Override
        public void run() {

            BaseResponse<OrderNoResVO> orderNo = orderServiceClient.getOrderNoCommonLimit(vo);
            logger.info("远程返回:" + JSON.toJSONString(orderNo));

        }
    }    
```

> 为了验证分布式效果启动了两个 Order 应用。

![](https://ws1.sinaimg.cn/large/006tKfTcly1fqrnxt2l8lj313x09rwfm.jpg)

效果如下：
![](https://ws1.sinaimg.cn/large/006tKfTcly1fqrlvvj8cbj31kw0f1wws.jpg)


![](https://ws4.sinaimg.cn/large/006tKfTcly1fqrlznycdnj31kw0gbh0n.jpg)

![](https://ws1.sinaimg.cn/large/006tKfTcly1fqrm0jpbjjj31kw04wgq9.jpg)


## 实现原理
实现原理其实很简单。既然要达到分布式全局限流的效果，那自然需要一个第三方组件来记录请求的次数。

其中 Redis 就非常适合这样的场景。

- 每次请求时将当前时间(精确到秒)作为 Key 写入到 Redis 中，超时时间设置为 2 秒，Redis 将该 Key 的值进行自增。
- 当达到阈值时返回错误。
- 写入 Redis 的操作用 Lua 脚本来完成，利用 Redis 的单线程机制可以保证每个 Redis 请求的原子性。

Lua 脚本如下:

```lua
--lua 下标从 1 开始
-- 限流 key
local key = KEYS[1]
-- 限流大小
local limit = tonumber(ARGV[1])

-- 获取当前流量大小
local curentLimit = tonumber(redis.call('get', key) or "0")

if curentLimit + 1 > limit then
    -- 达到限流大小 返回
    return 0;
else
    -- 没有达到阈值 value + 1
    redis.call("INCRBY", key, 1)
    redis.call("EXPIRE", key, 2)
    return curentLimit + 1
end
```

Java 中的调用逻辑:

```java
    public boolean limit() {
        String key = String.valueOf(System.currentTimeMillis() / 1000);
        Object result = null;
        if (jedis instanceof Jedis) {
            result = ((Jedis) this.jedis).eval(script, Collections.singletonList(key), Collections.singletonList(String.valueOf(limit)));
        } else if (jedis instanceof JedisCluster) {
            result = ((JedisCluster) this.jedis).eval(script, Collections.singletonList(key), Collections.singletonList(String.valueOf(limit)));
        } else {
            //throw new RuntimeException("instance is error") ;
            return false;
        }

        if (FAIL_CODE != (Long) result) {
            return true;
        } else {
            return false;
        }
    }
```

所以只需要在需要限流的地方调用该方法对返回值进行判断即可达到限流的目的。

当然这只是利用 Redis 做了一个粗暴的计数器，如果想实现类似于上文中的令牌桶算法可以基于 Lua 自行实现。


### Builder 构建器

在设计这个组件时想尽量的提供给使用者清晰、可读性、不易出错的 API。

> 比如第一步，如何构建一个限流对象。

最常用的方式自然就是构造函数，如果有多个域则可以采用重叠构造器的方式:

```java
public A(){}
public A(int a){}
public A(int a,int b){}
```

缺点也是显而易见的：如果参数过多会导致难以阅读，甚至如果参数类型一致的情况下客户端颠倒了顺序，但不会引起警告从而出现难以预测的结果。

第二种方案可以采用 JavaBean 模式，利用 `setter` 方法进行构建:

```java
A a = new A();
a.setA(a);
a.setB(b);
```

这种方式清晰易读，但却容易让对象处于不一致的状态，使对象处于线程不安全的状态。

所以这里采用了第三种创建对象的方式，构建器：

```java
public class RedisLimit {

    private JedisCommands jedis;
    private int limit = 200;

    private static final int FAIL_CODE = 0;

    /**
     * lua script
     */
    private String script;

    private RedisLimit(Builder builder) {
        this.limit = builder.limit ;
        this.jedis = builder.jedis ;
        buildScript();
    }


    /**
     * limit traffic
     * @return if true
     */
    public boolean limit() {
        String key = String.valueOf(System.currentTimeMillis() / 1000);
        Object result = null;
        if (jedis instanceof Jedis) {
            result = ((Jedis) this.jedis).eval(script, Collections.singletonList(key), Collections.singletonList(String.valueOf(limit)));
        } else if (jedis instanceof JedisCluster) {
            result = ((JedisCluster) this.jedis).eval(script, Collections.singletonList(key), Collections.singletonList(String.valueOf(limit)));
        } else {
            //throw new RuntimeException("instance is error") ;
            return false;
        }

        if (FAIL_CODE != (Long) result) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * read lua script
     */
    private void buildScript() {
        script = ScriptUtil.getScript("limit.lua");
    }


    /**
     *  the builder
     * @param <T>
     */
    public static class Builder<T extends JedisCommands>{
        private T jedis = null ;

        private int limit = 200;


        public Builder(T jedis){
            this.jedis = jedis ;
        }

        public Builder limit(int limit){
            this.limit = limit ;
            return this;
        }

        public RedisLimit build(){
            return new RedisLimit(this) ;
        }

    }
}
```

这样客户端在使用时:

```java
RedisLimit redisLimit = new RedisLimit.Builder<>(jedisCluster)
                .limit(limit)
                .build();
```

更加的简单直接，并且避免了将创建过程分成了多个子步骤。

这在有多个构造参数，但又不是必选字段时很有作用。

因此顺便将分布式锁的构建器方式也一并更新了：

[https://github.com/crossoverJie/distributed-redis-tool#features](https://github.com/crossoverJie/distributed-redis-tool#features)

> 更多内容可以参考 Effective Java

### API

从上文可以看出，使用过程就是调用 `limit` 方法。

```java
   //限流
    boolean limit = redisLimit.limit();
    if (!limit){
       //具体限流逻辑
    }
```

为了减少侵入性，也为了简化客户端提供了两种注解方式。

#### @ControllerLimit

该注解可以作用于 `@RequestMapping` 修饰的接口中，并会在限流后提供限流响应。

实现如下：

```java
@Component
public class WebIntercept extends WebMvcConfigurerAdapter {

    private static Logger logger = LoggerFactory.getLogger(WebIntercept.class);


    @Autowired
    private RedisLimit redisLimit;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CustomInterceptor())
                .addPathPatterns("/**");
    }


    private class CustomInterceptor extends HandlerInterceptorAdapter {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                                 Object handler) throws Exception {


            if (redisLimit == null) {
                throw new NullPointerException("redisLimit is null");
            }

            if (handler instanceof HandlerMethod) {
                HandlerMethod method = (HandlerMethod) handler;

                ControllerLimit annotation = method.getMethodAnnotation(ControllerLimit.class);
                if (annotation == null) {
                    //skip
                    return true;
                }

                boolean limit = redisLimit.limit();
                if (!limit) {
                    logger.warn("request has bean limit");
                    response.sendError(500, "request limit");
                    return false;
                }

            }

            return true;

        }
    }
}
```

其实就是实现了 SpringMVC 中的拦截器，并在拦截过程中判断是否有使用注解，从而调用限流逻辑。

**前提是应用需要扫描到该类，让 Spring 进行管理。**

```java
@ComponentScan(value = "com.crossoverjie.distributed.intercept")
```

#### @CommonLimit

当然也可以在普通方法中使用。实现原理则是 Spring AOP (SpringMVC 的拦截器本质也是 AOP)。

```java
@Aspect
@Component
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class CommonAspect {

    private static Logger logger = LoggerFactory.getLogger(CommonAspect.class);

    @Autowired
    private RedisLimit redisLimit ;

    @Pointcut("@annotation(com.crossoverjie.distributed.annotation.CommonLimit)")
    private void check(){}

    @Before("check()")
    public void before(JoinPoint joinPoint) throws Exception {

        if (redisLimit == null) {
            throw new NullPointerException("redisLimit is null");
        }

        boolean limit = redisLimit.limit();
        if (!limit) {
            logger.warn("request has bean limit");
            throw new RuntimeException("request has bean limit") ;
        }

    }
}
```

很简单，也是在拦截过程中调用限流。

当然使用时也得扫描到该包:

```java
@ComponentScan(value = "com.crossoverjie.distributed.intercept")
```

### 总结

**限流**在一个高并发大流量的系统中是保护应用的一个利器，成熟的方案也很多，希望对刚了解这一块的朋友提供一些思路。

以上所有的源码：

- [https://github.com/crossoverJie/distributed-redis-tool](https://github.com/crossoverJie/distributed-redis-tool)
- [https://github.com/crossoverJie/springboot-cloud](https://github.com/crossoverJie/springboot-cloud)

感兴趣的朋友可以点个 Star 或是提交 PR。

