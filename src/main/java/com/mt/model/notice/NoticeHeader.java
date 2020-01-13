package com.mt.model.notice;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @ClassName: Header
 * @Description:
 * @Author: xiedong
 * @Date: 2020/1/13 14:41
 */
@Data
public class NoticeHeader {
    //接口版本号，固定值
    private String version;
    private String respTime;
    private String charset;
    //签名
    private String sign;
    private String respCode;
    private String respMsg;
}
