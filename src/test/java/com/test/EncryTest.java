package com.test;

import com.alibaba.fastjson.JSON;
import com.mt.utils.AESUtil;

/**
 * @ClassName: EncryTest
 * @Description:
 * @Author: xiedong
 * @Date: 2020/1/13 15:13
 */
public class EncryTest
{

    public static void main(String[] args) throws Exception {
         String key="key123456789";
         String md5Salt="abcdefg";
        String sourceMsg="{\n" +
                "\t\"header\":\n" +
                "\t{\n" +
                "\t\t\"version\":\"1.0\",\n" +
                "\t\t\"requestNo\":\"20200113164900999\",\n" +
                "\t\t\"requestTime\":\"20200113164900\",\n" +
                "\t\t\"charset\":\"utf-8\",\n" +
                "\t\t\"sign\":\"\",\n" +
                "\t\t\"transId\":\"custInfo001\",\n" +
                "\t\t\"channelId\":\"01\"\n" +
                "\t\t},\n" +
                "\t\"body\":\n" +
                "\t{\n" +
                "\t\t\"name\":\"张三\",\n" +
                "\t\t\"sex\":\"男\",\n" +
                "\t\t\"age\":\"18\",\n" +
                "\t\t\"addr\":\"北京市小红门\"\n" +
                "\t}\n" +
                "}";
        String encryBody= AESUtil.encrypt(JSON.parseObject(sourceMsg).getString("body"),key);
        System.out.println("密文body:"+encryBody);
        System.out.println("签名："+AESUtil.shaHex("20200113164900".concat(encryBody).concat(md5Salt).getBytes()));
    }
}
