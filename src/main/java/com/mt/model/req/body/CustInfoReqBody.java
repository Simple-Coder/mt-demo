package com.mt.model.req.body;

import com.mt.annotation.CheckNull;
import lombok.Data;

/**
 * @ClassName: CustInfoBean
 * @Description:
 * @Author: xiedong
 * @Date: 2020/1/13 14:13
 */
@Data
public class CustInfoReqBody {
    private String name;
    @CheckNull
    private String sex;
    private String age;
    private String addr;
}
