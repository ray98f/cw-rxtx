package com.rxtx.constant;

/**
 * 常量
 *
 * @author zhangxin
 * @version 1.0
 * @date 2021/8/2 10:31
 */
public interface CommonConstants {
   String API_MESSAGE = "message";
   String API_SUCCESS = "success";
   String API_DATA = "data";
   String API_CONTENT = "content";
    /**
     * 删除
     */
    String IS_DEL = "1";


    /**
     * 正常
     */
    String STATUS_NORMAL = "0";

    /**
     * 禁用
     */
    String STATUS_BAN = "1";

    /**
     * webservice成功标记
     */
    String WSDL_SUCCESS = "0";

    Integer BATCH_EXECUTOR_SIZE = 100;

    String CURRENT_USER = "currentUser";

    /**
     * 禁用
     */
    String STATUS_ban = "9";

    /**
     * 编码
     */
    String UTF8 = "UTF-8";

    /**
     * JSON 资源
     */
    String CONTENT_TYPE = "application/json; charset=utf-8";


    /**
     * 成功标记
     */
    Integer SUCCESS = 0;

    /**
     * 失败标记
     */
    Integer FAIL = 401;

    /**
     * 当前页
     */
    String CURRENT = "current";

    /**
     * size
     */
    String SIZE = "size";


    /**
     * 人脸识别成功标记
     */
    Integer COMPARE_FACE_SUCCESS = 1;

    /**
     * 人脸识别失败标记
     */
    Integer COMPARE_FACE_FAIL = 2;

    String BUCKET_POLICY_DEFAULT ="";
    String BUCKET_POLICY_READ ="read";

    String BUCKET_POLICY_WRITE ="write";

    String BUCKET_POLICY_READ_WRITE ="read-write";

    String SUPPLIER_FLAG = "W";

    String DEFAULT_EIP_ROLE = "8a84ad9161daefdf0161daf074500001";

    String DEFAULT_PWD = "M+JUjNeeSsoLITbNtRqPjA==";
    String DEFAULT_USER_TYPE = "3";
    String SUPP_USER_TYPE = "2";
    String ADMIN_USER_TYPE = "1";
    String TRAIN_SPLIT = "--->";

    String WINE_CODE_35 = "7E01320002010035"; //等待测试
    String WINE_CODE_35_MSG = "等待测试,等待5秒";

    String WINE_CODE_36 = "7E01320002020036"; //测试失败
    String WINE_CODE_36_MSG = "测试失败，请重试";

    String WINE_CODE_1 = "7E01320002030037"; //允许吹气
    String WINE_CODE_1_MSG = "允许吹气";

    String WINE_CODE_2 = "7E01320002040038"; //吹气成功
    String WINE_CODE_2_MSG = "吹气成功";

    String WINE_CODE_3 = "7E013000010031"; //请求吹气,等待测试
    String WINE_CODE_4 = "7E014000010041"; //停止酒测


    String HEX_STRING = "0123456789ABCDEF";
    String NONE = "无";
    String ODD = "奇";
    String EVEN = "偶";
    String FORMAT_HEX="HEX";
    String WINE_COM1 ="COM1";
    String WINE_COM ="COM6";
    String FINGER_COM ="COM3";
    String CARD_COM ="COM5";
    String WINE_MSG_HEAD = "7E01";
    String WINE_MSG_CODE30 = "30";
    String WINE_MSG_CODE32 = "32";
    String WINE_MSG_CODE34 = "34";
    String WINE_MSG_CODE40 = "40";
    String WINE_MSG_CODE42 = "42";

    String CARD_CODE_0 = "031001";//9600波特率 03 10 01 12
    String CARD_CODE_1 = "032000";//未加校验字 寻卡 模式1 00
    String CARD_X20_HEAD = "20"; //寻卡命令
    String CARD_X20_FAILD = "DF"; //寻卡失败命令
    String FACE_DB = "C:/facedb";
    String FACE_IMAGE_SUBFIX = ".jpg";
    float  FACE_COMPARE_SUCCESS = 0.7f;
    Integer FACE_DEF_NUM = 10; //默认采集照片数量
    //Integer FACE_IMG
    Integer FACE_REG_TIME_OUT = 6000; //人脸采集超时时间
    Integer FACE_IMAGE_READ_INTERVAL = 500;//人脸图片读取间隔
    Integer FACE_DETECT_INTERVAL = 600;//人脸检测间隔
    Integer FACE_COMPARE_TIMES = 5; //人脸比对次数
    String USER_FEATURE_URL = "http://nocm.wzmtr.com:2443/zzj2/user/featureList?tenantId=RXTX";
    //String USER_FEATURE_URL = "http://192.168.34.127:9501/zzj/user/featureList?tenantId=RXTX";

   String AUTH_PATH = "C:/";
   String SEPRATOR = (System.getProperty("os.name").toLowerCase().contains("win")) ? "\\" : "/";

}
