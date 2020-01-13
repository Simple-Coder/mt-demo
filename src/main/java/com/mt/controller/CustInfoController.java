package com.mt.controller;

import com.mt.annotation.AESAndSign;
import com.mt.model.req.CustInfoReq001;
import com.mt.service.ICustInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: CustInfoController
 * @Description: 测试业务控制器
 * @Author: xiedong
 * @Date: 2020/1/13 14:03
 */
@RestController
@Slf4j
public class CustInfoController {
    @Autowired
    private ICustInfoService iCustInfoService;

    @RequestMapping("/cust/info")
    @AESAndSign
    public Object dealCustInfo(@RequestBody CustInfoReq001 req){
        return this.iCustInfoService.dealCustInfo(req);
    }
}
