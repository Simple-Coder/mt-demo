package com.mt.model.req.header;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @ClassName: ReqHeader
 * @Description:
 * @Author: xiedong
 * @Date: 2020/1/13 14:10
 */
@Data
public class ReqHeader {
    //接口版本号，固定值
    @JSONField(ordinal = 1)
    private String version;
    //请求流水号
    @JSONField(ordinal = 2)
    private String requestNo;
    //合作方请求时间，格式：yyyyMMddHHmmss
    @JSONField(ordinal = 3)
    private String requestTime;
    //编码类型：UTF-8
    @JSONField(ordinal = 4)
    private String charset;
    //签名
    @JSONField(ordinal = 5)
    private String sign;
    //交易编号，区分交易
    @JSONField(ordinal = 6)
    private String transId;
    //渠道编号
    @JSONField(ordinal = 7)
    private String channelId;
}
