package com.mt.service.Impl;

import com.mt.model.req.CustInfoReq001;
import com.mt.model.rsp.CustInfoRsp001;
import com.mt.model.rsp.body.CustInfoRspBody;
import com.mt.model.rsp.header.RspHeader;
import com.mt.service.ICustInfoService;
import com.mt.service.common.BaseService;
import com.mt.utils.ThreadPoolFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

/**
 * @ClassName: CustInfoServiceImpl
 * @Description:
 * @Author: xiedong
 * @Date: 2020/1/13 14:07
 */
@Service
@Slf4j
public class CustInfoServiceImpl extends BaseService implements ICustInfoService {

    @Override
    public CustInfoRsp001 dealCustInfo(CustInfoReq001 req)
    {
        CustInfoRsp001 custInfoRsp001 = new CustInfoRsp001();
        RspHeader rspHeader = super.gennerateReqHeader("000000", "受理成功");
        CustInfoRspBody custInfoRspBody = new CustInfoRspBody();
        custInfoRspBody.setMsg("受理成功");

        ExecutorService executorService = ThreadPoolFactory.newInstance().add(this.getClass().getName(), req.getHeader().getTransId(), req.getHeader().getChannelId());
        executorService.submit(new Runnable()
        {
            @Override
            public void run()
            {
//                tempCustInfoService.executeCustInfo(req);
            }
        });

        custInfoRsp001.setHeader(rspHeader);
        custInfoRsp001.setBody(custInfoRspBody);
        return custInfoRsp001;
    }
}
