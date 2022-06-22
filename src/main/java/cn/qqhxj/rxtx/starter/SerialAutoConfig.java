package cn.qqhxj.rxtx.starter;

import cn.qqhxj.rxtx.util.HexUtil;
import cn.qqhxj.rxtx.processor.CommPortByteDataProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author han1396735592
 **/
@Slf4j
@Configuration
public class SerialAutoConfig {

    @Bean
    @ConditionalOnMissingBean()
    public CommPortByteDataProcessor serialByteDataProcessor() {
        return bytes -> log.debug("Received Data:{}", HexUtil.bytesToHexString(bytes));
    }

    @Bean
    public SerialContentBuilder serialContentBuilder() {
        return new SerialContentBuilder();
    }

}
