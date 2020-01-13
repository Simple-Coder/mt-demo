package com.mt.service;

import com.mt.model.req.CustInfoReq001;
import com.mt.model.rsp.CustInfoRsp001;

/**
 * @Interface: ICustInfoService
 * @Description:
 * @Author: xiedong
 * @Date: 2020/1/13 13:45
 */
public interface ICustInfoService
{
    CustInfoRsp001 dealCustInfo(CustInfoReq001 req);
}
