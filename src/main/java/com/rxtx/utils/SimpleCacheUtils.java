package com.rxtx.utils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * description:
 *
 * @author zhangxin
 * @version 1.0
 * @date 2023/11/27 14:07
 */
public class SimpleCacheUtils {

    public static final boolean ENABLE = false;
    public static final String INIT = "init";
    public static final String SUBSYSTEM = "subsystem-list";
    public static final String STATION = "station-list";
    public static final String DEVICE = "device-list";
    public static final long CACHE_MINUTES = 24*60*365;

    private static ConcurrentHashMap<String, Map<String, Object>> cacheDataList = new ConcurrentHashMap<>();

    /**
     * 缓存数据
     *
     * @param data
     */
    public static void setCacheData(String cacheKey, Map<String, Object> data) {
        Map<String, Object> cacheData = cacheDataList.get(cacheKey);
        if (cacheData != null) {
            cacheData.remove(cacheKey);
        }
        cacheData = new HashMap<>();
        cacheData.putAll(data);
        cacheData.put("isCache", "true");
        cacheData.put("expireTime", LocalDateTime.now().plusMinutes(CACHE_MINUTES));
        cacheDataList.put(cacheKey, cacheData);
    }

    public static Map<String, Object> getCacheData(String cacheKey) {
        return cacheDataList.get(cacheKey);
    }

    /**
     * 判断缓存中是否有数据
     *
     * @return
     */
    public static boolean hasData(String cacheKey) {
        if (!ENABLE)
            return false;

        Map<String, Object> cacheData = cacheDataList.get(cacheKey);
        if (cacheData == null) {
            return false;
        } else {
            LocalDateTime expire = (LocalDateTime) cacheData.get("expireTime");
            if (expire.isBefore(LocalDateTime.now())) {
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * 清除所有缓存
     */
    public static void clearAll() {
        cacheDataList = new ConcurrentHashMap<>();
    }

    /**
     * 清除缓存数据
     */
    public static void clear(String cacheKey) {
        Map<String, Object> cacheData = cacheDataList.get(cacheKey);
        if (cacheData != null) {
            cacheDataList.remove(cacheKey);
        }
    }

}
