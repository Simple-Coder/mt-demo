package com.mt.model.req;

import com.mt.model.common.ReqCommon;
import com.mt.model.req.body.CustInfoReqBody;
import com.mt.model.req.header.ReqHeader;
import lombok.Data;

/**
 * @ClassName: CustInfoReq001
 * @Description:
 * @Author: xiedong
 * @Date: 2020/1/13 14:16
 */
@Data
public class CustInfoReq001 extends ReqCommon {
    private ReqHeader header;
    private CustInfoReqBody body;
}
