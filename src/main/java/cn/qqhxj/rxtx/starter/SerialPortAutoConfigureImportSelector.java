package cn.qqhxj.rxtx.starter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author han1396735592
 */
@Slf4j
@Order(Integer.MIN_VALUE)
public class SerialPortAutoConfigureImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        log.info("Enable SerialPortAutoConfigure");
        return new String[]{SerialAutoConfig.class.getName(),SerialPortProperties.class.getName(),SerialContentBuilder.class.getName()};
    }


}
