package cn.qqhxj.common.rxtx.starter;

import cn.qqhxj.common.rxtx.SerialContext;
import cn.qqhxj.common.rxtx.SerialUtils;
import cn.qqhxj.common.rxtx.parse.SerialDataParser;
import cn.qqhxj.common.rxtx.processor.SerialDataProcessor;
import gnu.io.SerialPort;
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

//    @Autowired
//    private SerialPortEventListener serialPortEventListener;

    @Autowired
    private SerialPortProperties serialPortProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        Collection<SerialDataParser> serialDataParsers = applicationContext.getBeansOfType(SerialDataParser.class).values();
        for (SerialDataParser serialDataParser : serialDataParsers) {
            log.debug("configured serialDataParser = {}", serialDataParser);
        }
        Collection<SerialDataProcessor> serialDataProcessors = applicationContext.getBeansOfType(SerialDataProcessor.class).values();

        for (SerialDataProcessor serialDataProcessor : serialDataProcessors) {
            log.debug("configured serialDataProcessor = {}", serialDataProcessor);
        }

        serialPortProperties.getConfig().forEach(config->{
            SerialContext context = new SerialContext();

            SerialPort serialPort = null;
            try {
                serialPort = SerialUtils.connect(config.getPortName(),
                        config.getBaudRate(),
                        config.getDataBits(),
                        config.getStopBits(),
                        config.getParity());
                log.debug("configured SerialPort = {}", serialPort);
                log.debug("SerialPort parameter  portName = {},baudRate={}, dataBits={},stopBits ={},parity={}",
                        config.getPortName(),
                        config.getBaudRate(),
                        config.getDataBits(),
                        config.getStopBits(),
                        config.getParity()
                );
                context.setSerialPort(serialPort);
                serialDataParsers.stream().filter(item->{


                    return true;
                });





            } catch (Exception e) {
                log.warn("serial port is not configured");
            }
        });



//        SerialContext.setSerialPortEventListener(serialPortEventListener);
//
//        SerialContext.getSerialDataParserSet().addAll(serialDataParsers);
//
//        SerialContext.getSerialDataProcessorSet().addAll(serialDataProcessors);
//        SerialContext.setSerialReader(applicationContext.getBean(SerialReader.class));
//        SerialContext.setSerialByteDataProcessor(applicationContext.getBean(SerialByteDataProcessor.class));
    }

}
