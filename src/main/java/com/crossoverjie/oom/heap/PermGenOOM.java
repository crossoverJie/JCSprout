package com.crossoverjie.oom.heap;

import com.crossoverjie.proxy.jdk.CustomizeHandle;
import com.crossoverjie.proxy.jdk.ISubject;
import com.crossoverjie.proxy.jdk.impl.ISubjectImpl;

import java.lang.reflect.Proxy;

/**
 * Function:方法区内存溢出
 *
 * @author crossoverJie
 *         Date: 29/12/2017 21:34
 * @since JDK 1.8
 */
public class PermGenOOM {

    public static void main(String[] args) {
        while (true){
            CustomizeHandle handle = new CustomizeHandle(ISubjectImpl.class) ;
            ISubject subject = (ISubject) Proxy.newProxyInstance(PermGenOOM.class.getClassLoader(), new Class[]{ISubject.class}, handle);
            subject.execute() ;

        }
    }
}
