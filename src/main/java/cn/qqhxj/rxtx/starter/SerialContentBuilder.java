package cn.qqhxj.rxtx.starter;

import cn.qqhxj.rxtx.context.SerialContext;
import cn.qqhxj.rxtx.event.DefaultCommPortDataListener;
import cn.qqhxj.rxtx.parse.CommPortDataParser;
import cn.qqhxj.rxtx.processor.CommPortByteDataProcessor;
import cn.qqhxj.rxtx.processor.CommPortDataProcessor;
import cn.qqhxj.rxtx.reader.AnyDataReader;
import cn.qqhxj.rxtx.reader.BaseCommPortReader;
import cn.qqhxj.rxtx.starter.annotation.SerialPortBinder;
import gnu.io.SerialPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.type.MethodMetadata;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author han1396735592
 **/
@Slf4j
public class SerialContentBuilder implements InitializingBean {
    @Autowired
    private ApplicationContext applicationContext;


    @Autowired
    private SerialPortProperties serialPortProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<SerialPortProperties.SerialPortConfig> configList = serialPortProperties.getConfig();
        if (configList != null) {
            configList.forEach(config -> {
                try {
                    SerialPortRegistrar.registerSerialContextBean(config, config.getPortName());
                } catch (Exception e) {
                    e.printStackTrace();
                    log.warn("serial port is not configured");
                }
            });
        }


        Map<String, SerialContext> stringSerialContextMap = applicationContext.getBeansOfType(SerialContext.class);
        Map<String, BaseCommPortReader> baseSerialReaderMap = applicationContext.getBeansOfType(BaseCommPortReader.class);
        Map<String, CommPortDataParser> serialDataParserMap = applicationContext.getBeansOfType(CommPortDataParser.class);
        Map<String, CommPortDataProcessor> serialDataProcessorMap = applicationContext.getBeansOfType(CommPortDataProcessor.class);
        Map<String, CommPortByteDataProcessor> serialByteDataProcessorMap = applicationContext.getBeansOfType(CommPortByteDataProcessor.class);

        baseSerialReaderMap.forEach((key, value) -> {
            //防虫重复添加
            if (value.getCommPort() == null) {
                Collection<SerialContext> serialContexts = filterSerialContext(stringSerialContextMap, key);
                for (SerialContext serialContext : serialContexts) {
                    serialContext.setSerialReader(value);
                }
            }

        });


        serialDataParserMap.forEach((key, value) -> {
            Collection<SerialContext> serialContexts = filterSerialContext(stringSerialContextMap, key);
            for (SerialContext serialContext : serialContexts) {
                Map<String, CommPortDataParser<?, SerialPort>> commPortDataParserMap = serialContext.getCommPortDataParserMap();
                try {
                    Method process = value.getClass().getMethod("parse", byte[].class);
                    String name = process.getReturnType().getName();
                    if (commPortDataParserMap.containsKey(name)) {
                        commPortDataParserMap.replace(name, value);
                        log.warn("重复");
                    } else {
                        commPortDataParserMap.put(name, value);
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }

            }
        });

        serialDataProcessorMap.forEach((key, value) -> {
            Collection<SerialContext> serialContexts = filterSerialContext(stringSerialContextMap, key);
            for (SerialContext serialContext : serialContexts) {
                Map<String, CommPortDataProcessor<?, SerialPort>> commPortDataProcessorMap = serialContext.getCommPortDataProcessorMap();
                try {
                    Method[] methods = value.getClass().getMethods();
                    for (Method method : methods) {
                        if ("process".equals(method.getName()) && !method.getReturnType().equals(Object.class) && method.getParameterTypes().length > 0) {
                            String name = method.getParameterTypes()[0].getName();
                            if (commPortDataProcessorMap.containsKey(name)) {
                                commPortDataProcessorMap.replace(name, value);
                                log.warn("重复");
                            } else {
                                commPortDataProcessorMap.put(name, value);
                            }

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        serialByteDataProcessorMap.forEach((key, value) -> {
            Collection<SerialContext> serialContexts = filterSerialContext(stringSerialContextMap, key);
            for (SerialContext serialContext : serialContexts) {
                serialContext.setSerialByteDataProcessor(value);
            }
        });


        for (SerialContext serialContext : stringSerialContextMap.values()) {
            serialContext.setCommPortEventListener(new DefaultCommPortDataListener(serialContext));
            if (serialContext.getSerialReader() == null) {
                serialContext.setSerialReader(new AnyDataReader());
            }
        }

    }


    public Collection<SerialContext> filterSerialContext(Map<String, SerialContext> serialContextMap, String beanName) {
        BeanDefinition beanDefinition = ((AnnotationConfigApplicationContext) applicationContext).getBeanFactory().getBeanDefinition(beanName);
        if (beanDefinition instanceof AnnotatedBeanDefinition) {
            AnnotatedBeanDefinition annotatedBeanDefinition = (AnnotatedBeanDefinition) beanDefinition;
            MethodMetadata factoryMethodMetadata = annotatedBeanDefinition.getFactoryMethodMetadata();
            Map<String, Object> defaultAttrs = factoryMethodMetadata.getAnnotationAttributes(SerialPortBinder.class.getName(), false);
            if (defaultAttrs != null && defaultAttrs.containsKey("value")) {
                ArrayList<SerialContext> serialContextArrayList = new ArrayList<>();
                String name = String.valueOf(defaultAttrs.get("value"));
                SerialContext serialContext = serialContextMap.get(name + "." + SerialContext.class.getSimpleName());
                if (serialContext != null) {
                    serialContextArrayList.add(serialContext);
                }
                return serialContextArrayList;
            }
        }
        return serialContextMap.values();
    }

}
