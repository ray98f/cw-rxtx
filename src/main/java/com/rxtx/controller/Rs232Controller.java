package com.rxtx.controller;

import com.rxtx.config.Rs232Config;
import com.rxtx.entity.SerialEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * description:
 *
 * @author zhangxin
 * @version 1.0
 * @date 2023/8/8 14:00
 */
@CrossOrigin
@RestController
@RequestMapping("/serial/232")
public class Rs232Controller {

    @Autowired
    Rs232Config rs232Config;

    /**
     * 监听端口
     * @param serial
     */
    @PostMapping("/open")
    public boolean open(@RequestBody SerialEntity serial){
        return rs232Config.openPort(serial);
    }

    /**
     * 获取端口列表
     * @return
     */
    @GetMapping("/close/{portId}")
    public void close(@PathVariable("portId") String portId){
        rs232Config.closePort(portId);
    }

    /**
     * 获取端口列表
     * @return
     */
    @GetMapping("/send/{portId}/{format}/{msg}")
    public void close(@PathVariable("portId") String portId,@PathVariable("format") String format,@PathVariable("msg") String msg){
        rs232Config.sendData(portId,format,msg);
    }
}
