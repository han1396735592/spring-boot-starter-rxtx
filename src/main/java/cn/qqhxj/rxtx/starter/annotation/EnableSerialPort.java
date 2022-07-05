package cn.qqhxj.rxtx.starter.annotation;

import cn.qqhxj.rxtx.starter.SerialPortRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author han1396735592
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(EnableSerialPorts.class)
@Import(SerialPortRegistrar.class)
public @interface EnableSerialPort {

    /**
     * 串口名称
     *
     * @return 注入容器的 `SerialContext` Bean名称
     */
    String value();

    String portName();

    /**
     * 波特率
     */
    int baudRate = 9600;

    /**
     * 数据位
     */
    int dataBits = gnu.io.SerialPort.DATABITS_8;

    /**
     * 停止位
     */
    int stopBits = gnu.io.SerialPort.STOPBITS_1;

    int parity = gnu.io.SerialPort.PARITY_NONE;

}
