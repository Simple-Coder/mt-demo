package com.mt.utils;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.util.List;

/**
 * @Classname： testXml2Bean
 * @Description：
 * @Author： xiedong
 * @Date： 2019/11/21 20:50
 * @Version： 1.0
 **/
@Data
@XStreamAlias("fathor")
public class Fathor {
    @XStreamAsAttribute
    private String id;
    @XStreamAlias("personAge")
    private Integer age;
    @XStreamImplicit
    private List<Son> sons;

    @Override
    public String toString() {
        return "Fathor{" +
                "id='" + id + '\'' +
                ", age=" + age +
                ", sons=" + sons +
                '}';
    }
}
