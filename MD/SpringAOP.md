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
```java
public class Main(){
    public static void main(String[] args){
        InterfaceA interface = new ProxyImplement() ;
        interface.exec();
    }
}
```
可以看出这样的代理方式调用者其实都不知道被代理对象的存在。

## JDK 动态代理
从静态代理中可以看出: 静态代理只能代理一个具体的类，如果要代理一个接口的多个实现的话需要定义不同的代理类。

需要解决这个问题就可以用到 JDK 的动态代理。

其中有两个非常核心的类:

- `java.lang.reflect.Proxy`类。
- `java.lang.reflect.InvocationHandler`接口。

`Proxy` 类是用于创建代理对象，而 `InvocationHandler` 接口主要你是来处理执行逻辑。

如下：
```java
public class CustomizeHandle implements InvocationHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(CustomizeHandle.class);

    private Object target;

    public CustomizeHandle(Class clazz) {
        try {
            this.target = clazz.newInstance();
        } catch (InstantiationException e) {
            LOGGER.error("InstantiationException", e);
        } catch (IllegalAccessException e) {
            LOGGER.error("IllegalAccessException",e);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        before();
        Object result = method.invoke(target, args);
        after();

        LOGGER.info("proxy class={}", proxy.getClass());
        return result;
    }


    private void before() {
        LOGGER.info("handle before");
    }

    private void after() {
        LOGGER.info("handle after");
    }
}
```

其中构造方法传入被代理类的类类型。其实传代理类的实例或者是类类型并没有强制的规定，传类类型的是因为被代理对象应当由代理创建而不应该由调用方创建。

使用方式如下：
```java
    @Test
    public void test(){
        CustomizeHandle handle = new CustomizeHandle(ISubjectImpl.class) ;
        ISubject subject = (ISubject) Proxy.newProxyInstance(JDKProxyTest.class.getClassLoader(), new Class[]{ISubject.class}, handle);
        subject.execute() ;
    }
```

首先传入被代理类的类类型构建代理处理器。接着使用 `Proxy` 的`newProxyInstance` 方法动态创建代理类。第一个参数为类加载器，第二个参数为代理类需要实现的接口列表，最后一个则是处理器。

其实代理类是由

![](https://ws3.sinaimg.cn/large/006tNc79gy1fms01lcml3j30ki09s75v.jpg)

这个方法动态创建出来的。将 proxyClassFile 输出到文件并进行反编译的话就可以的到代理类。
```java
    @Test
    public void clazzTest(){
        byte[] proxyClassFile = ProxyGenerator.generateProxyClass(
                "$Proxy1", new Class[]{ISubject.class}, 1);
        try {
            FileOutputStream out = new FileOutputStream("/Users/chenjie/Documents/$Proxy1.class") ;
            out.write(proxyClassFile);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
```

反编译后结果如下:
```java
import com.crossoverjie.proxy.jdk.ISubject;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;

public class $Proxy1 extends Proxy implements ISubject {
    private static Method m1;
    private static Method m2;
    private static Method m3;
    private static Method m0;

    public $Proxy1(InvocationHandler var1) throws  {
        super(var1);
    }

    public final boolean equals(Object var1) throws  {
        try {
            return ((Boolean)super.h.invoke(this, m1, new Object[]{var1})).booleanValue();
        } catch (RuntimeException | Error var3) {
            throw var3;
        } catch (Throwable var4) {
            throw new UndeclaredThrowableException(var4);
        }
    }

    public final String toString() throws  {
        try {
            return (String)super.h.invoke(this, m2, (Object[])null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    public final void execute() throws  {
        try {
            super.h.invoke(this, m3, (Object[])null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    public final int hashCode() throws  {
        try {
            return ((Integer)super.h.invoke(this, m0, (Object[])null)).intValue();
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    static {
        try {
            m1 = Class.forName("java.lang.Object").getMethod("equals", new Class[]{Class.forName("java.lang.Object")});
            m2 = Class.forName("java.lang.Object").getMethod("toString", new Class[0]);
            m3 = Class.forName("com.crossoverjie.proxy.jdk.ISubject").getMethod("execute", new Class[0]);
            m0 = Class.forName("java.lang.Object").getMethod("hashCode", new Class[0]);
        } catch (NoSuchMethodException var2) {
            throw new NoSuchMethodError(var2.getMessage());
        } catch (ClassNotFoundException var3) {
            throw new NoClassDefFoundError(var3.getMessage());
        }
    }
}
```

可以看到代理类继承了 `Proxy` 类，并实现了 `ISubject` 接口，由此也可以看到 JDK 动态代理为什么需要实现接口，已经继承了 `Proxy`是不能再继承其余类了。

其中实现了 `ISubject` 的 `execute()` 方法，并通过 `InvocationHandler` 中的 `invoke()` 方法来进行调用的。


## CGLIB 动态代理

cglib 是对一个小而快的字节码处理框架 `ASM` 的封装。
他的特点是继承于被代理类，这就要求被代理类不能被 `final` 修饰。


