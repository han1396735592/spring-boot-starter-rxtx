package cn.qqhxj.rxtx.starter;

import cn.qqhxj.rxtx.SerialContext;
import cn.qqhxj.rxtx.SerialUtils;
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

/**
 * @author han1396735592
 */
@Slf4j
@Order(Integer.MIN_VALUE)
public class SerialPortRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        Map<String, Object> annotationAttributes = annotationMetadata
                .getAnnotationAttributes(EnableSerialPorts.class.getName(), false);
        Object value = annotationAttributes.get("value");
        if (value != null) {
            if (value instanceof AnnotationAttributes[]) {
                for (AnnotationAttributes attributes : ((AnnotationAttributes[]) value)) {
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
