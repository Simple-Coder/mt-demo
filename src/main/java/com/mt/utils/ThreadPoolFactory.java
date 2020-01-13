package com.mt.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
//@Component
public class ThreadPoolFactory
{
    private Map<String, ExecutorService> executorServiceMap = new HashMap<String, ExecutorService>();

    private static ThreadPoolFactory instance = null;

    public static ThreadPoolFactory newInstance()
    {

        if (instance == null)
        {
            synchronized (ThreadPoolFactory.class)
            {
                if (instance == null)
                {
                    instance = new ThreadPoolFactory();
                }
            }
        }

        return instance;
    }
    public ExecutorService add(String poolName,String bankCode, String tradeCode)
    {
        return this.add(poolName,bankCode,tradeCode,100);
    }
    /**
     * 添加线程池
     *
     * @param bankCode
     * @param tradeCode
     * @param poolSize
     */
    public ExecutorService add(String poolName,String bankCode, String tradeCode, Integer poolSize)
    {

        ExecutorService executorService = this.getThreadPool(bankCode,tradeCode);
        if(executorService==null)
        {
            executorService=Executors.newFixedThreadPool(poolSize==null?10:poolSize, new NamedThreadFactory(poolName.concat("-").concat(bankCode).concat("-").concat(tradeCode)));
            executorServiceMap.put(bankCode.concat("-").concat(tradeCode), executorService);
            log.info("初始化【{}】交易线程池完成，线程池大小【{}】", tradeCode, poolSize);
        }
        return executorService;
    }

    /**
     * 获取指定交易线程池
     *
     * @param bankCode
     * @param tradeCode
     * @return
     */
    public ExecutorService getThreadPool(String bankCode, String tradeCode)
    {
        return executorServiceMap.get(bankCode.concat("-").concat(tradeCode));
    }


    /**
     * 关闭指定交易线程池
     *
     * @param bankCode
     * @param tradeCode
     */
    public void shutdown(String bankCode,String tradeCode)
    {
        ExecutorService executorService = executorServiceMap.get(bankCode.concat("-").concat(tradeCode));
        if (executorService != null && !executorService.isShutdown())
        {
            executorService.shutdown();
            executorServiceMap.remove(bankCode.concat("-").concat(tradeCode));
            log.info("移除【{}】交易线程池完成", bankCode.concat("-").concat(tradeCode));
        }
    }
}
