package cn.qqhxj.common.rxtx.starter;

import cn.qqhxj.common.rxtx.DefaultSerialDataListener;
import cn.qqhxj.common.rxtx.HexUtil;
import cn.qqhxj.common.rxtx.processor.SerialByteDataProcessor;
import cn.qqhxj.common.rxtx.reader.SerialReader;
import cn.qqhxj.common.rxtx.reader.VariableLengthSerialReader;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author han xinjian
 **/
@Slf4j
@Configuration
@EnableConfigurationProperties(SerialPortProperties.class)
@ConditionalOnClass(SerialPort.class)
public class SerialAutoConfig {

    @Primary
    @Bean
    public SerialPortEventListener serialPortEventListener() {
        SerialPortEventListener dataListener = new DefaultSerialDataListener();
        log.debug("config SerialPortEventListener = {}", dataListener);
        return dataListener;
    }

    @Primary
    @Bean
    public SerialReader serialReader() {
        SerialReader serialReader = new VariableLengthSerialReader('{', '}');
        log.debug("config SerialReader ={}", serialReader);
        return serialReader;
    }

    @Primary
    @Bean
    public SerialByteDataProcessor serialByteDataProcessor() {
        return bytes -> log.debug("Received Data:{}", HexUtil.bytesToHexString(bytes));
    }

    @Primary
    @Bean
    public SerialContentBuilder serialContentBuilder() {
        return new SerialContentBuilder();
    }

}
