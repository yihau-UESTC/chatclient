package com.uestc.yihau.client.scanner;

import com.uestc.yihau.client.annotion.SocketCommand;
import com.uestc.yihau.client.annotion.SocketMoudule;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class HandlerScanner implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("scanner");
        Class<? extends Object> clazz = bean.getClass();
        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces != null && interfaces.length > 0){
            for (Class<?> in : interfaces){
                SocketMoudule socketMoudule = in.getAnnotation(SocketMoudule.class);
                if (socketMoudule == null){
                    continue;
                }
                Method[] methods = in.getMethods();
                if (methods != null && methods.length > 0){
                    for (Method method : methods){
                        SocketCommand socketCommand = method.getAnnotation(SocketCommand.class);
                        if (socketCommand == null){
                            continue;
                        }
                        short module = socketMoudule.module();
                        short cmd = socketCommand.cmd();
                        Invoker invoker = Invoker.valueOf(bean, method);
                        if (InvokerHolder.getInvoker(module, cmd) == null){
                            InvokerHolder.addInvoker(module, cmd, invoker);
                        }else {
                            System.out.println("cfzc");
                        }
                    }
                }
            }
        }

        return null;
    }
}
