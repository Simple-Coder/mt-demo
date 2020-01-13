package com.mt.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.mt.annotation.AESAndSign;
import com.mt.annotation.CheckNull;
import com.mt.model.notice.NoticeBean;
import com.mt.model.notice.NoticeBody;
import com.mt.model.notice.NoticeHeader;
import com.mt.utils.AESUtil;
import com.mt.utils.DateUtil;
import com.mt.utils.ReflexObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @ClassName: AESAndSignAspect
 * @Description: 切面
 * @Author: xiedong
 * @Date: 2020/1/13 14:20
 */
@Aspect
@Component
@Slf4j
public class AESAndSignAspect {

    @Value("${aes-key}")
    private String key;
    @Value("${md5-salt}")
    private String salt;
    @Value("${mt.version}")
    private String versionId;
    @Value("${mt.globalCharsetName}")
    private String globalCharsetName;



    //切点
    @Pointcut("@annotation(com.mt.annotation.AESAndSign)")
    public void operateMethod(){}

    //环绕通知
    @Around("operateMethod()")
    public Object methodAround(ProceedingJoinPoint joinPoint) throws Throwable{
        //1.获取代理方法注解（此时已经解密完成）
        AESAndSign aesAndSign =this.getAesAndSignAspect(joinPoint);
        Object result=null;
        Object reqBean = joinPoint.getArgs()[0];
        String reqJson = JSON.toJSONString(joinPoint.getArgs()[0]);
        JSONObject header = JSON.parseObject(JSON.parseObject(reqJson).getString("header"), Feature.OrderedField);
        String sign = header.getString("sign");
        String transId = header.getString("transId");
        String requestTime = header.getString("requestTime");

        String encryMsg = (String) ReflexObjectUtil.getValueByKey(reqBean, "encryMsg");
        Object body = ReflexObjectUtil.getValueByKey(reqBean, "body");
        //2.验签（未通过则重新组装报文）
        if (aesAndSign.checkSign())
        {
            if (AESUtil.vailidate(requestTime.concat(encryMsg).concat(salt).getBytes("utf-8"), sign)){
                String fieldName = this.checkField(body);
                if (fieldName == null) {
                    log.info("当前交易：【{}】验签通过,字段非空校验通过",transId);
                    result = joinPoint.proceed();
                } else {
                    result = this.buildMsg(fieldName + "字段非空校验未通过");
                    log.info("当前交易：【{}】验签通过,字段非空校验未通过",transId);
                }
            }
            else
            {
                log.info("当前交易：【{}】验签未通过",transId);
                result=this.buildMsg("验签未通过");
            }
        }
        else
        {
            log.info("当前交易：【{}】无需验签",transId);
            result=joinPoint.proceed();
        }
        return result;
    }

    /**
     * 非空字段校验
     * @param obj
     * @return
     */
    private String checkField(Object obj)
    {
        try {
            Class<?> clazz = obj.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                CheckNull inspector = field.getAnnotation(CheckNull.class);
                if (inspector != null) {
                    Object val = field.get(obj);
                    //是否必输检查
                    if (!inspector.check() && (val == null || String.valueOf(val).length() == 0)) {
                        return field.getName();
                    }
                }
            }
            return null;
        } catch (Exception e) {
            log.error("字段检查异常：【{}】", e);
            throw new RuntimeException("字段检查发生其他异常", e);
        }
    }

    /**
     * 构造验签或非空字段校验未通过报文
     * @param msg
     * @return
     */
    private Object buildMsg(String msg) {
        NoticeBean rsp = new NoticeBean();
        NoticeHeader noticeHeader = new NoticeHeader();
        NoticeBody noticeBody = new NoticeBody();

        noticeHeader.setVersion(versionId);
        noticeHeader.setCharset(globalCharsetName);
        noticeHeader.setRespMsg(msg);
        noticeHeader.setRespCode("EEEEEE");
        noticeHeader.setRespTime(DateUtil.getYyyyMMddHHmmss(new Date()));

        noticeBody.setMsg(msg);

        rsp.setHeader(noticeHeader);
        rsp.setBody(noticeBody);
        return rsp;
    }
    //获取AesAndSign注解
    private AESAndSign getAesAndSignAspect(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(AESAndSign.class);
    }
}
