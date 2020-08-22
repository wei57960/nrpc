package com.cs.nrpc.starter;

import com.cs.nrpcserver.proxy.DefaultProxy;
import com.cs.common.zk.ServiceDiscover;
import com.cs.nrpcserver.annotaion.EnableNRpcConfiguration;
import com.cs.nrpcserver.annotaion.NRpcReference;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;

/**
 * @Author wei
 * @Time 2020/8/6
 * @Description 配置文件读取类
 */
@Configuration
// todo 配置生效的类？
@ConditionalOnClass({NRpcReference.class, EnableNRpcConfiguration.class})
@EnableConfigurationProperties(NrpcProperties.class)
public class NrpcConsumerAutoConfiguration {

    @Autowired
    private NrpcProperties nrpcProperties;

    /**
     * BeanPostProcessor : Bean的后置处理器,
     * 如果我们需要在Spring容器完成Bean的实例化、配置和其他的初始化前后添加一些自己的逻辑处理，
     * 我们就可以定义一个或者多个BeanPostProcessor接口的实现，然后注册到容器中。
     */
    @Bean
    public BeanPostProcessor beanPostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                return initialization(bean, beanName);
            }
        };
    }

    private Object initialization(Object bean, String beanName) {
        Class<?> clazz = AopUtils.isAopProxy(bean) ? AopUtils.getTargetClass(bean) : bean.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            NRpcReference nRpcReference = field.getAnnotation(NRpcReference.class);
            if (nRpcReference != null) {
                // 如果被 @NRpcReference 修饰
                // todo 反射 别的方法？
                Class<?> interfaceClass = nRpcReference.interfaceClass();
                String version = nRpcReference.version();
                ServiceDiscover serviceDiscover = new ServiceDiscover(nrpcProperties.getDiscover());
                String serviceHost = serviceDiscover.discoverClass(interfaceClass);
                // todo add other proxy
                DefaultProxy proxy = nrpcProperties.getProxyType().equals(NrpcProperties.ProxyType.JDK) ? new DefaultProxy(serviceHost) : null;
                Object proxyBean = proxy.createProxy(interfaceClass);
                field.setAccessible(true);

                try {
                    field.set(bean, proxyBean);
                } catch (IllegalAccessException e) {
                    throw new BeanCreationException(beanName, e);
                }
            }
        }
        return null;
    }

}
