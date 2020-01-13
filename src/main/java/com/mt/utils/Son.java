package com.mt.utils;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Data;

/**
 * @Classname： Son
 * @Description：
 * @Author： xiedong
 * @Date： 2019/11/21 20:52
 * @Version： 1.0
 **/
@XStreamAlias("son")
@Data
public class Son {
    @XStreamAsAttribute
    private String sname;

    @XStreamAsAttribute
    @XStreamAlias("class")
    private String clazz;

    public Son(String sname, String clazz) {
        this.sname = sname;
        this.clazz = clazz;
    }
}
