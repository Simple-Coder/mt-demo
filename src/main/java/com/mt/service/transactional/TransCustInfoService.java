package com.mt.service.transactional;

import com.mt.mapper.CustInfoMapper;
import com.mt.model.CustInfo;
import com.mt.model.req.CustInfoReq001;
import com.mt.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * @ClassName: TransCustInfoService
 * @Description:
 * @Author: xiedong
 * @Date: 2020/1/14 21:53
 */
@Slf4j
@Service
@Transactional
public class TransCustInfoService {
    @Autowired(required = false)
    private CustInfoMapper custInfoMapper;
    public void executeCustInfoTask(CustInfoReq001 req) {
        try {
            List<CustInfo> custInfos = this.custInfoMapper.selectAll();
            CustInfo custInfo = new CustInfo();
            custInfo.setId(CommonUtil.getUUID());
            custInfo.setAge(req.getBody().getAge());
            custInfo.setName(req.getBody().getName());
            custInfo.setSex(req.getBody().getSex());
            custInfo.setAddr(req.getBody().getAddr());
            log.info("开始入库");
            int insert = this.custInfoMapper.insert(custInfo);
            int a = 1 / 0;
        } catch (Exception e) {
            log.info("入库异常", e);
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new RuntimeException("入库异常");
        }
    }
//public void executeCustInfoTask(CustInfoReq001 req){
//    List<CustInfo> custInfos = this.custInfoMapper.selectAll();
//    CustInfo custInfo = new CustInfo();
//    custInfo.setId(CommonUtil.getUUID());
//    custInfo.setAge(req.getBody().getAge());
//    custInfo.setName(req.getBody().getName());
//    custInfo.setSex(req.getBody().getSex());
//    custInfo.setAddr(req.getBody().getAddr());
//    int insert = this.custInfoMapper.insert(custInfo);
//    int a =1/0;
//    log.info("入库结束");
//}
}
