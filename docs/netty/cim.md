

![](https://ws4.sinaimg.cn/large/006tNbRwly1fyr320o1ymj31hc0u0qew.jpg)

# 前言


大家新年快乐！

新的一年第一篇技术文章希望开个好头，所以元旦三天我也没怎么闲着，希望给大家带来一篇比较感兴趣的干货内容。

老读者应该还记得我在去年国庆节前分享过一篇[《设计一个百万级的消息推送系统》](https://crossoverjie.top/2018/09/25/netty/million-sms-push/)；虽然我在文中有贴一些伪代码，依然有些朋友希望能直接分享一些可以运行的源码；这么久了是时候把坑填上了。

目录结构：

![](https://ws4.sinaimg.cn/large/006tNbRwly1fyrjwtbi0cj304i0jfgm3.jpg)
![](https://ws4.sinaimg.cn/large/006tNbRwly1fyrjy5iu3sj305c03x0sm.jpg)

> 本文较长，高能预警；带好瓜子板凳。


<!--more-->

![](https://ws3.sinaimg.cn/large/006tNbRwly1fyr5yjibvqj30vk04paal.jpg)
![](https://ws3.sinaimg.cn/large/006tNbRwly1fyr60ahgr5j30te08nt9j.jpg)
![](https://ws3.sinaimg.cn/large/006tNbRwly1fyr60vrexlj30vs0d40up.jpg)

于是在之前的基础上我完善了一些内容，先来看看这个项目的介绍吧：

`CIM(CROSS-IM)` 一款面向开发者的 `IM(即时通讯)`系统；同时提供了一些组件帮助开发者构建一款属于自己可水平扩展的 `IM` 。

借助 `CIM` 你可以实现以下需求：

- `IM` 即时通讯系统。
- 适用于 `APP` 的消息推送中间件。
- `IOT` 海量连接场景中的消息透传中间件。

完整源码托管在 GitHub : [https://github.com/crossoverJie/cim](https://github.com/crossoverJie/cim)

# 演示

本次主要涉及到 IM 即时通讯，所以特地录了两段视频演示（群聊、私聊）。

> 点击下方链接可以查看视频版 Demo。

| YouTube | Bilibili|
| :------:| :------: | 
| [群聊](https://youtu.be/_9a4lIkQ5_o) [私聊](https://youtu.be/kfEfQFPLBTQ) | [群聊](https://www.bilibili.com/video/av39405501) [私聊](https://www.bilibili.com/video/av39405821) | 
| <img src="https://ws3.sinaimg.cn/large/006tNbRwly1fys8flaofrj315e0ose81.jpg"  height="295px" />  | <img src="https://ws4.sinaimg.cn/large/006tNbRwly1fys8mpa6wij31240lghdt.jpg" height="295px" />


也在公网部署了一套演示环境，想要试一试的可以联系我加入内测群获取账号一起尬聊😋。

# 架构设计

下面来看看具体的架构设计。

![](https://ws1.sinaimg.cn/large/006tNbRwly1fyldgiizhuj315o0r4n0k.jpg)

- `CIM` 中的各个组件均采用 `SpringBoot` 构建。
-  采用 `Netty + Google Protocol Buffer` 构建底层通信。
-  `Redis` 存放各个客户端的路由信息、账号信息、在线状态等。
-  `Zookeeper` 用于 `IM-server` 服务的注册与发现。

整体主要由以下模块组成：

## cim-server

`IM` 服务端；用于接收 `client` 连接、消息透传、消息推送等功能。

**支持集群部署。**

## cim-forward-route

消息路由服务器；用于处理消息路由、消息转发、用户登录、用户下线以及一些运营工具（获取在线用户数等）。

## cim-client

`IM` 客户端；给用户使用的消息终端，一个命令即可启动并向其他人发起通讯（群聊、私聊）；同时内置了一些常用命令方便使用。


# 流程图

整体的流程也比较简单，流程图如下：

![](https://ws1.sinaimg.cn/large/006tNbRwly1fylfxevl2ij30it0etaau.jpg)

- 客户端向 `route` 发起登录。
- 登录成功从 `Zookeeper` 中选择可用 `IM-server` 返回给客户端，并保存登录、路由信息到 `Redis`。
- 客户端向 `IM-server` 发起长连接，成功后保持心跳。
- 客户端下线时通过 `route` 清除状态信息。


所以当我们自己部署时需要以下步骤：

- 搭建基础中间件 `Redis、Zookeeper`。
- 部署 `cim-server`，这是真正的 IM 服务器，为了满足性能需求所以支持水平扩展，只需要注册到同一个 `Zookeeper` 即可。
- 部署 `cim-forward-route`，这是路由服务器，所有的消息都需要经过它。由于它是无状态的，所以也可以利用 `Nginx` 代理提高可用性。
- `cim-client` 真正面向用户的客户端；启动之后会自动连接 IM 服务器便可以在控制台收发消息了。

更多使用介绍可以参考[快速启动](https://github.com/crossoverJie/cim#%E5%BF%AB%E9%80%9F%E5%90%AF%E5%8A%A8)。

# 详细设计

接下来重点看看具体的实现，比如群聊、私聊消息如何流转；IM 服务端负载均衡；服务如何注册发现等等。

## IM 服务端

先来看看服务端；主要是实现客户端上下线、消息下发等功能。

首先是服务启动：

![](https://ws4.sinaimg.cn/large/006tNbRwly1fyrejaa9iaj30sg0ammz3.jpg)
![](https://ws1.sinaimg.cn/large/006tNbRwly1fyregwf1tnj30qq07qwg3.jpg)

由于是在 `SpringBoot` 中搭建的，所以在应用启动时需要启动 `Netty` 服务。

从 `pipline` 中可以看出使用了 `Protobuf` 的编解码（具体报文在客户端中分析）。

## 注册发现

需要满足 `IM` 服务端的水平扩展需求，所以 `cim-server` 是需要将自身数据发布到注册中心的。

这里参考之前分享的[《搞定服务注册与发现》](https://crossoverjie.top/2018/08/27/distributed/distributed-discovery-zk/)有具体介绍。

所以在应用启动成功后需要将自身数据注册到 `Zookeeper` 中。

![](https://ws3.sinaimg.cn/large/006tNbRwly1fyrerhadxkj30sc05ajs9.jpg)
![](https://ws1.sinaimg.cn/large/006tNbRwly1fyres6kmkaj30sd07e3zn.jpg)

最主要的目的就是将当前应用的 `ip + cim-server-port+ http-port` 注册上去。


![](https://ws1.sinaimg.cn/large/006tNbRwly1fyretaeawnj30y906qjrl.jpg)

上图是我在演示环境中注册的两个 `cim-server` 实例（由于在一台服务器，所以只是端口不同）。

这样在客户端（监听这个 `Zookeeper` 节点）就能实时的知道目前可用的服务信息。

## 登录

当客户端请求 `cim-forward-route` 中的登录接口（详见下文）做完业务验证（就相当于日常登录其他网站一样）之后，客户端会向服务端发起一个长连接，如之前的流程所示：

![](https://ws2.sinaimg.cn/large/006tNbRwly1fyrg4laej8j30r30eu76o.jpg)

这时客户端会发送一个特殊报文，表明当前是登录信息。

服务端收到后就需要将该客户端的 `userID` 和当前 `Channel` 通道关系保存起来。

![](https://ws1.sinaimg.cn/large/006tNbRwly1fyrg639a1wj30sw05zdhb.jpg)
![](https://ws2.sinaimg.cn/large/006tNbRwly1fyrg6w5anej30se056abe.jpg)

同时也缓存了用户的信息，也就是 `userID` 和 用户名。


## 离线

当客户端断线后也需要将刚才缓存的信息清除掉。

![](https://ws2.sinaimg.cn/large/006tNbRwly1fyrgjiwu2lj30sk0in42x.jpg)

同时也需要调用 `route` 接口清除相关信息（具体接口看下文）。



## IM 路由

![](https://ws1.sinaimg.cn/large/006tNbRwly1fyreyfu8ooj314f0qads5.jpg)

从架构图中可以看出，路由层是非常重要的一环；它提供了一系列的 `HTTP` 服务承接了客户端和服务端。

目前主要是以下几个接口。

### 注册接口

![](https://ws2.sinaimg.cn/large/006tNbRwly1fyrf29j3tmj30sn09zmze.jpg)
![](https://ws2.sinaimg.cn/large/006tNbRwly1fyrf2pkzwwj30sg089407.jpg)

由于每一个客户端都是需要登录才能使用的，所以第一步自然是注册。

这里就设计的比较简单，直接利用 `Redis` 来存储用户信息；用户信息也只有 `ID` 和 `userName` 而已。

只是为了方便查询在 `Redis` 中的 `KV` 又反过来存储了一份 `VK`，这样 `ID` 和 `userName` 都必须唯一。


### 登录接口

这里的登录和 `cim-server` 中的登录不一样，具有业务性质，

![](https://ws2.sinaimg.cn/large/006tNbRwly1fyrfnyin5uj30sp0clq6b.jpg)

- 登录成功之后需要判断是否是重复登录（一个用户只能运行一个客户端）。
- 登录成功后需要从 `Zookeeper` 中获取服务列表（`cim-server`）并根据某种算法选择一台服务返回给客户端。
- 登录成功之后还需要保存路由信息，也就是当前用户分配的服务实例保存到 `Redis` 中。

为了实现只能一个用户登录，使用了 `Redis` 中的 `set` 来保存登录信息；利用 `userID` 作为 `key` ，重复的登录就会写入失败。

![](https://ws3.sinaimg.cn/large/006tNbRwly1fyrflvo52lj30qu0attan.jpg)
![](https://ws4.sinaimg.cn/large/006tNbRwly1fyrfmfrnfkj30sm05bgmd.jpg)

> 类似于 Java 中的 HashSet，只能去重保存。


获取一台可用的路由实例也比较简单：

![](https://ws2.sinaimg.cn/large/006tNbRwly1fyrfp67qo3j30qz08xq41.jpg)

- 先从 `Zookeeper` 获取所有的服务实例做一个内部缓存。
- 轮询选择一台服务器（目前只有这一种算法，后续会新增）。

当然要获取 `Zookeeper` 中的服务实例前自然是需要监听 `cim-server` 之前注册上去的那个节点。

具体代码如下：

![](https://ws4.sinaimg.cn/large/006tNbRwly1fyrfrnxk03j30qx04g3yy.jpg)
![](https://ws4.sinaimg.cn/large/006tNbRwly1fyrfs42vwcj30qy06raaw.jpg)
![](https://ws1.sinaimg.cn/large/006tNbRwly1fyrfsuferjj30qt08xwfy.jpg)

也是在应用启动之后监听 `Zookeeper` 中的路由节点，一旦发生变化就会更新内部缓存。

> 这里使用的是 Guava 的 cache，它基于 `ConcurrentHashMap`，所以可以保证`清除、新增缓存`的原子性。

### 群聊接口

这是一个真正发消息的接口，实现的效果就是其中一个客户端发消息，其余所有客户端都能收到！

流程肯定是客户端发送一条消息到服务端，服务端收到后在上文介绍的 `SessionSocketHolder` 中遍历所有 `Channel`（通道）然后下发消息即可。

服务端是单机倒也可以，但现在是集群设计。所以所有的客户端会根据之前的轮询算法分配到不同的 `cim-server` 实例中。

因此就需要路由层来发挥作用了。

![](https://ws2.sinaimg.cn/large/006tNbRwly1fyrgrd1fcgj30sm0f9djy.jpg)
![](https://ws1.sinaimg.cn/large/006tNbRwly1fyrgt3135cj30st0a340e.jpg)

路由接口收到消息后首先遍历出所有的客户端和服务实例的关系。

路由关系在 `Redis` 中的存放如下：

![](https://ws2.sinaimg.cn/large/006tNbRwly1fyrgulywq5j30bc02ztaq.jpg)

由于 `Redis` 单线程的特质，当数据量大时；一旦使用 keys 匹配所有 `cim-route:*` 数据，会导致 Redis 不能处理其他请求。

所以这里改为使用 scan 命令来遍历所有的 `cim-route:*`。

---

接着会挨个调用每个客户端所在的服务端的 `HTTP` 接口用于推送消息。

在 `cim-server` 中的实现如下：

![](https://ws4.sinaimg.cn/large/006tNbRwly1fyrgyjn184j30sg0b40v5.jpg)
![](https://ws2.sinaimg.cn/large/006tNbRwly1fyrgz0lm1dj30sx082ac3.jpg)

`cim-server` 收到消息后会在内部缓存中查询该 userID 的通道，接着只需要发消息即可。


### 在线用户接口

这是一个辅助接口，可以查询出当前在线用户信息。

![](https://ws2.sinaimg.cn/large/006tNbRwly1fyrh2fp5qrj30sp05u75x.jpg)
![](https://ws2.sinaimg.cn/large/006tNbRwly1fyrh2w6fk5j30sh06nmyc.jpg)

实现也很简单，也就是查询之前保存 ”用户登录状态的那个去重 `set` “即可。

### 私聊接口

之所以说获取在线用户是一个辅助接口，其实就是用于辅助私聊使用的。

一般我们使用私聊的前提肯定得知道当前哪些用户在线，接着你才会知道你要和谁进行私聊。

类似于这样：

![](https://ws3.sinaimg.cn/large/006tNbRwly1fylh7bdlo6g30go01shdt.gif)

在我们这个场景中，私聊的前提就是需要获得在线用户的 `userID`。


![](https://ws4.sinaimg.cn/large/006tNbRwly1fyrh7nditgj30so0cegot.jpg)

所以私聊接口在收到消息后需要查询到接收者所在的 `cim-server` 实例信息，后续的步骤就和群聊一致了。调用接收者所在实例的 `HTTP` 接口下发信息。

只是群聊是遍历所有的在线用户，私聊只发送一个的区别。

### 下线接口

一旦客户端下线，我们就需要将之前存放在 `Redis` 中的一些信息删除掉（路由信息、登录状态）。

![](https://ws3.sinaimg.cn/large/006tNbRwly1fyrhcb5mehj30sp070gnl.jpg)
![](https://ws4.sinaimg.cn/large/006tNbRwly1fyrhcjsznmj30sp048q3n.jpg)




## IM 客户端

客户端中的一些逻辑其实在上文已经谈到一些了。

### 登录 

第一步也就是登录，需要在启动时调用 `route` 的登录接口，获得 `cim-server` 信息再创建连接。

![](https://ws3.sinaimg.cn/large/006tNbRwly1fyrhj0mgeyj30si06pdgr.jpg)

![image-20190102001525565](https://ws1.sinaimg.cn/large/006tNbRwly1fyrjgk4maej30su05u3zq.jpg)

![](https://ws4.sinaimg.cn/large/006tNbRwly1fyrhjtv984j30sr0fnadw.jpg)

登录过程中 `route` 接口会判断是否为重复登录，重复登录则会直接退出程序。

![](https://ws4.sinaimg.cn/large/006tNbRwly1fyrhl41i3cj30sm095jt2.jpg)

接下来是利用 `route` 接口返回的 `cim-server` 实例信息（`ip+port`）创建连接。

最后一步就是发送一个登录标志的信息到服务端，让它保持客户端和 `Channel` 的关系。

![](https://ws1.sinaimg.cn/large/006tNbRwly1fyrhn66rmij30sn06j0u1.jpg)

### 自定义协议

上文提到的一些`登录报文、真正的消息报文`这些其实都是在我们自定义协议中可以区别出来的。

由于是使用 `Google Protocol Buffer` 编解码，所以先看看原始格式。

![](https://ws1.sinaimg.cn/large/006tNbRwly1fyrhpupejtj30sj072my1.jpg)

其实这个协议中目前一共就三个字段：

- `requestId` 可以理解为 `userId`。
- `reqMsg` 就是真正的消息。
- `type` 也就是上文提到的消息类别。


目前主要是三种类型，分别对应不同的业务：

![](https://ws2.sinaimg.cn/large/006tNbRwly1fyrhsf53dzj30sf08mmy3.jpg)

### 心跳

为了保持客户端和服务端的连接，每隔一段时间没有发送消息都需要自动的发送心跳。

目前的策略是每隔一分钟就是发送一个心跳包到服务端：

![](https://ws3.sinaimg.cn/large/006tNbRwly1fyrhvs3z2gj30s209njtc.jpg)
![](https://ws2.sinaimg.cn/large/006tNbRwly1fyrhwdgorcj30sj08cabj.jpg)

这样服务端每隔一分钟没有收到业务消息时就会收到 `ping` 的心跳包：

![](https://ws1.sinaimg.cn/large/006tNbRwly1fyrhuu1xd3j30s40c0h83.jpg)


### 内置命令

客户端也内置了一些基本命令来方便使用。

| 命令 | 描述|
| ------ | ------ |
| `:q` | 退出客户端|
| `:olu` | 获取所有在线用户信息 |
| `:all` | 获取所有命令 |
| `:` | 更多命令正在开发中。。 |

![](https://ws3.sinaimg.cn/large/006tNbRwly1fylh7bdlo6g30go01shdt.gif)

比如输入 `:q` 就会退出客户端，同时会关闭一些系统资源。

![](https://ws4.sinaimg.cn/large/006tNbRwly1fyri42lsgpj30sg0cewg3.jpg)
![](https://ws1.sinaimg.cn/large/006tNbRwly1fyri5mwdh5j30sm0djwgp.jpg)

当输入 `:olu`(`onlineUser` 的简写)就会去调用 `route` 的获取所有在线用户接口。

![](https://ws2.sinaimg.cn/large/006tNbRwly1fyri6wuz62j30ss08eq4b.jpg)
![](https://ws2.sinaimg.cn/large/006tNbRwly1fyri7dvvubj312803u48b.jpg)

### 群聊

群聊的使用非常简单，只需要在控制台输入消息回车即可。

这时会去调用 `route` 的群聊接口。

![](https://ws4.sinaimg.cn/large/006tNbRwly1fyri9ltxedj30su0bkjtn.jpg)

### 私聊

私聊也是同理，但前提是需要触发关键字；使用 `userId;;消息内容` 这样的格式才会给某个用户发送消息，所以一般都需要先使用 `:olu` 命令获取所以在线用户才方便使用。

![](https://ws3.sinaimg.cn/large/006tNbRwly1fyrichyh40j30si0btq57.jpg)

### 消息回调

为了满足一些定制需求，比如消息需要保存之类的。

所以在客户端收到消息之后会回调一个接口，在这个接口中可以自定义实现。

![](https://ws4.sinaimg.cn/large/006tNbRwly1fyriffgjt4j30sh0bitb6.jpg)
![](https://ws2.sinaimg.cn/large/006tNbRwly1fyrigbtcfrj30sq06nq3r.jpg)

因此先创建了一个 `caller` 的 `bean`，这个 `bean` 中包含了一个 `CustomMsgHandleListener` 接口，需要自行处理只需要实现此接口即可。

### 自定义界面

由于我自己不怎么会写界面，但保不准有其他大牛会写。所以客户端中的群聊、私聊、获取在线用户、消息回调等业务(以及之后的业务)都是以接口形式提供。

也方便后面做页面集成，只需要调这些接口就行了；具体实现不用怎么关心。

# 总结

`cim` 目前只是第一版，BUG 多，功能少（只拉了几个群友做了测试）；不过后续还会接着完善，至少这一版会给那些没有相关经验的朋友带来一些思路。

后续计划：

![](https://ws4.sinaimg.cn/large/006tNbRwly1fyrinluw2vj30r50arabu.jpg)

完整源码：

[https://github.com/crossoverJie/cim](https://github.com/crossoverJie/cim)

如果这篇对你有所帮助还请不吝转发。