package com.uestc.yihau.client.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//定义注解将用在什么地方，这里就是用于方法前面
@Target(ElementType.METHOD)
//定义注解的可用级别，这里就是在运行时，运行时可以通过反射来获取
@Retention(RetentionPolicy.RUNTIME)
public @interface SocketCommand {
    short cmd();
}
