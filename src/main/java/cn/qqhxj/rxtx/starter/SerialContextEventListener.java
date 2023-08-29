package cn.qqhxj.rxtx.starter;

import cn.qqhxj.rxtx.context.AbstractSerialContext;
import gnu.io.SerialPortEvent;

/**
 * @author han1396735592
 * @date 2023/8/29 11:43
 */

public interface SerialContextEventListener {

    /**
     * connectError
     *
     * @param serialContext
     */
    default void connectError(AbstractSerialContext serialContext) {

    }

    /**
     * hardwareError
     *
     * @param serialContext
     */
    default void hardwareError(AbstractSerialContext serialContext) {
    }

    /**
     * serialEvent
     *
     * @param serialContext
     * @param ev
     */
    default void serialEvent(AbstractSerialContext serialContext, SerialPortEvent ev) {
    }

    /**
     * connected
     *
     * @param serialContext
     */
    default void connected(AbstractSerialContext serialContext) {
    }

    /**
     * disconnected
     *
     * @param serialContext
     */
    default void disconnected(AbstractSerialContext serialContext) {
    }
}
