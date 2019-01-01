package com.eairlv.service.demo1.dynamic.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Run {

    public static void main(String[] args) {

//        Subject realSubject = new RealSubject();
//        InvocationHandler handler = new DynamicProxy(realSubject);
//
////        Subject subject = (Subject)Proxy.newProxyInstance(handler.getClass().getClassLoader(), realSubject
////                .getClass().getInterfaces(), handler);
//        Subject subject = (Subject)Proxy.newProxyInstance(Subject.class.getClassLoader(), realSubject
//                .getClass().getInterfaces(), handler);
//
//        System.out.println(subject.getClass().getName());
//        subject.rent();
//        subject.hello("lv");

        Subject subject = (Subject)Proxy.newProxyInstance(Subject.class.getClassLoader(), new Class[]{Subject.class}, new DynamicProxy(null));
        subject.rent();

    }


}
