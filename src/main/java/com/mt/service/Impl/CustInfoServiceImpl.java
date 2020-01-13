package com.mt.service.Impl;

import com.mt.model.req.CustInfoReq001;
import com.mt.model.rsp.CustInfoRsp001;
import com.mt.model.rsp.header.RspHeader;
import com.mt.service.ICustInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @ClassName: CustInfoServiceImpl
 * @Description:
 * @Author: xiedong
 * @Date: 2020/1/13 14:07
 */
@Service
@Slf4j
public class CustInfoServiceImpl implements ICustInfoService {
    @Value("${mt.version}")
    private String versionId;
    @Override
    public CustInfoRsp001 dealCustInfo(CustInfoReq001 req)
    {
        log.info("收到报文了~");
        CustInfoRsp001 custInfoRsp001 = new CustInfoRsp001();
        RspHeader rspHeader = new RspHeader();
        rspHeader.setVersion(versionId);
//        rspHeader.setCharset();
        custInfoRsp001.setHeader(rspHeader);
        return custInfoRsp001;
    }
}
