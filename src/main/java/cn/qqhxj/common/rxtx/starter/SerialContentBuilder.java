package cn.qqhxj.common.rxtx.starter;

import cn.qqhxj.common.rxtx.SerialContext;
import cn.qqhxj.common.rxtx.SerialUtils;
import cn.qqhxj.common.rxtx.parse.SerialDataParser;
import cn.qqhxj.common.rxtx.processor.SerialByteDataProcesser;
import cn.qqhxj.common.rxtx.processor.SerialDataProcessor;
import cn.qqhxj.common.rxtx.reader.SerialReader;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Collection;

/**
 * @author han xinjian
 **/
@Slf4j
public class SerialContentBuilder implements InitializingBean {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private SerialPortEventListener serialPortEventListener;

    @Autowired
    private SerialPortProperties serialPortProperties;

    @Override
    public void afterPropertiesSet() throws Exception {

        SerialPort serialPort = null;
        try {
            serialPort = SerialUtils.connect(serialPortProperties.getPortName(), serialPortProperties.getBaudRate());
            serialPort.addEventListener(serialPortEventListener);
            serialPort.notifyOnDataAvailable(true);
            log.debug("configured SerialPort = {}", serialPort);
            SerialContext.setSerialPort(serialPort);
        } catch (Exception e) {
            log.warn("serial port is not configured");
        }

        Collection<SerialDataParser> serialDataParsers = applicationContext.getBeansOfType(SerialDataParser.class).values();
        for (SerialDataParser serialDataParser : serialDataParsers) {
            log.debug("configured serialDataParser = {}", serialDataParser);
        }
        SerialContext.getSerialDataParserSet().addAll(serialDataParsers);
        Collection<SerialDataProcessor> serialDataProcessors = applicationContext.getBeansOfType(SerialDataProcessor.class).values();
        for (SerialDataProcessor serialDataProcessor : serialDataProcessors) {
            log.debug("configured serialDataProcessor = {}", serialDataProcessor);
        }
        SerialContext.getSerialDataProcessorSet().addAll(serialDataProcessors);
        SerialContext.setSerialReader(applicationContext.getBean(SerialReader.class));
        SerialContext.setSerialByteDataProcesser(applicationContext.getBean(SerialByteDataProcesser.class));
    }

}
