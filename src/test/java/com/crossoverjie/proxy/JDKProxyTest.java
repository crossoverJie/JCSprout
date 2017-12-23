package com.crossoverjie.proxy;

import com.crossoverjie.proxy.jdk.impl.ISubjectImpl;
import com.crossoverjie.proxy.jdk.CustomizeHandle;
import com.crossoverjie.proxy.jdk.ISubject;
import org.junit.Test;

import java.lang.reflect.Proxy;

/**
 * Function:JDK 代理单测
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
}
