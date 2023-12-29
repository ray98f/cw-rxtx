package com.rxtx.websocket;

import com.chs_vision_faces.sdk.*;

/**
 * description:
 *
 * @author zhangxin
 * @version 1.0
 * @date 2023/11/29 10:51
 */

import com.chs_vision_faces.sdk.Chs_FaceDetector_Java;
import org.opencv.core.Mat;

import static org.opencv.imgcodecs.Imgcodecs.imread;

public class FaceTest {
    static
    {
        System.loadLibrary("opencv_java310");
    }
    public static void main(String[] args){
        Chs_IntRet err_code = new Chs_IntRet();

        //Init face detector,
        Chs_FaceDetector_Java objFaceDetector = new Chs_FaceDetector_Java();
        //The parameter fileDir represents the license file directory(i.e. D:\), and if it is empty, it represents the current directory
        long ptrFaceDetectorHandler = objFaceDetector.Chs_Fd_Create(err_code,"C:/");
        if(ptrFaceDetectorHandler == 0)
        {
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



        //Init face feature
        Chs_FaceFeature_Java objFeature = new Chs_FaceFeature_Java();

        //The parameter fileDir represents the license file directory(i.e. D:\), and if it is empty, it represents the current directory
        long ptrFeature  = objFeature.Chs_Fr_Create(err_code,"C:/");
        if(ptrFeature == 0)
        {
            objFaceDetector.Chs_Fd_Release(ptrFaceDetectorHandler);
            System.out.println("Failed to init face feature!");
            return;
        }
        nRet = objFeature.Chs_Fr_Init(ptrFeature, Chs_Devices.CHS_DEVICE_TYPE.CHS_DEVICE_CPU);
        if(nRet != 0)
        {
            objFeature.Chs_Fr_Release(ptrFeature);
            objFaceDetector.Chs_Fd_Release(ptrFaceDetectorHandler);
            System.out.println("Failed to init face feature!");
            return;
        }

        // Extract from the source image
        Mat img_source = imread("C:/source.jpg");
        if(img_source.empty())
        {
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
        int nFeature_size = objFeature.Chs_Fr_Size(ptrFeature);
        byte[] Feature_Soure = new byte[nFeature_size];
        nRet=objFeature.Chs_Fr_Extract(ptrFeature, source_data, img_source.cols(), img_source.rows(), img_source.channels(), pos_source[0], Feature_Soure);
        if(nRet != 0)
        {
            objFeature.Chs_Fr_Release(ptrFeature);
            objFaceDetector.Chs_Fd_Release(ptrFaceDetectorHandler);
            System.out.println("Failed to extract feature from source image!");
            return;
        }


        // Extract from the dest image
        Mat img_des = imread("C:/dest.jpg");
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

        //Compare the similarity of two pictures
        float fScore = objFeature.Chs_Fr_Compare(ptrFeature, Feature_Soure, Feature_dest);
        System.out.println("ok! score :"+ String.valueOf(fScore));


        //Release
        objFaceDetector.Chs_Fd_Release(ptrFaceDetectorHandler);
        objFeature.Chs_Fr_Release(ptrFeature);
        /*Chs_IntRet err_code = new Chs_IntRet();

        //Init face detector,
        Chs_FaceDetector_Java objFaceDetector = new Chs_FaceDetector_Java();
        //The parameter fileDir represents the license file directory(i.e. D:\), and if it is empty, it represents the current directory
        long ptrFaceDetectorHandler = objFaceDetector.Chs_Fd_Create(err_code,"c:/");
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
        long ptrFeature  = objFeature.Chs_Fr_Create(err_code,"c:/");
        if(ptrFeature == 0)
        {
            objFaceDetector.Chs_Fd_Release(ptrFaceDetectorHandler);
            System.out.println("Failed to init face feature!");
            return;
        }
        nRet = objFeature.Chs_Fr_Init(ptrFeature, Chs_Devices.CHS_DEVICE_TYPE.CHS_DEVICE_CPU);
        if(nRet != 0)
        {
            objFeature.Chs_Fr_Release(ptrFeature);
            objFaceDetector.Chs_Fd_Release(ptrFaceDetectorHandler);
            System.out.println("Failed to init face feature!");
            return;
        }

        // Extract from the source image
        Mat img_source = imread("C:/source.jpg");
        if(img_source.empty())
        {
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
        int nFeature_size = objFeature.Chs_Fr_Size(ptrFeature);
        byte[] Feature_Soure = new byte[nFeature_size];
        nRet=objFeature.Chs_Fr_Extract(ptrFeature, source_data, img_source.cols(), img_source.rows(), img_source.channels(), pos_source[0], Feature_Soure);
        if(nRet != 0)
        {
            objFeature.Chs_Fr_Release(ptrFeature);
            objFaceDetector.Chs_Fd_Release(ptrFaceDetectorHandler);
            System.out.println("Failed to extract feature from source image!");
            return;
        }


        // Extract from the dest image
        Mat img_des = imread("C:/dest.jpg");
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

        //Compare the similarity of two pictures
        float fScore = objFeature.Chs_Fr_Compare(ptrFeature, Feature_Soure, Feature_dest);
        System.out.println("ok! score :"+ String.valueOf(fScore));


        //Release
        objFaceDetector.Chs_Fd_Release(ptrFaceDetectorHandler);
        objFeature.Chs_Fr_Release(ptrFeature);*/

    }


}
