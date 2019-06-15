package cn.qqhxj.common.rxtx.starter;

import gnu.io.SerialPort;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author han xinjian
 **/

@ConfigurationProperties(prefix = "serialport")
public class SerialPortProperties {

    private String portName;

    private int baudRate;

    private int dataBits = SerialPort.DATABITS_8;

    private int stopBits = SerialPort.STOPBITS_1;

    private int parity = SerialPort.PARITY_NONE;

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }


    public void setBaudRate(int baudRate) {
        this.baudRate = baudRate;
    }


    public int getBaudRate() {
        return baudRate;
    }

    public void setDataBits(int dataBits) {
        this.dataBits = dataBits;
    }

    public void setStopBits(int stopBits) {
        this.stopBits = stopBits;
    }

    public void setParity(int parity) {
        this.parity = parity;
    }

    public int getDataBits() {
        return dataBits;
    }

    public int getStopBits() {
        return stopBits;
    }

    public int getParity() {
        return parity;
    }
}
