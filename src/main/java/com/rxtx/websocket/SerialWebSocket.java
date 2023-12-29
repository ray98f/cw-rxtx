package com.rxtx.websocket;

import com.alibaba.fastjson.JSON;
import com.rxtx.config.ChsFaceDetector;
import com.rxtx.config.ChsFaceFeature;
import com.rxtx.config.Rs232Config;
import com.rxtx.constant.CommonConstants;
import com.rxtx.dto.req.SocketEventReqDTO;
import com.rxtx.dto.res.UserFaceFeatureResDTO;
import com.rxtx.entity.SerialEntity;
import com.rxtx.manage.AsyncManager;
import com.rxtx.service.FaceService;
import com.rxtx.utils.ApplicationContextRegister;
import com.rxtx.utils.FaceUtils;
import com.rxtx.utils.SerialUtil;
import com.rxtx.utils.SpringUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.opencv.videoio.VideoCapture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * description:
 *
 * @author zhangxin
 * @version 1.0
 * @date 2023/8/8 11:33
 */
@Slf4j
@Component
@ServerEndpoint("/websocket/{sid}")
public class SerialWebSocket {

    private VideoCapture vc;
    private FaceService faceService;
    private Rs232Config rs232Config;
    private static final String SID_WINE_TEST0 = "test-0";
    private static final String SID_WINE_TEST1 = "test-1";
    private static final String SID_FINGER = "test-2";
    private static final String SID_CARD = "test-3";
    private static final String SID_FACE = "test-4";

    /**
     * 缓存通信实例
     */
    private static Map<String,SerialWebSocket> webSocketMap = new ConcurrentHashMap<>(16);
//    private static ConcurrentHashMap<String, VideoCapture> faceStreamCache = new ConcurrentHashMap<>();
//    private Map<String, LinkedList<Long>> tokenBucketMap = new ConcurrentHashMap<>();
    private Map<String, Long> lastRequestTimeMap = new ConcurrentHashMap<>();

    /**
     * 会话
     */
    private Session session;

    /**
     * 标识
     */
    private String sid;

    /**
     * 建立连接
     * @param sid
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("sid") String sid,Session session){
        if(rs232Config == null){
            ApplicationContext act = ApplicationContextRegister.getApplicationContext();
            rs232Config = act.getBean(Rs232Config.class);
            faceService = act.getBean(FaceService.class);
        }
        this.session = session;
        this.sid = sid;
        webSocketMap.put(sid,this);
    }

    /**
     * 关闭连接
     * @param sid
     */
    @OnClose
    public void onClose(@PathParam("sid") String sid){
        try {
            SerialWebSocket socket = webSocketMap.remove(sid);
            if (socket != null){
                log.info("Close {} msg:to close",sid);
                switch (sid){
                    case SID_WINE_TEST0:
                    case SID_WINE_TEST1:
                        closePort(CommonConstants.WINE_COM);
                        break;
                    case SID_FACE:
                        if(vc != null){
                            vc.release();
                            vc = null;
                        }
                        break;
                    case SID_CARD:
                        closePort(CommonConstants.CARD_COM);
                        break;
                    default:
                        break;
                }
                socket.session.close();
                socket = null;
            }
        } catch (IOException e) {
            log.error("Close {} exception:",sid,e);

        }
    }

    /**
     * 接收消息
     * @param message
     */
    @OnMessage
    public void onMessage(String message){
        log.info("sid {} msg {}",this.sid,message);
        switch (sid){
            case SID_WINE_TEST0:
            case SID_WINE_TEST1:
                SocketEventReqDTO param =  JSON.parseObject(message, SocketEventReqDTO.class);
                winTestDevice(param);
                break;
            case SID_FACE:
                SocketEventReqDTO paramFace =  JSON.parseObject(message, SocketEventReqDTO.class);
                faceDevice(paramFace);
                break;
            case SID_CARD:
                SocketEventReqDTO paramCard =  JSON.parseObject(message, SocketEventReqDTO.class);
                cardDevice(paramCard);
                break;
            default:
                break;
        }

    }

    /**
     * 发送消息
     * @param message
     * @param sid
     */
    public void sendMessage(String portId,String sid,String message){
        SerialWebSocket socket = webSocketMap.get(sid);
        if (socket != null){
            synchronized(socket.session) {
                try {
                    String socketMsg = "";
                    switch (sid){
                        case SID_WINE_TEST0:
                        case SID_WINE_TEST1: //酒测
                            if(CommonConstants.WINE_COM1.equals(portId) || CommonConstants.WINE_COM.equals(portId)){
                                socketMsg = wineTestMsg( sid, message);
                            }
                            break;
                        case SID_FACE: // 人脸
                            socketMsg = message;

                            break;
                        case SID_CARD: // 刷卡
                            if(CommonConstants.CARD_COM.equals(portId)){
                                socketMsg = cardMsg(sid,message);
                            }
                            break;
                        default:
                            break;
                    }
                    socket.session.getBasicRemote().sendText(socketMsg);
                } catch (IOException e) {
                    //log.error("Send {} message {} exception:",sid,message,e);
                }
            }

        }
    }

    /**
     * 广播消息
     * @param message
     */
    public void broadcast(String portId,String message){
        for (String sid:webSocketMap.keySet()){
            sendMessage(portId,sid,message);
        }
    }

    private void winTestDevice(SocketEventReqDTO param){
        switch (param.getEvent()){
            case "1": //打开 COM6端口
                String serialStr = "{\"portId\": \"COM6\",\"bitRate\": \"9600\",\"dataBit\": \"8\",\"stopBit\": \"1\",\"checkBit\": \"无\",\"format\": \"HEX\"}";
                openPort(serialStr);
                break;
            case "2":
                rs232Config.sendData(CommonConstants.WINE_COM,CommonConstants.FORMAT_HEX,CommonConstants.WINE_CODE_3);
                break;
            case "9":
                rs232Config.sendData(CommonConstants.WINE_COM,CommonConstants.FORMAT_HEX,CommonConstants.WINE_CODE_4);
                break;
            default:
                break;
        }
    }

    private void cardDevice(SocketEventReqDTO param){
        switch (param.getEvent()){
            case "1": //打开 COM5端口
                String serialStr = "{\"portId\": \"COM5\",\"bitRate\": \"9600\",\"dataBit\": \"8\",\"stopBit\": \"1\",\"checkBit\": \"无\",\"format\": \"HEX\"}";
                openPort(serialStr);
                String hexCode0 = SerialUtil.cardCheckSum(CommonConstants.CARD_CODE_0);
                rs232Config.sendData(CommonConstants.CARD_COM,CommonConstants.FORMAT_HEX,hexCode0);

                break;
            case "2": //寻卡
                String hexCode = SerialUtil.cardCheckSum(CommonConstants.CARD_CODE_1);
                rs232Config.sendData(CommonConstants.CARD_COM,CommonConstants.FORMAT_HEX,hexCode);
                break;
            case "9":

                break;
            default:
                break;
        }
    }

    private void faceDevice(SocketEventReqDTO param){
        switch (param.getEvent()){
            case "1"://连接并检测人脸返回
                    vc = new VideoCapture(0);

                    faceMsgEvent1(SID_FACE);
                break;
            case "2": //采集注册人脸
                List<HashMap<String,String>>  featureList = faceService.register(vc,param.getUserNo());
                HashMap<String,Object> map2 = new HashMap<>();
                map2.put("event","2");
                if(featureList == null || featureList.size() == 0){
                    map2.put("message","");
                }else{
                    map2.put("message",featureList);
                }
                sendMessage(null,sid,JSON.toJSONString(map2));
                break;
            case "3": // 人脸比对
                UserFaceFeatureResDTO detectRes = null;
                detectRes = faceService.detect(vc);

                HashMap<String,Object> map3 = new HashMap<>();
                map3.put("event","3");
                map3.put("message",(detectRes == null) ? "" : detectRes);
                sendMessage(null,sid,JSON.toJSONString(map3));
                break;
//            case "4": // 人脸检测
//                Integer res = faceService.initFd(vc);
//                HashMap<String,String> map4 = new HashMap<>();
//                map4.put("event","4");
//                map4.put("message",res+"");
//                sendMessage(null,sid,JSON.toJSONString(map4));
//                break;
            case "9":
                vc.release();
                vc = null;
                //关闭
                break;
            default:
                break;
        }
    }

    //关闭端口
    private void closePort(String portId){
        try{
            rs232Config.closePort(portId);
        }catch (Exception e){
            log.error("exception:",e);
        }
    }

    private void openPort(String serialStr){
        SerialEntity serial = JSON.parseObject(serialStr,SerialEntity.class);
        try{
            rs232Config.openPort(serial);
        }catch (Exception e){
            log.error("exception:",e);
        }

    }

    /**
     * 酒测返回数据
     * */
    private String wineTestMsg(String sid,String message){
        log.info("sid : {} , msg : {}",sid,message);
        message = message.replace(" ","");
        String res = "";
        String head = message.substring(0, 4);
        if(!CommonConstants.WINE_MSG_HEAD.equals(head)){
            return res;
        }
        String tail = message.substring(message.length() - 2);
        String data = message.replace(head,"").replace(tail,"");
        String cmdCode = data.substring(0, 2);
        //7E 01 32 00 02 01 00 35  等待测试
        //7E 01 32 00 02 02 00 36  测试失败
        //7E 01 32 00 02 03 00 37  允许吹气
        //7E 01 32 00 02 04 00 38  吹气成功
        //92 c8  返回浓度值 7E 01 34 00 02 00 4C 82 7E 01 34 00 02 00 00 36

        //解析数据
        if(SerialUtil.checkHexSum(data,tail)){

            switch (cmdCode){
                case CommonConstants.WINE_MSG_CODE32:
                    if(CommonConstants.WINE_CODE_1.equals(message)){
                        res = CommonConstants.WINE_CODE_1_MSG;
                    }else if(CommonConstants.WINE_CODE_2.equals(message)){
                        res = CommonConstants.WINE_CODE_2_MSG;
                    }else if(CommonConstants.WINE_CODE_35.equals(message)){
                        res = CommonConstants.WINE_CODE_35_MSG;
                    }else if(CommonConstants.WINE_CODE_36.equals(message)){
                        res = CommonConstants.WINE_CODE_36_MSG;
                        closePort(CommonConstants.WINE_COM);
                    }
                    break;
                case CommonConstants.WINE_MSG_CODE34:
                    String testCtx = data.substring(data.length()-4);
                    Integer sum = SerialUtil.hexToInt(testCtx);
                    res = "浓度值为:" + sum;
                    break;
                default:
                    break;
            }
        }
        return res;
    }


    /**
     * 刷卡返回数据
     * */
    private String cardMsg(String sid,String message){
        log.info("sid : {} , msg : {}",sid,message);
        message = message.replace(" ","");
        String res = "";
        String dataStr = "";
        String head = message.substring(0, 4);
        log.info("head:"+head);
        //排除酒测数据
        if(CommonConstants.WINE_MSG_HEAD.equals(head)){
            return res;
        }

        //命令字
        String cmdCode = message.substring(2, 4);
        switch (cmdCode){
            case CommonConstants.CARD_X20_HEAD ://寻卡成功返回值
                // 长度字
                // 数据 4、7、10字节卡片序列号 2 字节 ATQA 1字节SAK 获取UID
                String lengthStr = message.substring(0, 2);
                int length = SerialUtil.hexToInt(lengthStr);

                //数据截取计算: 减去 长度字 命令字 占的2个字节 剩余的为数据字节 乘以2 (两位16进制数作为1字节) 加上长度字和命令字的4位
                dataStr = message.substring(4,(length - 2) * 2 + 4);
                log.info("dataStr:"+dataStr);
                //0320005D164100082000
                //0320007D14BF000820
                //005D1641 0008 20
                //ATQA+SAK
                String atqaSakStr = dataStr.substring(dataStr.length()-6);

                //checkSum校验字
                String checkSum = message.substring(length-2, length);

                //物理卡号为 02 + 数据字

                res = "卡号:" +  CommonConstants.CARD_X20_HEAD+dataStr;
                //log.info("res:"+res);
                break;
            case CommonConstants.CARD_X20_FAILD ://寻卡 失败返回值

                break;
            default:
                break;
        }

        log.info("res:"+res);
        return res;
    }

    /**
     * 人脸初始化
     * */
    @SneakyThrows
    private void faceMsgEvent1(String sid){
//        Timer timer = new Timer("传输图像");
//        timer.schedule(new UserFaceInitTask(sid,vc),0);
        AsyncManager.getManager().execute(new UserFaceInitTask(sid,vc));
    }

    class UserFaceInitTask extends TimerTask {

        private VideoCapture videoCapture;
        private String socketId;

        UserFaceInitTask(String sid,VideoCapture videoCapture) {
            this.videoCapture = videoCapture;
            this.socketId = sid;
        }

        @Override
        public void run() {
            HashMap<String,String> map = new HashMap<>();
            String img = "";
            while (videoCapture.isOpened()){
                img = faceService.initFdStream(videoCapture);
                map.put("event","1");
                map.put("message",img);
                sendMessage(null,socketId,JSON.toJSONString(map));

            }
            videoCapture.release();
            videoCapture = null;
            log.info("videoCapture.release!!!!");
        }
    }

    /*private int checkToken(String sid){
        //获取用户id

        //通过用户id生成一个token桶
        LinkedList<Long> tokenBucket = tokenBucketMap.get(sid);
        if (tokenBucket == null) {
            tokenBucket = new LinkedList<>();
            tokenBucketMap.put(sid, tokenBucket);
        }
        long currentTime = System.currentTimeMillis();
        synchronized (tokenBucket) {
            if (tokenBucket.size() < 10 || currentTime - tokenBucket.getFirst() > 60000) {
                tokenBucket.addLast(currentTime);
                if (tokenBucket.size() > 10) {
                    tokenBucket.removeFirst();
                }
                // 处理请求
                return "success";
            } else {
                // 重复请求
                return "error";
            }
        }
    }*/

    private int checkRequest(String sid){
        //从map中查找上次请求的时间戳
        Long lastRequestTime = lastRequestTimeMap.get(sid);
        //如果为空或者上次请求的时间戳与当前时间做差大于5s，则视为新请求，否则重复请求。
        if (lastRequestTime == null || System.currentTimeMillis() - lastRequestTime > 5000) {
            lastRequestTimeMap.put(sid, System.currentTimeMillis());
            // 处理请求
            return 1;
        } else {
            // 重复请求
            return 0;
        }
    }

}
