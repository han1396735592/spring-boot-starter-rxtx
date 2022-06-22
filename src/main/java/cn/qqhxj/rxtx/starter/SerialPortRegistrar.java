package cn.qqhxj.rxtx.starter;

import cn.qqhxj.rxtx.context.SerialContext;
import cn.qqhxj.rxtx.util.SerialUtils;
import cn.qqhxj.rxtx.starter.annotation.EnableSerialPort;
import gnu.io.SerialPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author han1396735592
 * @date 2022/6/9 16:31
 */
@Slf4j
@Order(Integer.MIN_VALUE)
public class SerialPortRegistrar implements ImportBeanDefinitionRegistrar {

    private static BeanDefinitionRegistry beanDefinitionRegistry = null;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        System.out.println("==================");
        Map<String, Object> defaultAttrs = annotationMetadata
                .getAnnotationAttributes(EnableSerialPort.class.getName(), false);
        SerialPortProperties.SerialPortConfig serialPortConfig = new SerialPortProperties.SerialPortConfig();
        String beanName = String.valueOf(defaultAttrs.get("value"));
        assert defaultAttrs != null;
        defaultAttrs.remove("value");
        defaultAttrs.forEach((k, v) -> {
            try {
                Field field = SerialPortProperties.SerialPortConfig.class.getDeclaredField(k);
                field.setAccessible(true);
                field.set(serialPortConfig, v);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        SerialPortRegistrar.beanDefinitionRegistry= beanDefinitionRegistry;
        registerSerialContextBean(serialPortConfig, beanName);
    }

    public static void registerSerialContextBean(SerialPortProperties.SerialPortConfig serialPortConfig, String beanName) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
