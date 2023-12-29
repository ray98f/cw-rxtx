package com.rxtx.service;

import com.rxtx.dto.res.UserFaceFeatureResDTO;
import org.opencv.videoio.VideoCapture;

import java.util.HashMap;
import java.util.List;

/**
 * description:
 *
 * @author zhangxin
 * @version 1.0
 * @date 2023/11/30 13:31
 */
public interface FaceService {

    Integer initFd(VideoCapture vc);
    String initFdStream(VideoCapture vc);
    List<HashMap<String,String>> register(VideoCapture vc,String userNo);
    UserFaceFeatureResDTO detect(VideoCapture vc);
    void getFeature();

}
