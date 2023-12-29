package com.rxtx.controller;

import com.rxtx.entity.SerialEntity;
import com.rxtx.service.FaceService;
import org.opencv.videoio.VideoCapture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * description:
 *
 * @author zhangxin
 * @version 1.0
 * @date 2023/11/30 13:29
 */
@CrossOrigin
@RestController
@RequestMapping("/face")
public class FaceController {

    @Autowired
    private FaceService faceService;

    /**
     * 检测初始化
     */
    @GetMapping("/init")
    public Integer init(){
        //Integer res = faceService.initFd();
        VideoCapture vc = new VideoCapture(0);
        faceService.initFdStream(vc);
        vc.release();
        return null;
    }

    /**
     * 注册
     * @param userNo
     */
    //@GetMapping("/register")
    public void faceRegister(@RequestParam String userNo){
         //faceService.register(userNo);
         return;
    }


    /**
     * 验证
     */
    //@GetMapping("/detect")
    public void faceDetect(){
        //faceService.detect();
        return;
    }


    /**
     * FEATURE
     */
    //@GetMapping("/feature")
    public void feature(){
        faceService.getFeature();
        return;
    }
}
