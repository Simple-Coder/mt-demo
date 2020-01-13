package com.mt.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @Classname： ReflexObjectUtil
 * @Description：反射工具类
 * @Author： xiedong
 * @Date： 2019/11/23 0:55
 * @Version： 1.0
 **/
public class ReflexObjectUtil {
    public static Map<String, Object> getKeyAndValue(Object obj) {
        Map<String, Object> map = new HashMap<String, Object>();
        Class userCla = (Class) obj.getClass();
        Field[] fs = userCla.getDeclaredFields();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            f.setAccessible(true);
            Object val = new Object();
            try {
                val = f.get(obj);
                map.put(f.getName(), val);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public static Object getValueByKey(Object obj, String key) {
        Class userCla = (Class) obj.getClass();
//        Field[] fs = userCla.getDeclaredFields();
        Field[] fs=getAllFields(userCla);
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            f.setAccessible(true);
            try {

                if (f.getName().endsWith(key)) {
                    return f.get(obj);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static Field[] getAllFields(Class clz){
        List<Field> fieldList = new ArrayList<>();
        while (clz != null){
            fieldList.addAll(new ArrayList<>(Arrays.asList(clz.getDeclaredFields())));
            clz = clz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }


    public static <T> T setValueByKey(T obj,String key,Object value) throws Exception {
        Class<?> reClass = obj.getClass();
        Field keyField = reClass.getDeclaredField(key);
        keyField.setAccessible(true);
        keyField.set(obj,value);
        return obj;
    }
    public static void invokeValueByKey(Object obj,String methodName,Object value) throws Exception {
        Class<?> reClass = obj.getClass();
        Method declaredMethod = reClass.getDeclaredMethod(methodName, value.getClass());
        declaredMethod.invoke(obj,value);
    }

    public static void main(String[] args) throws Exception {
        Person person = new Person();
        person.setAge(1);
        person.setName("张三");

        System.out.println(person);
        System.out.println("*****");
        Person newPerson = setValueByKey(person, "age", 23);
        System.out.println(person);
        System.out.println(newPerson);

        System.out.println("*****");
        invokeValueByKey(person, "setAge", 234);
        System.out.println(person);

    }
    static class Person{
        String name;
        Integer age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}
