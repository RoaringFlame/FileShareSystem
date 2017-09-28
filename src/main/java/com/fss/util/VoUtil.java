package com.fss.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.sql.Timestamp;

public class VoUtil {
    static Logger logger = LoggerFactory.getLogger(VoUtil.class);

    public static <T> T copyBasic(T vo,Object entity){
        if(vo==null)return null;
        Class<?> voClass=vo.getClass();
        //获取方法列表
        Method[] voMethdos=voClass.getMethods();
        for(Method m:voMethdos){
            String mName=m.getName();
            //查找set开头的方法
            if(mName.indexOf("set")==0){
                try {
                    //获得参数类型
                    Class<?> paramType = m.getParameterTypes()[0];
                    if (!checkType(paramType)) continue;
                    //推算get方法名
                    String getName = "get" + mName.substring(3);
                    //判断vo中该属性值是否为空
                    Method voGetMethod=voClass.getMethod(getName);
                    Object voValue=voGetMethod.invoke(vo);
                    if(voValue!=null) continue;
                    //通过实体对应方法获取实体中对应属性值
                    Class<?> entityClass = entity.getClass();
                    Method entityGetMethod = entityClass.getMethod(getName);
                    Object value = entityGetMethod.invoke(entity);
                    //调用VO的set方法设置值
                    m.invoke(vo, value);
                } catch (Exception e) {
                    logger.debug("VO:" + voClass.getSimpleName() + "-->Entity:" + entity.getClass().getSimpleName() + "转换错误，对应方法:" + mName, e);
                }
            }
        }
        return vo;
    }
    public static <T> T copyBasic(Class<T> voClass,Object entity){
        try {
            T vo=voClass.newInstance();
            return copyBasic(vo,entity);
        } catch (InstantiationException e) {
            logger.debug("VO:" + voClass.getSimpleName()+"无法实例化,需要无参构造函数",e);
        } catch (IllegalAccessException e) {
            logger.debug("VO:" + voClass.getSimpleName() + "无法实例化,无法访问", e);
        }
        return null;
    }

    private static boolean checkType(Class<?> paramType) {
        if(Integer.class.equals(paramType)||"int".equals(paramType.getName()))return true;
        if(Long.class.equals(paramType)||"long".equals(paramType.getName()))return true;
        if(Double.class.equals(paramType)||"double".equals(paramType.getName()))return true;
        if(Float.class.equals(paramType)||"float".equals(paramType.getName()))return true;
        if(String.class.equals(paramType))return true;
        if(Boolean.class.equals(paramType)||"boolean".equals(paramType.getName()))return true;
        if(Timestamp.class.equals(paramType))return true;
        if(java.util.Date.class.equals(paramType))return true;
        if(java.sql.Date.class.equals(paramType))return true;
        return false;
    }

}
