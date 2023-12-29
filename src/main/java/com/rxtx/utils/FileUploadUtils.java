package com.rxtx.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

/**
 * 文件上传工具类
 *
 * @author zhangxin
 * @version 1.0
 * @date 2021/7/5 9:24
 */
@Slf4j
public class FileUploadUtils {
    /**
     * 默认大小 10M
     */
    public static final long DEFAULT_MAX_SIZE = 10 * 1024 * 1024;

    /**
     * 默认的文件名最大长度 100
     */
    public static final int DEFAULT_FILE_NAME_LENGTH = 100;

    /**
     * 编码文件名
     */
    public static String extractFilename(MultipartFile file) {
        String fileName;
        String extension = getExtension(file);
        fileName = DateUtils.datePath() + "/" + UUID.randomUUID().toString() + "." + extension;
        return fileName;
    }

    public static String extractFilenameWithoutDate(MultipartFile file) {
        return file.getOriginalFilename();
    }

    /**
     * 获取文件名的后缀
     *
     * @param file 表单文件
     * @return 后缀名
     */
    public static String getExtension(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isEmpty(extension))
        {
            extension = MimeTypeUtils.getExtension(Objects.requireNonNull(file.getContentType()));
        }
        return extension;
    }





}