
<div align="center">  

<img src="https://ws3.sinaimg.cn/large/006tNbRwly1fxda6k9k3bj30oy08cjsx.jpg"  /> 
<br/>

[![Build Status](https://travis-ci.org/crossoverJie/cicada.svg?branch=master)](https://travis-ci.org/crossoverJie/cicada)
[![](https://maven-badges.herokuapp.com/maven-central/top.crossoverjie.opensource/cicada-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/top.crossoverjie.opensource/cicada-core/)
[![QQ群](https://img.shields.io/badge/QQ%E7%BE%A4-787381170-yellowgreen.svg)](https://jq.qq.com/?_wv=1027&k=5HPYvQk)

[qq0groupsvg]: https://img.shields.io/badge/QQ%E7%BE%A4-787381170-yellowgreen.svg
[qq0group]: https://jq.qq.com/?_wv=1027&k=5HPYvQk


📘[特性](#features) |🌁[快速启动](#quick-start) | 🏖[性能测试](#performance-test) | 🌈[更新记录](#changelog) | 💡 [联系作者](#contact-author)|🇦🇺[English](https://github.com/TogetherOS/cicada)

</div><br>


## 简介

基于 Netty4 实现的快速、轻量级 WEB 框架；没有过多的依赖，核心 jar 包仅 `30KB`。

如果你感兴趣，请点 [Star](https://github.com/crossoverJie/cicada/stargazers)。

## 特性

- [x] 代码简洁，没有过多依赖。
- [x] 一行代码即可启动 HTTP 服务。
- [x] [自定义拦截器](#自定义拦截器)。
- [x] 灵活的传参方式。
- [x] `json` 响应格式。
- [x] [自定义配置](#自定义配置)。
- [x] 多种响应方式。
- [x] 内置可插拔 `IOC` 容器。
- [x] [`Cookie` 支持](#cookie-支持)。
- [ ] 文件上传。


## 快速启动

创建一个 maven 项目，引入核心依赖。

```java
<dependency>
    <groupId>top.crossoverjie.opensource</groupId>
    <artifactId>cicada-core</artifactId>
    <version>x.y.z</version>
</dependency>
```

启动类：

```java
public class MainStart {

    public static void main(String[] args) throws InterruptedException {
        CicadaServer.start(MainStart.class,"/cicada-example") ;
    }
}
```

### 配置业务 Action

```java
@CicadaAction("routeAction")
public class RouteAction {

    private static final Logger LOGGER = LoggerBuilder.getLogger(RouteAction.class);


    @CicadaRoute("getUser")
    public void getUser(DemoReq req){

        LOGGER.info(req.toString());
        WorkRes<DemoReq> reqWorkRes = new WorkRes<>() ;
        reqWorkRes.setMessage("hello =" + req.getName());
        CicadaContext.getContext().json(reqWorkRes) ;
    }

    @CicadaRoute("getInfo")
    public void getInfo(DemoReq req){

        WorkRes<DemoReq> reqWorkRes = new WorkRes<>() ;
        reqWorkRes.setMessage("getInfo =" + req.toString());
        CicadaContext.getContext().json(reqWorkRes) ;
    }

    @CicadaRoute("getReq")
    public void getReq(CicadaContext context,DemoReq req){

        WorkRes<DemoReq> reqWorkRes = new WorkRes<>() ;
        reqWorkRes.setMessage("getReq =" + req.toString());
        context.json(reqWorkRes) ;
    }



}
```

启动应用访问 [http://127.0.0.1:5688/cicada-example/routeAction/getUser?id=1234&name=zhangsan](http://127.0.0.1:5688/cicada-example/routeAction/getUser?id=1234&name=zhangsan)

```json
{"message":"hello =zhangsan"}
```

## Cicada 上下文

通过 `context.json(),context.text()` 方法可以选择不同的响应方式。

```java
@CicadaAction("routeAction")
public class RouteAction {

    private static final Logger LOGGER = LoggerBuilder.getLogger(RouteAction.class);

    @CicadaRoute("getUser")
    public void getUser(DemoReq req){

        LOGGER.info(req.toString());
        WorkRes<DemoReq> reqWorkRes = new WorkRes<>() ;
        reqWorkRes.setMessage("hello =" + req.getName());
        CicadaContext.getContext().json(reqWorkRes) ;
    }
    
    @CicadaRoute("hello")
    public void hello() throws Exception {
        CicadaContext context = CicadaContext.getContext();

        String url = context.request().getUrl();
        String method = context.request().getMethod();
        context.text("hello world url=" + url + " method=" + method);
    }    


}
```


## Cookie 支持

### 设置 Cookie

```java
Cookie cookie = new Cookie() ;
cookie.setName("cookie");
cookie.setValue("value");
CicadaContext.getResponse().setCookie(cookie);
```

### 获取 Cookie

```java
Cookie cookie = CicadaContext.getRequest().getCookie("cookie");
logger.info("cookie = " + cookie.toString());
```

## 自定义配置

`cicada` 默认会读取 classpath 下的 `application.properties` 配置文件。

同时也可以自定义配置文件。

只需要继承 `top.crossoverjie.cicada.server.configuration.AbstractCicadaConfiguration`

并传入配置文件名称即可。比如：


```java
public class RedisConfiguration extends AbstractCicadaConfiguration {


    public RedisConfiguration() {
        super.setPropertiesName("redis.properties");
    }

}

public class KafkaConfiguration extends AbstractCicadaConfiguration {

    public KafkaConfiguration() {
        super.setPropertiesName("kafka.properties");
    }


}
```

![](https://ws3.sinaimg.cn/large/0069RVTdgy1fv5mw7p5nvj31by0fo76t.jpg)

### 获取配置

按照如下方式即可获取自定义配置：

```java
KafkaConfiguration configuration = (KafkaConfiguration) getConfiguration(KafkaConfiguration.class);
RedisConfiguration redisConfiguration = (RedisConfiguration) ConfigurationHolder.getConfiguration(RedisConfiguration.class);
ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) ConfigurationHolder.getConfiguration(ApplicationConfiguration.class);

String brokerList = configuration.get("kafka.broker.list");
String redisHost = redisConfiguration.get("redis.host");
String port = applicationConfiguration.get("cicada.port");

LOGGER.info("Configuration brokerList=[{}],redisHost=[{}] port=[{}]",brokerList,redisHost,port);
```

### 外置配置文件

当然在特殊环境中(`dev/test/pro`)也可以读取外置配置文件。只需要加上启动参数，保证参数名称和文件名一致即可。

```shell
-Dapplication.properties=/xx/application.properties
-Dkafka.properties=/xx/kakfa.properties
-Dredis.properties=/xx/redis.properties
```

## 自定义拦截器

实现 `top.crossoverjie.cicada.example.intercept.CicadaInterceptor` 接口。

```java
@Interceptor(value = "executeTimeInterceptor")
public class ExecuteTimeInterceptor implements CicadaInterceptor {

    private static final Logger LOGGER = LoggerBuilder.getLogger(ExecuteTimeInterceptor.class);

    private Long start;

    private Long end;

    @Override
    public boolean before(Param param) {
        start = System.currentTimeMillis();
        return true;
    }

    @Override
    public void after(Param param) {
        end = System.currentTimeMillis();

        LOGGER.info("cast [{}] times", end - start);
    }
}
```


## 性能测试

![](https://ws4.sinaimg.cn/large/006tNbRwly1fv4luap7w0j31kw0iwdnu.jpg)

> 测试条件：100 threads and 100 connections ;1G RAM/4 CPU。

**每秒将近 10W 请求。**

## 更新记录

### v2.0.1
- 更新 Logo ,美化日志。
- 支持 `Cookie`

### v2.0.0
- 修复 [#12](https://github.com/TogetherOS/cicada/issues/12) [#22](https://github.com/TogetherOS/cicada/issues/22) [#28](28)
- 更加灵活的路由方式。
- 内置可插拔 `IOC` 容器。

### v1.0.3

- 修复 [#9](https://github.com/TogetherOS/cicada/issues/9)
- 修复 [#8](https://github.com/TogetherOS/cicada/issues/8),多种响应方式。
- 重构了核心代码，新增上下文环境。
- 优雅停机。

### v1.0.2

- 修复 [#6](https://github.com/TogetherOS/cicada/issues/6)
- 自定义配置文件。
- 灵活使用配置。
- 重构代码。

## 联系作者


> crossoverJie#gmail.com

<img src="https://ws2.sinaimg.cn/large/006tKfTcly1fsa01u7ro1j30gs0howfq.jpg" width="300"/> 

## 特别感谢

- [Netty](https://github.com/netty/netty)
- [blade](https://github.com/lets-blade/blade)