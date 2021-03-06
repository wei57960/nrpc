package com.cs.nrpcserver.annotaion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @Author wei
 * @Time 2020/8/12
 * @Description 服务提供
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NRpcProvider {

    Class<?> interfaceClass();

    String version() default "";

}
