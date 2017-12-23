package com.crossoverjie.proxy;

import com.crossoverjie.proxy.impl.ISubjectImpl;
import org.junit.Test;

import java.lang.reflect.Proxy;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 23/12/2017 22:40
 * @since JDK 1.8
 */
public class ProxyTest {
    
    @Test
    public void test(){
        CustomizeHandle handle = new CustomizeHandle(ISubjectImpl.class) ;
        ISubject subject = (ISubject) Proxy.newProxyInstance(ProxyTest.class.getClassLoader(), new Class[]{ISubject.class}, handle);
        subject.execute() ;
    }
}
