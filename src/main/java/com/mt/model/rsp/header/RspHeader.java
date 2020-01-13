package com.mt.model.rsp.header;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @ClassName: RspHeader
 * @Description:
 * @Author: xiedong
 * @Date: 2020/1/13 14:11
 */
@Data
public class RspHeader {
    //接口版本号，固定值
    @JSONField(ordinal = 1)
    private String version;
    //返回时间，格式：yyyyMMddHHmmss
    @JSONField(ordinal = 2)
    private String respTime;
    //编码类型：UTF-8
    @JSONField(ordinal = 3)
    private String charset;
    //签名
    @JSONField(ordinal = 4)
    private String sign;
    //返回码
    @JSONField(ordinal = 5)
    private String respCode;
    //返回提示信息
    @JSONField(ordinal = 6)
    private String respMsg;
}
