package com.mt.model.rsp;

import com.mt.model.rsp.body.CustInfoRspBody;
import com.mt.model.rsp.header.RspHeader;
import lombok.Data;

/**
 * @ClassName: CustInfoRsp001
 * @Description:
 * @Author: xiedong
 * @Date: 2020/1/13 14:17
 */
@Data
public class CustInfoRsp001 {
    private RspHeader header;
    private CustInfoRspBody body;
}
