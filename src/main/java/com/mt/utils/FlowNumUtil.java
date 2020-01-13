package com.mt.utils;

import java.util.Date;

/**
 * @author xiedong
 * 流水号生成工具类
 * @date 2019/7/25 19:42
 */
public class FlowNumUtil {
    public static String getFlowNum(){
        return DateUtil.getYyyyMMddHHmmssSSS(new Date()).concat(Long.toString(DateUtil.random(100,999)));
    }
}
