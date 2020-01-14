package com.mt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @ClassName: MtApplication
 * @Description:
 * @Author: xiedong
 * @Date: 2020/1/13 13:40
 */
@SpringBootApplication(scanBasePackages = {"com.mt.*"})
//@ComponentScan(basePackages = {"com.mt.advice","com.mt.aop","com.mt.controller","com.mt.service"})
@MapperScan("com.mt.mapper")
@Slf4j
public class MtApplication {
    public static void main(String[] args) {
        SpringApplication.run(MtApplication.class);
    }
}
