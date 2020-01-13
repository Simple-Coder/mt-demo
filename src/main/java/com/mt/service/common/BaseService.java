package com.mt.service.common;

import com.mt.model.req.header.ReqHeader;
import com.mt.model.rsp.header.RspHeader;
import com.mt.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

/**
 * @ClassName: BaseService
 * @Description:  公共Service
 * @Author: xiedong
 * @Date: 2020/1/13 17:12
 */
@Slf4j
@Service
public class BaseService {

    @Value("${aes-key}")
    private String key;
    @Value("${md5-salt}")
    private String salt;
    @Value("${mt.version}")
    private String versionId;
    @Value("${mt.charset}")
    private String charset;

    protected RspHeader gennerateReqHeader(String respCode,String msg)
    {
        RspHeader rspHeader = new RspHeader();
        rspHeader.setVersion(versionId);
        rspHeader.setCharset(charset);
        rspHeader.setRespCode(respCode);
        rspHeader.setRespTime(DateUtil.getYyyyMMddHHmmssSSS(new Date()));
        rspHeader.setRespMsg(msg);
        return rspHeader;
    }
}
