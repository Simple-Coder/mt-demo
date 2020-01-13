package com.mt.utils;

import java.util.ArrayList;

/**
 * @Classname： Test
 * @Description：
 * @Author： xiedong
 * @Date： 2019/11/21 20:28
 * @Version： 1.0
 **/
public class Test {
    public static void main(String[] args) {
        ArrayList<Son> sons = new ArrayList<>();
        sons.add(new Son("zhangsan1", "class1"));
        sons.add(new Son("zhangsan2", "class2"));
        Fathor fathor = new Fathor();
        fathor.setId("1001");
        fathor.setAge(50);
        fathor.setSons(sons);

        /*******bean转xml*******/
        String s = XStreamUtil.bean2Xml(fathor);
        System.out.println(s);


        /*******xml转bean*******/
        Fathor fathor1 = XStreamUtil.xml2Bean(s,Fathor.class);
        System.out.println(fathor1);
    }
}
