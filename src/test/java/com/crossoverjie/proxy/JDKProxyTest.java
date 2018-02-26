package com.crossoverjie.proxy;

import com.crossoverjie.proxy.jdk.CustomizeHandle;
import com.crossoverjie.proxy.jdk.ISubject;
import com.crossoverjie.proxy.jdk.impl.ISubjectImpl;
import org.junit.Test;
import sun.misc.ProxyGenerator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Proxy;

/**
 * Function: JDK 代理单测
 *
 * @author crossoverJie
 *         Date: 23/12/2017 22:40
 * @since JDK 1.8
 */
public class JDKProxyTest {

    @Test
    public void test(){
        CustomizeHandle handle = new CustomizeHandle(ISubjectImpl.class) ;
        ISubject subject = (ISubject) Proxy.newProxyInstance(JDKProxyTest.class.getClassLoader(), new Class[]{ISubject.class}, handle);
        subject.execute() ;
    }

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
}
