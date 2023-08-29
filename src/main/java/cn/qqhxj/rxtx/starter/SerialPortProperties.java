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
     * 错误自动连接
     * 连接错误重连+断开错误重连
     */
    private boolean errorAutoConnect = true;

    /**
     * 自动连接 优先级最高
     */
    private boolean autoConnect = true;

    /**
     * 自动重间隔
     * 单位毫秒
     */
    private Long autoReconnectInterval = 10000L;

    /**
     * 串口配置
     */
    private List<SerialPortConfig> config;


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{errorAutoConnect:");
        sb.append(errorAutoConnect);
        sb.append(",autoConnect:");
        sb.append(autoConnect);
        sb.append(",autoReconnectInterval:");
        sb.append(autoReconnectInterval);
        sb.append(",config:");
        sb.append(config.toString());
        sb.append("}");
        return sb.toString();
    }
}
