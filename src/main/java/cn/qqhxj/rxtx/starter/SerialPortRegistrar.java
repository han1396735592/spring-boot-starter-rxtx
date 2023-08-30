package cn.qqhxj.rxtx.starter;

import cn.qqhxj.rxtx.context.SerialContext;
import cn.qqhxj.rxtx.context.SerialContextImpl;
import cn.qqhxj.rxtx.context.SerialPortConfig;
import cn.qqhxj.rxtx.starter.annotation.EnableSerialPort;
import cn.qqhxj.rxtx.starter.annotation.EnableSerialPorts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

/**
 * @author han1396735592
 */
@Slf4j
@Order(Integer.MIN_VALUE)
public class SerialPortRegistrar implements ImportBeanDefinitionRegistrar {

    private void registerBeanDefinitions(Map<String, Object> attributes, BeanDefinitionRegistry beanDefinitionRegistry) {
        SerialPortConfig serialPortConfig = new SerialPortConfig();
        String alias = String.valueOf(attributes.get("value"));
        attributes.remove("value");
        attributes.forEach((k, v) -> {
            try {
                Field field = SerialPortConfig.class.getDeclaredField(k);
                field.setAccessible(true);
                field.set(serialPortConfig, v);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        if (StringUtils.isEmpty(alias)) {
            alias = String.valueOf(serialPortConfig.getPort());
        }
        serialPortConfig.setAlias(alias);
        registerSerialContextBean(beanDefinitionRegistry, serialPortConfig, serialPortConfig.getAlias());
    }


    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        Set<String> annotationTypes = annotationMetadata.getAnnotationTypes();
        for (String annotationType : annotationTypes) {
            if (annotationType.equals(EnableSerialPorts.class.getName())) {
                Map<String, Object> annotationAttributes = annotationMetadata
                        .getAnnotationAttributes(EnableSerialPorts.class.getName(), false);
                if (annotationAttributes != null) {
                    Object value = annotationAttributes.get("value");
                    if (value != null) {
                        if (value instanceof AnnotationAttributes[]) {
                            for (AnnotationAttributes attributes : ((AnnotationAttributes[]) value)) {
                                registerBeanDefinitions(attributes, beanDefinitionRegistry);
                            }
                        }
                    }
                }

            } else if (annotationType.equals(EnableSerialPort.class.getName())) {
                Map<String, Object> attributes = annotationMetadata
                        .getAnnotationAttributes(EnableSerialPort.class.getName(), false);
                if (attributes != null) {
                    registerBeanDefinitions(attributes, beanDefinitionRegistry);
                }
            }
        }

    }

    public static void registerSerialContextBean(BeanDefinitionRegistry beanDefinitionRegistry, SerialPortConfig serialPortConfig, String beanName) {
        try {
            log.info("[{}] configured [port={},autoConnect={},baud={},parity={},dataBits={},stopBits={}]",
                    serialPortConfig.getAlias(), serialPortConfig.getPort(),
                    serialPortConfig.isAutoConnect(),
                    serialPortConfig.getBaud(), serialPortConfig.getParity(),
                    serialPortConfig.getDataBits(), serialPortConfig.getStopBits()
            );
            BeanDefinitionBuilder builder = BeanDefinitionBuilder
                    .genericBeanDefinition(SerialContextImpl.class);
            builder.addConstructorArgValue(serialPortConfig);
            beanDefinitionRegistry.registerBeanDefinition(
                    beanName + "." + SerialContext.class.getSimpleName(),
                    builder.getBeanDefinition());
            log.info("Register SerialContextImpl [beanName={}] of {} ", beanName + "." + SerialContext.class.getSimpleName(), serialPortConfig.getPort());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
