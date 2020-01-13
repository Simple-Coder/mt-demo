package com.mt.utils;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class CommonUtil
{
    public static String getClassesPath()
    {
        String classesPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        return classesPath;
    }

    public static String toUpperCaseFirstOne(String s)
    {
        // 进行字母的ascii编码前移，效率要高于截取字符串进行转换的操作
        char[] cs = s.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }

    public static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes)
    {

        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass())
        {
            try
            {
                //superClass.getMethod(methodName, parameterTypes);
                return superClass.getDeclaredMethod(methodName, parameterTypes);
            }
            catch (NoSuchMethodException e)
            {
                //Method 不在当前类定义, 继续向上转型
            }
            //..
        }

        return null;
    }

    /**
     * 获取本机IP
     *
     * @return
     */
    public static String getLocalIp()
    {
        try
        {
            return InetAddress.getLocalHost().getHostAddress();
        }
        catch (UnknownHostException e)
        {
        }
        return null;
    }

    /**
     * 生成UUID
     */
    public static String getUUID()
    {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * @Description: 字符串转码
     * @Param: 要转码的字符串
     * @return: 转码后的字符串
     * @Author: 韩伟成
     * @date: 2019/8/21 0021
     */
    public static String transCode(String value, String sourceCharset, String targetCharset)
    {
        try
        {
            if (value == null)
            {
                return null;
            }
            else
            {
                value = new String(value.getBytes(sourceCharset), targetCharset);
                return value;
            }
        }
        catch (Exception e)
        {
            return value;
        }
    }

    /**
     * @Description: 解析CICS返回报文
     * @Param:
     * @return: map
     * @Author: 韩伟成
     * @date: 2019/8/9 0009
     */
    public static Map<String, String> analysisRespXml(String responseXml) {
        char[] xmlArr = responseXml.toCharArray();
        StringBuffer value = new StringBuffer();
        Map<String, String> map = new HashMap<>();
        int j;
        for (int i = 0; i < xmlArr.length; i++) {
            j = i;
            if (j + 1 < xmlArr.length && '<' == xmlArr[j] && '/' != xmlArr[j + 1]) {
                while (j + 1 < xmlArr.length && '<' != xmlArr[j + 1]) {
                    value.append(xmlArr[j + 1]);
                    j = j + 1;
                }
                String[] strs = value.toString().split(">");
                map.put(strs[0], strs[1]);
                value.setLength(0);
            }
            i = j;
        }
        return map;
    }

    /**
     * 生成16位不重复的随机数，含数字+大小写
     *
     * @return
     */
    public static String getGUID() {
        StringBuilder uid = new StringBuilder();
        //产生16位的强随机数
        Random rd = new SecureRandom();
        for (int i = 0; i < 16; i++) {
            //产生0-2的3位随机数
            int type = rd.nextInt(3);
            switch (type) {
                case 0:
                    //0-9的随机数
                    uid.append(rd.nextInt(10));
                    break;
                case 1:
                    //ASCII在65-90之间为大写,获取大写随机
                    uid.append((char) (rd.nextInt(25) + 65));
                    break;
                case 2:
                    //ASCII在97-122之间为小写，获取小写随机
                    uid.append((char) (rd.nextInt(25) + 97));
                    break;
                default:
                    break;
            }
        }
        return uid.toString();
    }
}
