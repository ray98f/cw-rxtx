package com.rxtx.config;

import com.chs_vision_faces.sdk.Chs_Devices;
import com.chs_vision_faces.sdk.Chs_Fd_Param;
import com.chs_vision_faces.sdk.Chs_IntRet;
import com.rxtx.config.filter.CorsFilter;
import org.opencv.core.Core;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 配置类
 *
 * @author frp
 */
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    public static final String REGEX = ",";
    public final static String AUTH_PATH = "C:/";


    @Autowired
    ConfigResource configResource;

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/META-INF/resources/", "classpath:/resources/",
            "classpath:/static/", "classpath:/public/"};

    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistration() {
        FilterRegistrationBean<CorsFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new CorsFilter());
        registration.setName("CorsFilter");
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }

//    @Bean(name = "initFaceDetector")
//    public ChsFaceDetector initFaceDetector(){
//        Chs_IntRet err_code = new Chs_IntRet();
//        ChsFaceDetector objFaceDetector = new ChsFaceDetector();
//        long ptrFaceDetectorHandler = objFaceDetector.Chs_Fd_Create(err_code,AUTH_PATH);
//        if(ptrFaceDetectorHandler == 0) {
//            throw new RuntimeException("人脸算法未授权");
//        }
//        objFaceDetector.setPtrFaceDetectorHandler(ptrFaceDetectorHandler);
//        Chs_Fd_Param objParam = new Chs_Fd_Param(50,30,30,30,false);
//        if(objFaceDetector.Chs_Fd_Init(ptrFaceDetectorHandler, objParam, Chs_Devices.CHS_DEVICE_TYPE.CHS_DEVICE_CPU) != 0){
//            throw new RuntimeException("人脸算法 初始化失败");
//        }
//        return objFaceDetector;
//    }

    @Bean(name = "detectFaceDetector")
    public ChsFaceDetector objFaceDetector(){
        Chs_IntRet err_code = new Chs_IntRet();
        ChsFaceDetector objFaceDetector = new ChsFaceDetector();
        long ptrFaceDetectorHandler = objFaceDetector.Chs_Fd_Create(err_code,AUTH_PATH);
        if(ptrFaceDetectorHandler == 0) {
            throw new RuntimeException("人脸算法未授权");
        }
        objFaceDetector.setPtrFaceDetectorHandler(ptrFaceDetectorHandler);
        Chs_Fd_Param objParam = new Chs_Fd_Param(50,30,30,30,false);
        if(objFaceDetector.Chs_Fd_Init(ptrFaceDetectorHandler, objParam, Chs_Devices.CHS_DEVICE_TYPE.CHS_DEVICE_CPU) != 0){
            throw new RuntimeException("人脸算法 初始化失败");
        }
        return objFaceDetector;
    }

//    @Bean(name = "registerFaceDetector")
//    public ChsFaceDetector registerFaceDetector(){
//        Chs_IntRet err_code = new Chs_IntRet();
//        ChsFaceDetector objFaceDetector = new ChsFaceDetector();
//        long ptrFaceDetectorHandler = objFaceDetector.Chs_Fd_Create(err_code,AUTH_PATH);
//        if(ptrFaceDetectorHandler == 0) {
//            throw new RuntimeException("人脸算法未授权");
//        }
//        objFaceDetector.setPtrFaceDetectorHandler(ptrFaceDetectorHandler);
//        Chs_Fd_Param objParam = new Chs_Fd_Param(50,30,30,30,false);
//        if(objFaceDetector.Chs_Fd_Init(ptrFaceDetectorHandler, objParam, Chs_Devices.CHS_DEVICE_TYPE.CHS_DEVICE_CPU) != 0){
//            throw new RuntimeException("人脸算法 初始化失败");
//        }
//        return objFaceDetector;
//    }

    @Bean(name = "detectFaceFeature")
    public ChsFaceFeature detectFaceFeature(){
        Chs_IntRet err_code = new Chs_IntRet();
        ChsFaceFeature objFeature = new ChsFaceFeature();
        long ptrFeature  = objFeature.Chs_Fr_Create(err_code,AUTH_PATH);
        if(ptrFeature == 0) {
            throw new RuntimeException("人脸特征算法未授权");
        }
        objFeature.setPtrFeature(ptrFeature);
        if(objFeature.Chs_Fr_Init(ptrFeature, Chs_Devices.CHS_DEVICE_TYPE.CHS_DEVICE_CPU) != 0){
            throw new RuntimeException("人脸特征算法初始化失败");
        }
        return objFeature;
    }

//    @Bean(name = "registerFaceFeature")
//    public ChsFaceFeature registerFaceFeature(){
//        Chs_IntRet err_code = new Chs_IntRet();
//        ChsFaceFeature objFeature = new ChsFaceFeature();
//        long ptrFeature  = objFeature.Chs_Fr_Create(err_code,AUTH_PATH);
//        if(ptrFeature == 0) {
//            throw new RuntimeException("人脸特征算法未授权");
//        }
//        objFeature.setPtrFeature(ptrFeature);
//        if(objFeature.Chs_Fr_Init(ptrFeature, Chs_Devices.CHS_DEVICE_TYPE.CHS_DEVICE_CPU) != 0){
//            throw new RuntimeException("人脸特征算法初始化失败");
//        }
//        return objFeature;
//    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE", "PATCH")
                .allowedHeaders("Origin", "X-Requested-With", "Content-Type", "Accept", "x-auth-token")
                .allowCredentials(false).maxAge(3600);
    }


    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/static/**")
                .addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("classpath:/templates/**");
        configResource.getResources().forEach(r -> {
            String[] paths = r.getPathPatterns().split(REGEX);
            String[] resources = r.getResourceLocations().split(REGEX);
            registry.addResourceHandler(paths).addResourceLocations(resources);
        });
    }
}
