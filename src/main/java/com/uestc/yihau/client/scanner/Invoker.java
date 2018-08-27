package com.uestc.yihau.client.scanner;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Invoker {

    private Object target;

    private Method method;

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object invoke(Object... args){
        try {
           return method.invoke(target, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Invoker valueOf(Object object, Method method){
        Invoker invoker = new Invoker();
        invoker.setTarget(object);
        invoker.setMethod(method);
        return invoker;
    }
}
