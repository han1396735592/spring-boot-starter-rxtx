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
     * 串口别名
     *
     * @return 注入容器的 `SerialContext` Bean名称
     */

    String value() default "";

    String port();

    /**
     * 错误自动连接
     * 连接错误重连+断开错误重连
     */
    boolean errorAutoConnect() default true;

    int reconnectInterval() default 10000;

    /**
     * 波特率
     */
    int baud() default 115200;

    /**
     * 数据位
     */
    int dataBits() default gnu.io.SerialPort.DATABITS_8;

    /**
     * 停止位
     */
    int stopBits() default gnu.io.SerialPort.STOPBITS_1;

    int parity() default gnu.io.SerialPort.PARITY_NONE;

}
