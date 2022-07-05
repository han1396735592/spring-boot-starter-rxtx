package cn.qqhxj.rxtx.starter;

import cn.qqhxj.rxtx.SerialContext;
import cn.qqhxj.rxtx.SerialUtils;
import cn.qqhxj.rxtx.starter.annotation.EnableSerialPort;
import cn.qqhxj.rxtx.starter.annotation.EnableSerialPorts;
import gnu.io.SerialPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotationMetadata;

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
        SerialPortProperties.SerialPortConfig serialPortConfig = new SerialPortProperties.SerialPortConfig();
        String beanName = String.valueOf(attributes.get("value"));
        attributes.remove("value");
        attributes.forEach((k, v) -> {
            try {
                Field field = SerialPortProperties.SerialPortConfig.class.getDeclaredField(k);
                field.setAccessible(true);
                field.set(serialPortConfig, v);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        registerSerialContextBean(beanDefinitionRegistry, serialPortConfig, beanName);
        log.info("start SerialPortRegistrar {}", serialPortConfig.getPortName());
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

    public static void registerSerialContextBean(BeanDefinitionRegistry beanDefinitionRegistry, SerialPortProperties.SerialPortConfig serialPortConfig, String beanName) {
        try {
            SerialPort serialPort = SerialUtils.connect(serialPortConfig.getPortName(),
                    serialPortConfig.getBaudRate(),
                    serialPortConfig.getDataBits(),
                    serialPortConfig.getStopBits(),
                    serialPortConfig.getParity());
            log.debug("configured SerialPortConfig = {}", serialPort);
            log.debug("SerialPortConfig parameter  portName = {},baudRate={}, dataBits={},stopBits ={},parity={}",
                    serialPortConfig.getPortName(),
                    serialPortConfig.getBaudRate(),
                    serialPortConfig.getDataBits(),
                    serialPortConfig.getStopBits(),
                    serialPortConfig.getParity()
            );

            BeanDefinitionBuilder builder = BeanDefinitionBuilder
                    .genericBeanDefinition(SerialContext.class);
            builder.addConstructorArgValue(serialPort);

            beanDefinitionRegistry.registerBeanDefinition(
                    beanName + "." + SerialContext.class.getSimpleName(),
                    builder.getBeanDefinition());
            log.info("register SerialContext [beanName={}] of {} ", beanName + "." + SerialContext.class.getSimpleName(), serialPortConfig.getPortName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
