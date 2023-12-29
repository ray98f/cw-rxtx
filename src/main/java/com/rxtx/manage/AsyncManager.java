package com.rxtx.manage;


import com.rxtx.utils.SpringUtils;
import com.rxtx.utils.ThreadUtils;

import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 异步任务管理器
 *
 * @author zhangxin
 * @version 1.0
 * @date 2021/11/18 16:44
 */
public class AsyncManager {

    /**
     * 操作延迟10毫秒
     */
    private final int OPERATE_DELAY_TIME = 10;

    /**
     * 异步操作任务调度线程池
     */
    private ScheduledExecutorService executor = (ScheduledExecutorService) SpringUtils.getBean("myScheduledExecutor");

    /**
     * 单例模式
     */
    private AsyncManager(){}

    private static AsyncManager manager = new AsyncManager();

    public static AsyncManager getManager()
    {
        return manager;
    }

    /**
     * 执行任务
     *
     * @param task 任务
     */
    public void execute(TimerTask task)
    {
        executor.schedule(task, OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
    }

    /**
     * 停止任务线程池
     */
    public void shutdown()
    {
        ThreadUtils.shutdownAndAwaitTermination(executor);
    }
}
