package com.mt.model.advice;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @ClassName: RspAdviceBean
 * @Description:
 * @Author: xiedong
 * @Date: 2020/1/13 15:10
 */
@Data
public class RspAdviceBean {
    @JSONField(ordinal = 1)
    private Object header;
    @JSONField(ordinal = 2)
    private Object body;
}
