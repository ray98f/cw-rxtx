package com.rxtx.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务心跳
 * @author  Ray
 * @version 1.0
 * @date 2024/01/09
 */
@CrossOrigin
@RestController
public class PingController {


    /**
     * 服务心跳接口
     * @return 标识
     */
    @GetMapping("/ping")
    public String ping() {
        return "1";
    }
}
