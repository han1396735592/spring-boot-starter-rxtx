package cn.qqhxj.rxtx.starter;

import gnu.io.SerialPort;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author han1396735592
 **/
@Data
@ConfigurationProperties(prefix = SerialPortProperties.prefix)
public class SerialPortProperties {

    public final static String prefix = "serialport";

    private List<SerialPortConfig> config;

    @Data
    public static class SerialPortConfig {
        private String portName;
        private String alias;

        private int baudRate = 9600;

        private int dataBits = SerialPort.DATABITS_8;

        private int stopBits = SerialPort.STOPBITS_1;

        private int parity = SerialPort.PARITY_NONE;
    }

}
