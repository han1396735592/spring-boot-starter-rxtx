package cn.qqhxj.rxtx.starter.annotation;

import cn.qqhxj.rxtx.starter.SerialPortRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;
/**
 * @author han1396735592
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
@Import(SerialPortRegistrar.class)
public @interface EnableSerialPorts {
    EnableSerialPort[] value();
}


