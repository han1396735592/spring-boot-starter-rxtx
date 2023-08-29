package cn.qqhxj.rxtx.starter;

import cn.qqhxj.rxtx.HexUtil;
import cn.qqhxj.rxtx.context.SerialPortConfig;
import cn.qqhxj.rxtx.processor.SerialByteDataProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author han1396735592
 **/
@Slf4j
@Configuration
@EnableConfigurationProperties({SerialPortProperties.class})
@Order(Integer.MIN_VALUE)
public class SerialAutoConfig implements BeanPostProcessor {


    @Autowired
    private GenericApplicationContext applicationContext;

    @Autowired
    private SerialPortProperties serialPortProperties;

    @PostConstruct
    public void init() {
        log.info("serialPortProperties={}", serialPortProperties);
        List<SerialPortConfig> configList = serialPortProperties.getConfig();
        if (configList != null) {
            configList.forEach(config -> {
                try {
                    if (StringUtils.isEmpty(config.getAlias())) {
                        config.setAlias(config.getPort());
                    }
                    SerialPortRegistrar.registerSerialContextBean(applicationContext, config, config.getAlias());
                } catch (Exception e) {
                    e.printStackTrace();
                    log.warn("serial port is not configured");
                }
            });
        }
    }

    @Bean
    @ConditionalOnMissingBean()
    public SerialByteDataProcessor serialByteDataProcessor() {
        return (bytes, content) -> log.debug("[{}] Received Data:{}", content.getSerialPortConfig().getAlias(), HexUtil.bytesToHexString(bytes));
    }
}
