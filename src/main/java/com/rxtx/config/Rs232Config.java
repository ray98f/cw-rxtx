package com.rxtx.config;

import com.rxtx.constant.CommonConstants;
import com.rxtx.entity.SerialEntity;
import com.rxtx.utils.SerialUtil;
import com.rxtx.websocket.SerialWebSocket;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TooManyListenersException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * description:
 *
 * @author zhangxin
 * @version 1.0
 * @date 2023/8/8 14:04
 */
@Slf4j
@Component
public class Rs232Config {
    private static final int DELAY_TIME = 1000;

    @Autowired
    SerialWebSocket socket;

    @Autowired
    SerialPortConfig config;

    /**
     * 缓存端口实例
     */
    public Map<String, SerialPort> serialPortMap = new ConcurrentHashMap<>(16);

    /**
     * 监听端口
     * @param serial
     */
    public boolean openPort(SerialEntity serial) {
        String portId = serial.getPortId();
        CommPortIdentifier commPortIdentifier = config.getSerialMap().get(portId);
        if (null != commPortIdentifier){
            SerialPort serialPort = null;
            int bitRate = 0,dataBit = 0,stopBit = 0,parity = 0;
            try {
                serialPort = (SerialPort) commPortIdentifier.open(portId,DELAY_TIME);
                // 设置监听器生效 当有数据时通知
                serialPort.notifyOnDataAvailable(true);
                // 比特率、数据位、停止位、奇偶校验位
                bitRate = serial.getBitRate();
                dataBit = serial.getDataBit();
                stopBit = serial.getStopBit();
                parity = SerialUtil.getParity(serial.getCheckBit());
                serialPort.setSerialPortParams(bitRate, dataBit, stopBit,parity);
            } catch (PortInUseException e) {
                log.error("Open CommPortIdentifier {} Exception:",serial.getPortId(),e );
                return false;
            } catch (UnsupportedCommOperationException e) {
                log.error("Set SerialPortParams BitRate {} DataBit {} StopBit {} Parity {} Exception:",bitRate,dataBit,stopBit,parity,e);
                return false;
            }

            // 设置当前串口的输入输出流
            InputStream input;
            OutputStream output;
            try {
                input = serialPort.getInputStream();
                output = serialPort.getOutputStream();
            } catch (IOException e) {
                log.error("Get serialPort data stream exception:",e);
                return false;
            }

            // 给当前串口添加一个监听器
            try {
                serialPort.addEventListener(new Serial232Listener(socket,portId,input,output,serial.getFormat()));
            } catch (TooManyListenersException e) {
                log.error("Get serialPort data stream exception:",e);
                return false;
            }
            serialPortMap.put(portId,serialPort);
            return true;
        }
        return false;
    }

    /**
     * 关闭端口
     * @param portId
     */
    public void closePort(String portId){
        SerialPort serialPort = serialPortMap.remove(portId);
        if (null != serialPort){
            serialPort.close();
        }
    }

    /**
     * 发送数据
     * @param portId
     * @param format
     * @param message
     */
    public void sendData(String portId,String format,String message){
        SerialPort serialPort = serialPortMap.get(portId);
        if (null == serialPort){
            return;
        }
        OutputStream output = null;
        try {
            byte[] bytes;
            if (CommonConstants.FORMAT_HEX.equals(format)){
                bytes = SerialUtil.hexToByte(message);
            } else {
                bytes = message.getBytes(StandardCharsets.UTF_8);
            }
            output = serialPort.getOutputStream();
            output.write(bytes);
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (null != output){
                try {
                    output.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
