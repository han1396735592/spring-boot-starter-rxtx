package cn.qqhxj.rxtx.starter;

import cn.qqhxj.rxtx.context.SerialPortConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author han1396735592
 **/
@Setter
@Getter
@ConfigurationProperties(prefix = SerialPortProperties.PREFIX)
public class SerialPortProperties {

    public final static String PREFIX = "serialport";

    /**
     * 自动连接 优先级最高
     */
    private boolean autoConnect = true;

    /**
     * 自动连接延时
     * 单位毫秒
     */
    private long autoConnectDelay = 0L;

    /**
     * 读数据超时时长
     */
    private int readTimeOut = 100;
    /**
     * 串口配置
     */
    private List<SerialPortConfig> config;


    @Override
    public String toString() {
        return "{autoConnect:" +
                autoConnect +
                ",autoConnectDelay:" +
                autoConnectDelay +
                ",readTimeOut:" +
                readTimeOut +
                "}";
    }
}
