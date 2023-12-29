package com.rxtx.manage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * description:
 *
 * @author zhangxin
 * @version 1.0
 * @date 2021/11/18 16:53
 */
@Component
@Slf4j
public class ShutdownManager {

    @PreDestroy
    public void destroy()
    {
        shutdownAsyncManager();
    }

    /**
     * 停止异步执行任务
     */
    private void shutdownAsyncManager()
    {
        try
        {
            //关闭后台任务任务线程池
            AsyncManager.getManager().shutdown();
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
    }
}
