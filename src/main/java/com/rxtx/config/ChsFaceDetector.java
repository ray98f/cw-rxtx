package com.rxtx.config;

import com.chs_vision_faces.sdk.Chs_FaceDetector_Java;
import org.opencv.core.Mat;

/**
 * description:
 *
 * @author zhangxin
 * @version 1.0
 * @date 2023/12/23 14:10
 */
public class ChsFaceDetector extends Chs_FaceDetector_Java {
    private static Mat image = new Mat();
    private long ptrFaceDetectorHandler;
    public synchronized static Mat getMatInstance (){
        if(image == null){
            image = new Mat();
        }
        return image;
    }
    public long getPtrFaceDetectorHandler() {
        return ptrFaceDetectorHandler;
    }

    public void setPtrFaceDetectorHandler(long ptrFaceDetectorHandler) {
        this.ptrFaceDetectorHandler = ptrFaceDetectorHandler;
    }
}
