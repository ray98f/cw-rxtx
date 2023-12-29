package com.rxtx.config;

import gnu.io.CommPortIdentifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * description:
 *
 * @author zhangxin
 * @version 1.0
 * @date 2023/8/8 14:00
 */
@Slf4j
@Component
public class SerialPortConfig {
    /**
     * 缓存端口信息
     */
    private static Map<String, CommPortIdentifier> serialMap;

    @PostConstruct
    private void init(){
        refreshCom();
    }

    /**
     * 刷新端口
     */
    public void refreshCom(){
        Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();
        CommPortIdentifier serial;
        Map<String,CommPortIdentifier> temp = new ConcurrentHashMap<>(16);
        while (portList.hasMoreElements()){
            serial = portList.nextElement();
            if (serial.getPortType() == CommPortIdentifier.PORT_SERIAL){
                temp.put(serial.getName(),serial);
            }
        }
        serialMap = Collections.unmodifiableMap(temp);
    }

    /**
     * 获取端口列表
     * @return
     */
    public List<String> getSerialPortList(){
        return serialMap.keySet().stream().sorted().collect(Collectors.toList());
    }

    /**
     * 获取串口
     * @return
     */
    public Map<String, CommPortIdentifier> getSerialMap(){
        return serialMap;
    }
}
