package com.rxtx.utils;

import com.chs_vision_faces.sdk.*;
import org.opencv.core.Mat;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import static org.opencv.imgcodecs.Imgcodecs.imread;

/**
 * description:
 *
 * @author zhangxin
 * @version 1.0
 * @date 2023/11/29 15:17
 */
public class FaceUtils {

    public void  getFaceFeature(){

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
    }

    public static BufferedImage mat2BI(Mat mat) {
        int dataSize = mat.cols() * mat.rows() * (int) mat.elemSize();
        byte[] data = new byte[dataSize];
        mat.get(0, 0, data);
        int type = mat.channels() == 1 ? BufferedImage.TYPE_BYTE_GRAY :
                BufferedImage.TYPE_3BYTE_BGR;

        if (type == BufferedImage.TYPE_3BYTE_BGR) {
            for (int i = 0; i < dataSize; i += 3) {
                byte blue = data[i + 0];
                data[i + 0] = data[i + 2];
                data[i + 2] = blue;
            }
        }
        if(mat.cols() > 0){
            BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
            image.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);

            return image;
        }
        return null;
    }

    public static String BufferedImageToBase64(BufferedImage bufferedImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();//io流
        try {
            ImageIO.write(bufferedImage, "jpg", baos);//写入流中
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();//转换成字节
        BASE64Encoder encoder = new BASE64Encoder();
        String jpg_base64 = encoder.encodeBuffer(bytes).trim();//转换成base64串
        jpg_base64 = jpg_base64.replaceAll("\n", "").replaceAll("\r", "");//删除 \r\n
        //System.out.println("值为：" + "data:image/jpg;base64," + jpg_base64);
        return "data:image/jpg;base64," + jpg_base64;
    }
}
