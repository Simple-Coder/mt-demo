package com.mt.advice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.mt.annotation.AESAndSign;
import com.mt.model.advice.ReqAdviceBean;
import com.mt.utils.AESUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;

/**
 * @ClassName: ReqAdvice
 * @Description: 解密
 * @Author: xiedong
 * @Date: 2020/1/13 14:53
 */
@Slf4j
@RestControllerAdvice
public class ReqAdvice implements RequestBodyAdvice
{
    @Value("${aes-key}")
    private String aesKey;

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        //这里默认为false，需要修改
        return  methodParameter.hasParameterAnnotation(RequestBody.class);
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        //这里方法名顾名思义，就是在请求体读取之前做哪些逻辑---而解密就在这里
        HttpHeaders headers = httpInputMessage.getHeaders();
        if (methodParameter.getMethod().isAnnotationPresent(AESAndSign.class))
        {
            AESAndSign AESAndSign = methodParameter.getMethod().getAnnotation(AESAndSign.class);
            if (AESAndSign.decode())
            {
                return new HttpInputMessage()
                {
                    @Override
                    public InputStream getBody() throws IOException
                    {
                        try
                        {
                            ReqAdviceBean syncReqAdvice = JSON.parseObject(IOUtils.toString(httpInputMessage.getBody()), ReqAdviceBean.class, Feature.OrderedField);
                            syncReqAdvice.setEncryMsg(syncReqAdvice.getBody());
                            syncReqAdvice.setBody(AESUtil.decrypt(syncReqAdvice.getBody(), aesKey));
                            log.info("当前交易:【{}】已解密，报文内容：【{}】",syncReqAdvice.getHeader().getTransId(),JSON.toJSONString(syncReqAdvice));
                            return IOUtils.toInputStream(this.replaceBlank(StringEscapeUtils.unescapeJava(JSON.toJSONString(syncReqAdvice)))
                                    .replace("\"{\"", "{\"").replace("}\"", "}"));
                        } catch (Exception e) {
                            log.info("请求报文解密出现异常", e);
                            return null;
                        }
                    }

                    private String replaceBlank(String str) {
                        String dest = "";
                        if (str!=null) {
                            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                            Matcher m = p.matcher(str);
                            dest = m.replaceAll("");
                        }
                        return dest;
                    }

                    @Override
                    public HttpHeaders getHeaders() {
                        return httpInputMessage.getHeaders();
                    }
                };
            }

        }
        return httpInputMessage;
    }

    @Override
    public Object afterBodyRead(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }

    @Override
    public Object handleEmptyBody(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }
}
