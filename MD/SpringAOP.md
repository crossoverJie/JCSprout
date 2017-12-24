# Spring AOP 实现原理

## 静态代理

众所周知 Spring 的 `AOP` 是基于动态代理实现的，谈到动态代理就不得不提下静态代理。实现如下：

假设有一接口 `InterfaceA`：

```java
public interface InterfaceA{
    void exec();
}
```

其中有实现类 `RealImplement`:
```java
public class RealImplement implement InterfaceA{
    public void exec(){
        System.out.println("real impl") ;
    }
}
```

这时也有一个代理类 `ProxyImplement` 也实现了 `InterfaceA`:
```java
public class ProxyImplement implement InterfaceA{
    private InterfaceA interface ;
    
    public ProxyImplement(){
        interface = new RealImplement() ;
    }
    
    public void exec(){
        System.out.println("dosomethings before);
        //实际调用
        interface.exec();
        
        System.out.println("dosomethings after);
    }
    
}
```
使用如下:
```
public class Main(){
    public static void main(String[] args){
        InterfaceA interface = new ProxyImplement() ;
        interface.exec();
    }
}
```
可以看出这样的代理方式调用者其实都不知道被代理对象的存在。

## JDK 动态代理

## CGLIB 动态代理
