package com.eairlv.service.demo1.utils;

import com.eairlv.service.demo1.Demo2FeignFactory;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;

/**
 * 远程捕获
 */
public class HystrixErrorFactory<T> implements FallbackFactory<T> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public T create(Throwable throwable) {
        T newProxyInstance = (T) Proxy.newProxyInstance(
                getEntityClass().getClassLoader(),
                new Class[] { getEntityClass() }, new MethodProxy(logger));
        return newProxyInstance;
    }

    private <T> Class<? extends T> getEntityClass() {
        return ReflectionUtil.getSuperClassGenricType(getClass());
    }

//    @Override
//    public Demo2FeignFactory create(Throwable cause) {
//
//        return new Demo2FeignFactory(){
//            @Override
//            public String demo2() {
//                System.out.printf("aa");
//                return null;
//            }
//        };
//    }
}

