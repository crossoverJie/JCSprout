# 限流算法

限流是解决高并发大流量的一种方案，至少是可以保证应用的可用性。

通常有以下两种限流方案：

- 漏桶算法
- 令牌桶算法

## 漏桶算法

![漏桶算法，来自网络.png](https://i.loli.net/2017/08/11/598c905caa8cb.png)

漏桶算法非常简单，就是将流量放入桶中并按照一定的速率流出。如果流量过大时候并不会提高流出效率，而溢出的流量也只能是抛弃掉了。

这种算法很简单，但也非常粗暴，无法应对突发的大流量。
这时可以考虑令牌桶算法。

## 令牌桶算法
![令牌桶算法-来自网络.gif](https://i.loli.net/2017/08/11/598c91f2a33af.gif)

令牌桶算法是按照恒定的速率向桶中放入令牌，每当请求经过时则消耗一个或多个令牌。当桶中的令牌为 0 时，请求则会被阻塞。

> note：
令牌桶算法支持先消费后付款，比如一个请求可以获取多个甚至全部的令牌，但是需要后面的请求付费。也就是说后面的请求需要等到桶中的令牌补齐之后才能继续获取。

实例:
```java
    @Override
    public BaseResponse<UserResVO> getUserByFeignBatch(@RequestBody UserReqVO userReqVO) {
        //调用远程服务
        OrderNoReqVO vo = new OrderNoReqVO() ;
        vo.setReqNo(userReqVO.getReqNo());

        RateLimiter limiter = RateLimiter.create(2.0) ;
        //批量调用
        for (int i = 0 ;i< 10 ; i++){
            double acquire = limiter.acquire();
            logger.debug("获取令牌成功!,消耗=" + acquire);
            BaseResponse<OrderNoResVO> orderNo = orderServiceClient.getOrderNo(vo);
            logger.debug("远程返回:"+JSON.toJSONString(orderNo));
        }

        UserRes userRes = new UserRes() ;
        userRes.setUserId(123);
        userRes.setUserName("张三");

        userRes.setReqNo(userReqVO.getReqNo());
        userRes.setCode(StatusEnum.SUCCESS.getCode());
        userRes.setMessage("成功");

        return userRes ;
    }
```


1. [单 JVM 限流](http://crossoverjie.top/2017/08/11/sbc4/)
2. [分布式限流](http://crossoverjie.top/2018/04/28/sbc/sbc7-Distributed-Limit/)
