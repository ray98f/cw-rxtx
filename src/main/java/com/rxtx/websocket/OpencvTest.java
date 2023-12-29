package com.rxtx.websocket;

import cn.hutool.core.io.FileUtil;
import com.chs_vision_faces.sdk.*;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgcodecs.Imgcodecs.imread;


/**
 * description:
 *
 * @author zhangxin
 * @version 1.0
 * @date 2023/11/29 16:36
 */
public class OpencvTest {
    static{
        System.out.println(System.getProperty("java.library.path"));
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
    public final static String seprator = (System.getProperty("os.name").toLowerCase().contains("win")) ? "\\" : "/";
    public static void main(String[] args) {
//        System.out.println("Welcome to OpenCV " + Core.VERSION);
//        Mat m = new Mat(5, 10, CvType.CV_8UC1, new Scalar(0));
//        System.out.println("OpenCV Mat: " + m);
//        Mat mr1 = m.row(1);
//        mr1.setTo(new Scalar(1));
//        Mat mc5 = m.col(5);
//        mc5.setTo(new Scalar(5));
//        System.out.println("OpenCV Mat data:\n" + m.dump());

        VideoCapture vc = new VideoCapture(0);
        if(!vc.isOpened()){
            System.out.println("在电脑上未发现已连接的摄像头！");
        }

        //采集 注册
        captureImage(vc,"00001518",null);

        //检测人脸
        faceCompareForN(vc);

    }


    private static void captureImage(VideoCapture vc,String userCode,Integer n) {

        String userDir = "C:/opencvDemo/"+userCode;

        if (!FileUtil.isDirectory(userDir)) {
            // 创建文件夹
            FileUtil.mkdir(userDir);
        }

        if(n == null){
            n = 5;
        }
        List<String> images = new ArrayList<>();
        for(int i = 0;i < n;i++){
            Mat image = new Mat();
            vc.read(image);
            String fileName = userDir +   seprator + i + ".jpg";
            Imgcodecs.imwrite(fileName, image);
            images.add(fileName);
        }
        faceRegis(images);
    }

    private static void faceInit(){

    }

    private static void faceCompareForN(VideoCapture vc){
        Chs_IntRet err_code = new Chs_IntRet();

        //Init face detector,
        Chs_FaceDetector_Java objFaceDetector = new Chs_FaceDetector_Java();
        //The parameter fileDir represents the license file directory(i.e. D:\), and if it is empty, it represents the current directory
        long ptrFaceDetectorHandler = objFaceDetector.Chs_Fd_Create(err_code,"C:/");
        if(ptrFaceDetectorHandler == 0) {
            System.out.println("Failed to init face detector!");
            return;
        }

        Chs_Fd_Param objParam = new Chs_Fd_Param(10,90,90,90,false);
        int nRet = objFaceDetector.Chs_Fd_Init(ptrFaceDetectorHandler, objParam, Chs_Devices.CHS_DEVICE_TYPE.CHS_DEVICE_CPU);
        if(nRet != 0) {
            objFaceDetector.Chs_Fd_Release(ptrFaceDetectorHandler);
            System.out.println("Failed to init face detector!");
            return;
        }

        /*
1.调用Chs_Fr_Create创建特征检测对象。
2．调用Chs_Fr_Init初始化特征检测对象。
3.调用Chs_Fr_OpenOneToManyDb打开一个人脸特征索引数据库。
4.调用Chs_Fr_Extract提取待查找的人脸特征。
5.调用Chs_Fr_CompareOnetoMany将待查找的人脸特征与特征索引库进行1：N比对，提取比对结果。
6.调用Chs_Fr_CloseOneToManyDb关闭人脸特征索引数据库(可以不用，Chs_Fr_Release也会关闭人脸特征索引数据库)。
7．调用Chs_Fr_Release释放特征检测对象。
        * */

        //Init face feature
        Chs_FaceFeature_Java objFeature = new Chs_FaceFeature_Java();

        //1
        long ptrFeature  = objFeature.Chs_Fr_Create(err_code,"C:/");
        if(ptrFeature == 0)
        {
            objFaceDetector.Chs_Fd_Release(ptrFaceDetectorHandler);
            System.out.println("Failed to init face feature!");
            return;
        }

        //2
        nRet = objFeature.Chs_Fr_Init(ptrFeature, Chs_Devices.CHS_DEVICE_TYPE.CHS_DEVICE_CPU);
        if(nRet != 0)
        {
            objFeature.Chs_Fr_Release(ptrFeature);
            objFaceDetector.Chs_Fd_Release(ptrFaceDetectorHandler);
            System.out.println("Failed to init face feature!");
            return;
        }

        //3 Chs_Fr_OpenOneToManyDb  C:/facedb
        nRet = objFeature.Chs_Fr_OpenOneToManyDb(ptrFeature,"C:/facedb".toCharArray());
        if(nRet != 0)
        {
            objFeature.Chs_Fr_Release(ptrFeature);
            objFaceDetector.Chs_Fd_Release(ptrFaceDetectorHandler);
            System.out.println("Failed to init face feature!");
            return;
        }

        int nFeature_size = objFeature.Chs_Fr_Size(ptrFeature);

        //待检测
        Mat img_des = new Mat();
        vc.read(img_des);
        //4 Chs_Fr_Extract
        if(img_des.empty())
        {
            objFeature.Chs_Fr_Release(ptrFeature);
            objFaceDetector.Chs_Fd_Release(ptrFaceDetectorHandler);
            System.out.println("Failed to read source image!");
            return;
        }
        Chs_FacePos pos_dest[] = new Chs_FacePos[1];
        byte[] dest_data = new byte[img_des.cols()*img_des.rows()*img_des.channels()];
        img_des.get(0,0,dest_data);
        int nDesFaces = objFaceDetector.Chs_Fd_DetectFace(ptrFaceDetectorHandler, dest_data, img_des.channels(), img_des.cols(), img_des.rows(), pos_dest, 1, 360);
        if(nDesFaces <= 0)
        {
            objFeature.Chs_Fr_Release(ptrFeature);
            objFaceDetector.Chs_Fd_Release(ptrFaceDetectorHandler);
            System.out.println("dest image have No face detected!");
            return;
        }
        byte[] Feature_dest=new byte[nFeature_size];
        nRet = objFeature.Chs_Fr_Extract(ptrFeature, dest_data, img_des.cols(), img_des.rows(), img_des.channels(), pos_dest[0], Feature_dest);
        if(nRet != 0)
        {
            objFeature.Chs_Fr_Release(ptrFeature);
            objFaceDetector.Chs_Fd_Release(ptrFaceDetectorHandler);
            System.out.println("Failed to extract feature from dest image!");
            return;
        }

        //5 Chs_Fr_CompareOnetoMany
        int nMaxResult = 5;
        Chs_ResultInfo pFakeIDArray[] = new Chs_ResultInfo[nMaxResult];
        nRet = objFeature.Chs_Fr_CompareOnetoMany(ptrFeature,Feature_dest,80,nMaxResult,pFakeIDArray);
        if(nRet <= 0)
        {
            objFeature.Chs_Fr_Release(ptrFeature);
            objFaceDetector.Chs_Fd_Release(ptrFaceDetectorHandler);
            System.out.println("Failed to extract feature from dest image!");
            return;
        }

        //6 Chs_Fr_CloseOneToManyDb
        objFeature.Chs_Fr_CloseOneToManyDb(ptrFaceDetectorHandler);

        //7
        objFaceDetector.Chs_Fd_Release(ptrFaceDetectorHandler);
        objFeature.Chs_Fr_Release(ptrFeature);



    }

    private static void faceRegis(List<String> images)  {

        Chs_IntRet err_code = new Chs_IntRet();

        //Init face detector,
        Chs_FaceDetector_Java objFaceDetector = new Chs_FaceDetector_Java();
        //The parameter fileDir represents the license file directory(i.e. D:\), and if it is empty, it represents the current directory
        long ptrFaceDetectorHandler = objFaceDetector.Chs_Fd_Create(err_code,"C:/");
        if(ptrFaceDetectorHandler == 0) {
            System.out.println("Failed to init face detector!");
            return;
        }

        Chs_Fd_Param objParam = new Chs_Fd_Param(10,90,90,90,false);
        int nRet = objFaceDetector.Chs_Fd_Init(ptrFaceDetectorHandler, objParam, Chs_Devices.CHS_DEVICE_TYPE.CHS_DEVICE_CPU);
        if(nRet != 0) {
            objFaceDetector.Chs_Fd_Release(ptrFaceDetectorHandler);
            System.out.println("Failed to init face detector!");
            return;
        }

        /*
1.调用Chs_Fr_Create创建特征检测对象。
2．调用Chs_Fr_Init初始化特征检测对象。
3.调用Chs_Fr_OpenOneToManyDb打开一个人脸特征索引数据库。

4.调用Chs_Fr_Extract提取模板的人脸特征。
5.调用Chs_Fr_GetNextFeatureID获取特征索引的下一个ID号。
6.调用Chs_Fr_AddFeature建立人脸特征索引。

7.如果有多个人脸模板，请重复4、5、6逐一建立人脸特征索引。
8.调用Chs_Fr_CloseOneToManyDb关闭人脸特征索引数据库(可以不用，Chs_Fr_Release也会关闭人脸特征索引数据库)。
9.调用Chs_Fr_Release释放特征检测对象。
        * */
        //Init face feature
        Chs_FaceFeature_Java objFeature = new Chs_FaceFeature_Java();

        //1
        long ptrFeature  = objFeature.Chs_Fr_Create(err_code,"C:/");
        if(ptrFeature == 0)
        {
            objFaceDetector.Chs_Fd_Release(ptrFaceDetectorHandler);
            System.out.println("Failed to init face feature!");
            return;
        }

        //2
        nRet = objFeature.Chs_Fr_Init(ptrFeature, Chs_Devices.CHS_DEVICE_TYPE.CHS_DEVICE_CPU);
        if(nRet != 0)
        {
            objFeature.Chs_Fr_Release(ptrFeature);
            objFaceDetector.Chs_Fd_Release(ptrFaceDetectorHandler);
            System.out.println("Failed to init face feature!");
            return;
        }

        //3 Chs_Fr_OpenOneToManyDb  C:/facedb
        nRet = objFeature.Chs_Fr_OpenOneToManyDb(ptrFeature,"C:/facedb".toCharArray());
        if(nRet != 0)
        {
            objFeature.Chs_Fr_Release(ptrFeature);
            objFaceDetector.Chs_Fd_Release(ptrFaceDetectorHandler);
            System.out.println("Failed to init face feature!");
            return;
        }

        //4 5 6 7
        for(String image: images){
            faceExtra(ptrFeature,ptrFaceDetectorHandler,objFaceDetector,objFeature,image);
        }

        //8 Chs_Fr_CloseOneToManyDb
        objFeature.Chs_Fr_CloseOneToManyDb(ptrFaceDetectorHandler);

        //9
        objFaceDetector.Chs_Fd_Release(ptrFaceDetectorHandler);
        objFeature.Chs_Fr_Release(ptrFeature);

    }

    private static void faceExtra(long ptrFeature ,long ptrFaceDetectorHandler,Chs_FaceDetector_Java objFaceDetector,Chs_FaceFeature_Java objFeature,String image){
        Mat img_source = imread(image);
        if(img_source.empty()) {
            objFeature.Chs_Fr_Release(ptrFeature);
            objFaceDetector.Chs_Fd_Release(ptrFaceDetectorHandler);
            System.out.println("Failed to read source image!");
            return;
        }

        Chs_FacePos pos_source[] = new Chs_FacePos[1];
        byte[] source_data= new byte[img_source.cols()*img_source.rows()*img_source.channels()];
        img_source.get(0, 0,source_data);
        int nSourceFaces = objFaceDetector.Chs_Fd_DetectFace(ptrFaceDetectorHandler, source_data, img_source.channels(), img_source.cols(), img_source.rows(), pos_source,1, 360);
        if(nSourceFaces <= 0)
        {
            objFeature.Chs_Fr_Release(ptrFeature);
            objFaceDetector.Chs_Fd_Release(ptrFaceDetectorHandler);
            System.out.println("Source image have No face detected!");
            return;
        }

        //4 Chs_Fr_Extract
        int nFeature_size = objFeature.Chs_Fr_Size(ptrFeature);
        byte[] Feature_Soure = new byte[nFeature_size];
        int nRet=objFeature.Chs_Fr_Extract(ptrFeature, source_data, img_source.cols(), img_source.rows(), img_source.channels(), pos_source[0], Feature_Soure);
        if(nRet != 0)
        {
            //objFeature.Chs_Fr_Release(ptrFeature);
            //objFaceDetector.Chs_Fd_Release(ptrFaceDetectorHandler);
            System.out.println("Failed to extract feature from source image!");
            return;
        }


        //5 Chs_Fr_GetNextFeatureID
        int nextId = objFeature.Chs_Fr_GetNextFeatureID(ptrFeature);
        if(nextId <= 0){
            //objFeature.Chs_Fr_Release(ptrFeature);
            //objFaceDetector.Chs_Fd_Release(ptrFaceDetectorHandler);
            System.out.println("Failed to extract feature from source image!");
            return;
        }

        //6 Chs_Fr_AddFeature
        nRet = objFeature.Chs_Fr_AddFeature(ptrFeature,nextId,Feature_Soure,"userCode".toCharArray());
        if(nRet != 0)
        {
            //objFeature.Chs_Fr_Release(ptrFeature);
            //objFaceDetector.Chs_Fd_Release(ptrFaceDetectorHandler);
            System.out.println("Failed to extract feature from source image!");
            return;
        }
    }

    /*private  static void faceDetect(VideoCapture vc){
        Chs_IntRet err_code = new Chs_IntRet();

        //Init face detector,
        Chs_FaceDetector_Java objFaceDetector = new Chs_FaceDetector_Java();

        long ptrFaceDetectorHandler = objFaceDetector.Chs_Fd_Create(err_code,"C:/");

        if(ptrFaceDetectorHandler == 0)
        {
            System.out.println("Failed to init face detector!");
            return;
        }

        Chs_Fd_Param objParam = new Chs_Fd_Param(10,90,90,90,false);
        int nRet = objFaceDetector.Chs_Fd_Init(ptrFaceDetectorHandler, objParam, Chs_Devices.CHS_DEVICE_TYPE.CHS_DEVICE_CPU);
        if(nRet != 0)
        {
            objFaceDetector.Chs_Fd_Release(ptrFaceDetectorHandler);
            System.out.println("Failed to init face detector!");
            return;
        }

        //Init face feature
        Chs_FaceFeature_Java objFeature = new Chs_FaceFeature_Java();

        //The parameter fileDir represents the license file directory(i.e. D:\), and if it is empty, it represents the current directory
        long ptrFeature  = objFeature.Chs_Fr_Create(err_code,"C:/");
        if(ptrFeature == 0) {
            objFaceDetector.Chs_Fd_Release(ptrFaceDetectorHandler);
            System.out.println("Failed to init face feature!");
            return;
        }
        nRet = objFeature.Chs_Fr_Init(ptrFeature, Chs_Devices.CHS_DEVICE_TYPE.CHS_DEVICE_CPU);
        if(nRet != 0) {
            objFeature.Chs_Fr_Release(ptrFeature);
            objFaceDetector.Chs_Fd_Release(ptrFaceDetectorHandler);
            System.out.println("Failed to init face feature!");
            return;
        }


    }*/

}
