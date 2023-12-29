package com.rxtx.config;

import com.chs_vision_faces.sdk.Chs_FaceFeature_Java;

/**
 * description:
 *
 * @author zhangxin
 * @version 1.0
 * @date 2023/12/23 14:18
 */
public class ChsFaceFeature extends Chs_FaceFeature_Java {

    long ptrFeature;

    public long getPtrFeature() {
        return ptrFeature;
    }

    public void setPtrFeature(long ptrFeature) {
        this.ptrFeature = ptrFeature;
    }
}
