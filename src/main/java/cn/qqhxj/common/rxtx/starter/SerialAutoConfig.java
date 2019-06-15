package cn.qqhxj.common.rxtx.starter;

import cn.qqhxj.common.rxtx.DefaultSerialDataListener;
import cn.qqhxj.common.rxtx.parse.StringSerialDataParser;
import cn.qqhxj.common.rxtx.processor.SerialByteDataProcessor;
import cn.qqhxj.common.rxtx.reader.SerialReader;
import cn.qqhxj.common.rxtx.reader.VariableLengthSerialReader;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author han xinjian
 **/
@Slf4j
@Configuration
@EnableConfigurationProperties(SerialPortProperties.class)
@ConditionalOnClass(SerialPort.class)
public class SerialAutoConfig {

    @Autowired
    private SerialPortProperties serialPortProperties;

    @Bean
    @ConditionalOnMissingBean
    public SerialPortEventListener serialPortEventListener() {
        SerialPortEventListener dataListener = new DefaultSerialDataListener();
        log.debug("config SerialPortEventListener = {}", dataListener);
        return dataListener;
    }


    @Bean
    @ConditionalOnMissingBean
    public SerialReader serialReader() {
        SerialReader serialReader = null;
        serialReader = new VariableLengthSerialReader('{', '}');
        log.debug("config SerialReader ={}", serialReader);
        return serialReader;
    }

    @Bean
    @ConditionalOnMissingBean
    public SerialByteDataProcessor serialByteDataProcesser() {
        return new SerialByteDataProcessor() {
            @Override
            public void process(byte[] bytes) {
                log.debug("Received Data:{}", bytesToHexString(bytes));
            }

            public String bytesToHexString(byte[] bArr) {
                StringBuffer sb = new StringBuffer(bArr.length);
                String sTmp;
                for (int i = 0; i < bArr.length; i++) {
                    sTmp = Integer.toHexString(0xFF & bArr[i]);
                    if (sTmp.length() < 2) {
                        sb.append(0);
                    }
                    sb.append(sTmp.toUpperCase() + "  ");
                }
                return sb.toString();
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public StringSerialDataParser stringSerialDataParser() {
        return new StringSerialDataParser();
    }

    @Bean
    public SerialContentBuilder serialContentBuilder() {
        return new SerialContentBuilder();
    }

}
