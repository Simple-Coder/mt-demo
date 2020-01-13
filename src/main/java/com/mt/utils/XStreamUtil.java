package com.mt.utils;

import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;

/**
 * @Classname： XStreamUtil
 * @Description：xml与bean相互转换工具类
 * @Author： xiedong
 * @Date： 2019/11/21 20:11
 * @Version： 1.0
 **/
@Slf4j
public class XStreamUtil {
    /**
     * bean转xml
     * @param obj   bean对象
     * @return
     */
    public static String bean2Xml(Object obj) {
        if (obj == null) {
            log.info("【{}】为null，默认返回空", obj);
            return "";
        }
        String top = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n";
        XStream stream = XStreamFactory.getInstance().getStream(obj.getClass());
        return top.concat(stream.toXML(obj));
    }

    /**
     * xml转bean方法
     * @param xmlStr        xml报文
     * @param reClass       bean
     * @param <T>
     * @return
     */
    public static <T> T xml2Bean(String xmlStr,Class reClass){
        try {
            XStream stream = XStreamFactory.getInstance().getStream(reClass);
            return (T) stream.fromXML(xmlStr);
        } catch (Exception e) {
            log.info("【{}】转换【{}】异常",xmlStr,reClass, e);
           return null;
        }
    }
}
