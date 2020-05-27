package cn.qqhxj.common.rxtx.starter;

import gnu.io.SerialPort;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author han xinjian
 **/
@Data
@ConfigurationProperties(prefix = SerialPortProperties.prefix)
public class SerialPortProperties {

    public final static String prefix = "serialport";

    private List<SerialPortConfig> config;

    @Data
    public static class SerialPortConfig {
        private String portName;

        private int baudRate;

        private int dataBits = SerialPort.DATABITS_8;

        private int stopBits = SerialPort.STOPBITS_1;

        private int parity = SerialPort.PARITY_NONE;

        String readerClass = "cn.qqhxj.common.rxtx.reader.VariableLengthSerialReader";

        private Object[] readerConfig = new Object[]{"{", "}"};

        String parseClass = "cn.qqhxj.common.rxtx.parse.StringSerialDataParser";

        private Object[] parseConfig = new Object[0];


    }

}
