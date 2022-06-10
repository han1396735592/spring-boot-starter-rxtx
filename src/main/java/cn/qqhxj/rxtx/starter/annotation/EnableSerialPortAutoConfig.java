package cn.qqhxj.rxtx.starter.annotation;

import cn.qqhxj.rxtx.starter.SerialPortAutoConfigureImportSelector;
import gnu.io.SerialPort;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author han1396735592
 * @date 2022/6/9 14:46
 */
@Import({SerialPortAutoConfigureImportSelector.class})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@ConditionalOnClass(SerialPort.class)
@Target({ElementType.TYPE})
public @interface EnableSerialPortAutoConfig {
}
