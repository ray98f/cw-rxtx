package com.rxtx.service.impl;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chs_vision_faces.sdk.*;
import com.rxtx.config.ChsFaceDetector;
import com.rxtx.config.ChsFaceFeature;
import com.rxtx.constant.CommonConstants;
import com.rxtx.dto.res.UserFaceFeatureResDTO;
import com.rxtx.enums.ErrorCode;
import com.rxtx.exception.CommonException;
import com.rxtx.service.FaceService;
import com.rxtx.utils.FaceUtils;
import com.rxtx.utils.SpringUtils;
import com.rxtx.utils.ThreadUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

import static org.opencv.imgcodecs.Imgcodecs.imread;

/**
 * description:
 *
 * @author zhangxin
 * @version 1.0
 * @date 2023/11/30 13:32
 */
@Service
@Slf4j
public class FaceServiceImpl implements FaceService {

    @Resource(name = "detectFaceDetector")
    private ChsFaceDetector detectFaceDetector;

    @Resource(name = "detectFaceFeature")
    private ChsFaceFeature detectFaceFeature;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${opencv.image-path}")
    private String imgPath;

    @Override
    public Integer initFd(VideoCapture vc) {
        return detectFaceInit(vc);
    }

    @Override
    public String initFdStream(VideoCapture vc) {
        try{
            Mat image = new Mat();
            vc.read(image);
            BufferedImage bfImg = FaceUtils.mat2BI(image);
            image.release();
            if(bfImg != null){
                return FaceUtils.BufferedImageToBase64(bfImg);
            }
        }catch (Exception e){
            return null;
        }
        return null;
    }

    @Override
    public List<HashMap<String, String>> register(VideoCapture vc,String userNo) {
        return captureImage(vc,userNo);
    }

    @Override
    public UserFaceFeatureResDTO detect(VideoCapture vc)  {
        int num = CommonConstants.FACE_COMPARE_TIMES;
        while(num > 0){
            UserFaceFeatureResDTO dto = faceCompareForN(vc);
            if (dto != null){
                return dto;
            }
            num--;
        }
        return null;
    }

    @Override
    public void getFeature() {}

    private List<HashMap<String,String>> captureImage(VideoCapture vc,String userNo){

        String userDir = imgPath + userNo;
        if (!FileUtil.isDirectory(userDir)) {
            FileUtil.mkdir(userDir);
        }
        int num = CommonConstants.FACE_DEF_NUM;
        List<String> images = new ArrayList<>();
        long enterTime = System.currentTimeMillis();
        while(num > 0){
            Mat image = new Mat();
            vc.read(image);
            Chs_FacePos[] pos_source = new Chs_FacePos[1];
            byte[] source_data= new byte[image.cols()*image.rows()*image.channels()];
            image.get(0, 0,source_data);
            int nSourceFaces = detectFaceDetector.Chs_Fd_DetectFace(detectFaceDetector.getPtrFaceDetectorHandler(),
                    source_data, image.channels(), image.cols(), image.rows(), pos_source,1, 360);
            if(nSourceFaces > 0) {
                String fileName = userDir + CommonConstants.SEPRATOR + num + CommonConstants.FACE_IMAGE_SUBFIX;
                Imgcodecs.imwrite(fileName, image);
                image.release();
                images.add(fileName);
                num --;
                ThreadUtils.sleep(CommonConstants.FACE_IMAGE_READ_INTERVAL);
            }
            if((System.currentTimeMillis() - enterTime) > CommonConstants.FACE_REG_TIME_OUT){
                image.release();
                return faceRegis(images);
            }
            image.release();
        }
        return faceRegis(images);
    }

    private List<HashMap<String,String>>  faceRegis(List<String> images)  {

        //Chs_Fr_OpenOneToManyDb
        int nRet = detectFaceFeature.Chs_Fr_OpenOneToManyDb(detectFaceFeature.getPtrFeature(),CommonConstants.FACE_DB.toCharArray());
        if(nRet != 0) {
            log.error("Failed to init face feature!");
            return null;
        }
        if(images == null || images.size() == 0){
            return null;
        }
        //4 5 6 7
        List<HashMap<String,String>> featureList = new ArrayList<>();
        for(String image: images){
            HashMap<String,String> extraRes = faceExtra(detectFaceFeature.getPtrFeature(),
                    detectFaceDetector.getPtrFaceDetectorHandler(),detectFaceDetector,detectFaceFeature,image);
            featureList.add(extraRes);
        }

        //8 Chs_Fr_CloseOneToManyDb
        detectFaceFeature.Chs_Fr_CloseOneToManyDb(detectFaceFeature.getPtrFeature());

        return featureList;
    }

    private HashMap<String,String> faceExtra(long ptrFeature ,
                                                    long ptrFaceDetectorHandler,
                                                    Chs_FaceDetector_Java objFaceDetector,
                                                    Chs_FaceFeature_Java objFeature,
                                                    String image) {
        Mat img_source = imread(image);
        if(img_source.empty()) {
            log.error("Failed to read source image!");
            return null;
        }
        Chs_FacePos pos_source[] = new Chs_FacePos[1];
        byte[] source_data= new byte[img_source.cols()*img_source.rows()*img_source.channels()];
        img_source.get(0, 0,source_data);
        int nSourceFaces = objFaceDetector.Chs_Fd_DetectFace(ptrFaceDetectorHandler, source_data, img_source.channels(),
                img_source.cols(), img_source.rows(), pos_source,1, 360);
        if(nSourceFaces <= 0) {
            log.error("Source image have No face detected!");
            return null;
        }

        //Chs_Fr_Extract
        int nFeature_size = objFeature.Chs_Fr_Size(ptrFeature);
        byte[] Feature_Soure = new byte[nFeature_size];
        int nRet=objFeature.Chs_Fr_Extract(ptrFeature, source_data, img_source.cols(), img_source.rows(), img_source.channels(), pos_source[0], Feature_Soure);
        if(nRet != 0) {
            log.error("Failed to extract feature from source image!");
            return null;
        }

        //Chs_Fr_GetNextFeatureID
        int nextId = objFeature.Chs_Fr_GetNextFeatureID(ptrFeature);
        if(nextId <= 0){
            log.error("Failed to extract feature from source image!");
            return null;
        }

        //Chs_Fr_AddFeature
        nRet = objFeature.Chs_Fr_AddFeature(ptrFeature,nextId,Feature_Soure,"userCode".toCharArray());
        if(nRet != 0) {
            log.error("Failed to extract feature from source image!");
            return null;
        }
        HashMap<String,String> res = new HashMap<>();
        String testStr = Base64.encodeBase64String(Feature_Soure);
        byte[] testByte = Base64.decodeBase64(testStr);
        res.put("id", nextId+"");
        res.put("feature", Base64.encodeBase64String(Feature_Soure));
        return res;

    }

    private UserFaceFeatureResDTO faceCompareForN(VideoCapture vc){
        System.out.println("enter faceCompareForN " + System.currentTimeMillis() + " thread is:  " + Thread.currentThread());
        UserFaceFeatureResDTO res = null;
        int nRet = 0;
        int nFeature_size = detectFaceFeature.Chs_Fr_Size(detectFaceFeature.getPtrFeature());

        //待检测
        Mat img_des = new Mat();
        vc.read(img_des);

        //Chs_Fr_Extract
        if(img_des.empty()) {
            ThreadUtils.sleep(CommonConstants.FACE_DETECT_INTERVAL);
            log.error("Failed to read source image!");
            img_des.release();
            return res;
        }
        Chs_FacePos[] pos_dest = new Chs_FacePos[1];
        byte[] dest_data = new byte[img_des.cols()*img_des.rows()*img_des.channels()];
        img_des.get(0,0,dest_data);
        int nDesFaces = detectFaceDetector.Chs_Fd_DetectFace(detectFaceDetector.getPtrFaceDetectorHandler(), dest_data, img_des.channels(), img_des.cols(), img_des.rows(), pos_dest, 1, 360);
        //是否存在人脸
        if(nDesFaces <= 0) {
            ThreadUtils.sleep(CommonConstants.FACE_DETECT_INTERVAL);
            log.error("dest image have No face detected!");
            img_des.release();
            img_des = null;
            return res;
        }
        byte[] Feature_dest=new byte[nFeature_size];
        nRet = detectFaceFeature.Chs_Fr_Extract(detectFaceFeature.getPtrFeature(), dest_data, img_des.cols(),
                img_des.rows(), img_des.channels(), pos_dest[0], Feature_dest);
        //是否提取到特征值
        if(nRet != 0) {
            log.error("Failed to extract feature from dest image!");
            ThreadUtils.sleep(CommonConstants.FACE_DETECT_INTERVAL);
            img_des.release();
            img_des = null;
            return res;
        }
        img_des.release();
        img_des = null;
        try{
            //获取服务器特征值
            List<UserFaceFeatureResDTO> userFeatureList = getFeatureList();
            if(userFeatureList != null && userFeatureList.size() > 0){
                for(UserFaceFeatureResDTO uf : userFeatureList){
                    byte[] source = Base64.decodeBase64(uf.getFaceFeature());
                    float fScore = detectFaceFeature.Chs_Fr_Compare(detectFaceFeature.getPtrFeature(), source, Feature_dest);
                    if(fScore >= CommonConstants.FACE_COMPARE_SUCCESS){
                        return uf;
                    }
                }
                ThreadUtils.sleep(CommonConstants.FACE_DETECT_INTERVAL);
            }
            return null;
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

    Integer detectFaceInit(VideoCapture vc){
        int num = CommonConstants.FACE_COMPARE_TIMES;
        while(num > 0){
            Mat image = new Mat();
            vc.read(image);
            Chs_FacePos[] pos_source = new Chs_FacePos[1];
            byte[] source_data= new byte[image.cols()*image.rows()*image.channels()];
            image.get(0, 0,source_data);
            int nSourceFaces = detectFaceDetector.Chs_Fd_DetectFace(detectFaceDetector.getPtrFaceDetectorHandler(),
                    source_data, image.channels(), image.cols(), image.rows(), pos_source,1, 360);
            image.release();
            if(nSourceFaces > 0) {
                return 1;
            }
            ThreadUtils.sleep(CommonConstants.FACE_DETECT_INTERVAL);
            num --;
        }
        return 0;

//        Mat image = new Mat();
//        vc.read(image);
//        Chs_FacePos[] pos_source = new Chs_FacePos[1];
//        byte[] source_data= new byte[image.cols()*image.rows()*image.channels()];
//        image.get(0, 0,source_data);
//        int nSourceFaces = detectFaceDetector.Chs_Fd_DetectFace(detectFaceDetector.getPtrFaceDetectorHandler(),
//                source_data, image.channels(), image.cols(), image.rows(), pos_source,1, 360);
//        image.release();
//        if(nSourceFaces != 1) {
//            log.error("Source image have No face detected!");
//            return 0;
//        }
//        return 1;
    }

    private List<UserFaceFeatureResDTO> getFeatureList(){

        UriComponents uriComponents = UriComponentsBuilder.fromUriString(CommonConstants.USER_FEATURE_URL)
                .build()
                .expand()
                .encode();
        URI uri = uriComponents.toUri();
        RestTemplate restTemplate = SpringUtils.getBean(RestTemplate.class);
        JSONObject res = restTemplate.getForEntity(uri, JSONObject.class).getBody();

        if (!CommonConstants.API_SUCCESS.equals(Objects.requireNonNull(res).getString(CommonConstants.API_MESSAGE))) {
            throw new CommonException(ErrorCode.OPENAPI_ERROR, String.valueOf(res.get(CommonConstants.API_MESSAGE)));
        }
        if (res.getJSONArray(CommonConstants.API_DATA) == null) {
            return null;
        }
        return JSONArray.parseArray(res.getJSONArray(CommonConstants.API_DATA).toJSONString(), UserFaceFeatureResDTO.class);
    }


}
