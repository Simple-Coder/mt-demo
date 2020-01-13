package com.mt.advice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mt.annotation.AESAndSign;
import com.mt.model.advice.RspAdviceBean;
import com.mt.utils.AESUtil;
import com.mt.utils.ReflexObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @ClassName: RspAdvice
 * @Description:
 * @Author: xiedong
 * @Date: 2020/1/13 15:07
 */
@RestControllerAdvice
@Slf4j
public class RspAdvice implements ResponseBodyAdvice
{
    @Value("${aes-key}")
    private String key;
    @Value("${md5-salt}")
    private String salt;

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        //对方法注解AESAndSign生效
        return methodParameter.hasMethodAnnotation(AESAndSign.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        //body响应之前加密
        log.info("返回明文内容：【{}】", JSON.toJSONString(body, SerializerFeature.DisableCircularReferenceDetect));
        Object plainHeader = ReflexObjectUtil.getValueByKey(body, "header");
        Object plainBody = ReflexObjectUtil.getValueByKey(body, "body");
        RspAdviceBean tempBodyBean = new RspAdviceBean();
        if (methodParameter.getMethod().isAnnotationPresent(AESAndSign.class)){
            AESAndSign AESAndSign = methodParameter.getMethod().getAnnotation(AESAndSign.class);
            String sign=null;
            String encryptBody= null;
            try
            {
                //加密逻辑
                if (AESAndSign.encode())
                {
                    encryptBody = AESUtil.encrypt(JSON.parseObject(JSON.toJSONString(body)).getString("body"), key);
                }
                //加签逻辑
                if (AESAndSign.addSign())
                {
                    String respTime = (String)ReflexObjectUtil.getValueByKey(plainHeader, "respTime");
                    sign=AESUtil.shaHex(respTime.concat(encryptBody).concat(salt).getBytes("utf-8"));
                }
                tempBodyBean.setHeader(ReflexObjectUtil.setValueByKey(plainHeader, "sign", sign));
                tempBodyBean.setBody(encryptBody);

                return tempBodyBean;
            } catch (Exception e) {
                log.info("响应体加密加签异常：【{}】",e);
                return body;
            }
        }
        return body;
    }
}
