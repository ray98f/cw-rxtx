package com.rxtx.config;

import com.rxtx.utils.Crc16Modbus;
import com.rxtx.utils.SerialUtil;
import com.rxtx.websocket.SerialWebSocket;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * description:
 *
 * @author zhangxin
 * @version 1.0
 * @date 2023/8/8 14:07
 */

public class Serial232Listener implements SerialPortEventListener {


    SerialWebSocket serialwebsocket;
    String portId;
    InputStream inputStream;
    OutputStream outputStream;
    String format;

    public Serial232Listener(SerialWebSocket socket,String port,InputStream input, OutputStream output, String format){
        serialwebsocket = socket;
        portId = port;
        inputStream = input;
        outputStream = output;
        this.format = format;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        switch (event.getEventType()) {
            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            case SerialPortEvent.DATA_AVAILABLE:
                // 当有可用数据时读取数据
                byte[] readBuffer = null;
                int availableBytes = 0;
                try {
                    availableBytes = inputStream.available();
                    while (availableBytes > 0) {
                        readBuffer = SerialUtil.readFromPort(inputStream);
                        String needData = Crc16Modbus.byteTo16String(readBuffer);
                        serialwebsocket.broadcast(portId,needData);
                        availableBytes = inputStream.available();
                    }
                } catch (IOException e) {
                }
            default:
                break;
        }
    }

}
