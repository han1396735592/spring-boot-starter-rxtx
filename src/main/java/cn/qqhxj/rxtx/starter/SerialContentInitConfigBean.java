package cn.qqhxj.rxtx.starter;

import cn.qqhxj.rxtx.context.SerialContext;
import cn.qqhxj.rxtx.event.SerialContextEventDispatcher;
import cn.qqhxj.rxtx.event.SerialContextEventListener;
import cn.qqhxj.rxtx.parse.SerialDataParser;
import cn.qqhxj.rxtx.processor.SerialByteDataProcessor;
import cn.qqhxj.rxtx.processor.SerialDataProcessor;
import cn.qqhxj.rxtx.reader.AnyDataReader;
import cn.qqhxj.rxtx.reader.SerialReader;
import cn.qqhxj.rxtx.starter.annotation.SerialPortBinder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author han1396735592
 **/
@Slf4j
@Configuration
@EnableConfigurationProperties({SerialPortProperties.class})
public class SerialContentInitConfigBean implements InitializingBean {
    @Autowired
    private GenericApplicationContext applicationContext;

    @Autowired
    private SerialPortProperties serialPortProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, SerialContext> stringSerialContextMap = applicationContext.getBeansOfType(SerialContext.class);
        Map<String, SerialReader> baseSerialReaderMap = applicationContext.getBeansOfType(SerialReader.class);
        Map<String, SerialDataParser> serialDataParserMap = applicationContext.getBeansOfType(SerialDataParser.class);
        Map<String, SerialDataProcessor> serialDataProcessorMap = applicationContext.getBeansOfType(SerialDataProcessor.class);
        Map<String, SerialByteDataProcessor> serialByteDataProcessorMap = applicationContext.getBeansOfType(SerialByteDataProcessor.class);
        Map<String, SerialContextEventListener> serialContextEventListenerMap = applicationContext.getBeansOfType(SerialContextEventListener.class);

        baseSerialReaderMap.forEach((key, value) -> {
            //防止重复添加
            if (value.setSerialContext() == null) {
                Collection<SerialContext> serialContexts = filterSerialContext(stringSerialContextMap, key);
                for (SerialContext serialContext : serialContexts) {
                    serialContext.setSerialReader(value);
                }
            }
        });

        serialDataParserMap.forEach((key, value) -> {
            Collection<SerialContext> serialContexts = filterSerialContext(stringSerialContextMap, key);
            for (SerialContext serialContext : serialContexts) {
                serialContext.addSerialDataParser(value);
            }
        });

        serialDataProcessorMap.forEach((key, value) -> {
            Collection<SerialContext> serialContexts = filterSerialContext(stringSerialContextMap, key);
            for (SerialContext serialContext : serialContexts) {
                serialContext.addSerialDataProcessor(value);
            }
        });


        serialByteDataProcessorMap.forEach((key, value) -> {
            Collection<SerialContext> serialContexts = filterSerialContext(stringSerialContextMap, key);
            for (SerialContext serialContext : serialContexts) {
                serialContext.setSerialByteDataProcessor(value);
            }
        });

        HashMap<SerialContext, SerialContextEventListener> hashMap = new HashMap<>();
        serialContextEventListenerMap.forEach((key, value) -> {
            Collection<SerialContext> serialContexts = filterSerialContext(stringSerialContextMap, key);
            for (SerialContext serialContext : serialContexts) {
                hashMap.put(serialContext, value);
            }
        });


        for (SerialContext serialContext : stringSerialContextMap.values()) {
            if (serialContext.getSerialReader() == null) {
                serialContext.setSerialReader(new AnyDataReader());
            }

            SerialContextEventListener serialContextEventListener = hashMap.get(serialContext);
            serialContext.setSerialContextEventListener(serialContextEventListener);
            if (serialPortProperties.isAutoConnect() && serialContext.getSerialPortConfig().isAutoConnect()) {
                serialContext.connect();
            }
        }
    }


    public Collection<SerialContext> filterSerialContext(Map<String, SerialContext> serialContextMap, String beanName) {
        BeanDefinition beanDefinition = applicationContext.getBeanFactory().getBeanDefinition(beanName);
        if (beanDefinition instanceof AnnotatedBeanDefinition) {
            AnnotatedBeanDefinition annotatedBeanDefinition = (AnnotatedBeanDefinition) beanDefinition;
            MethodMetadata factoryMethodMetadata = annotatedBeanDefinition.getFactoryMethodMetadata();
            if (factoryMethodMetadata != null) {
                return getSerialContextListByAnnotatedMetadata(factoryMethodMetadata, serialContextMap, serialContextMap.values());
            } else {
                AnnotationMetadata metadata = annotatedBeanDefinition.getMetadata();
                return getSerialContextListByAnnotatedMetadata(metadata, serialContextMap, serialContextMap.values());
            }
        }
        return serialContextMap.values();
    }

    private Collection<SerialContext> getSerialContextListByAnnotatedMetadata(AnnotatedTypeMetadata annotatedTypeMetadata, Map<String, SerialContext> serialContextMap, Collection<SerialContext> allSerialContextList) {
        Map<String, Object> defaultAttrs = annotatedTypeMetadata.getAnnotationAttributes(SerialPortBinder.class.getName(), false);
        if (defaultAttrs != null && defaultAttrs.containsKey("value")) {
            ArrayList<SerialContext> serialContextArrayList = new ArrayList<>();
            String name = String.valueOf(defaultAttrs.get("value"));
            SerialContext serialContext = serialContextMap.get(name + "." + SerialContext.class.getSimpleName());
            if (serialContext != null) {
                serialContextArrayList.add(serialContext);
            }
            return serialContextArrayList;
        }
        return allSerialContextList;
    }

}
