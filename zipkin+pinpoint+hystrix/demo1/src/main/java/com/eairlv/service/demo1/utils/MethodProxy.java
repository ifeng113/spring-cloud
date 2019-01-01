package com.eairlv.service.demo1.utils;

import com.google.gson.Gson;
import org.slf4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 方法代理
 */
public class MethodProxy implements InvocationHandler {

    private Logger logger;

    public MethodProxy(Logger logger) {
        this.logger = logger;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
//        if (Object.class.equals(method.getDeclaringClass())) {
//            try {
//                return method.invoke(this, args);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            return handle(method, args);
//        }
//        return null;
        return handle(method, args);
    }

    private Object handle(Method method, Object[] args) {
        logger.error("远程调用失败，目标类：" + method.getDeclaringClass());
        logger.error("方法：" + method.getName());
        logger.error("参数：" + new Gson().toJson(args));
        return null;
    }
}
