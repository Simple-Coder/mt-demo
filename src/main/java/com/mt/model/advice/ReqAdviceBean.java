package com.mt.model.advice;

import com.mt.model.common.ReqCommon;
import com.mt.model.req.header.ReqHeader;
import lombok.Data;

/**
 * @ClassName: ReqAdviceBean
 * @Description:
 * @Author: xiedong
 * @Date: 2020/1/13 15:06
 */
@Data
public class ReqAdviceBean extends ReqCommon {
    private ReqHeader header;
    private String body;
}
