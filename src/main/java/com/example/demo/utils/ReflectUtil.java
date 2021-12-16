package com.example.demo.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author jiejT
 * @ClassName: ReflectUtil
 * @description: TODO
 * @date 2021/12/11
 */
public abstract  class ReflectUtil {

    public static Object getFiledValue(Object object, String fieldName) {
        Object objValue = null;
        Field objField = null;
        Class clazz = object.getClass();

        while(clazz != Object.class) {
            try {
                objField = clazz.getDeclaredField(fieldName);
                objField.setAccessible(true);
                objValue = objField.get(object);
                return objValue;
            } catch (Exception var6) {
                clazz = clazz.getSuperclass();
            }
        }

        return objValue;
    }

    public static Object getFiledValue4I(Class iClass, String fieldName) {
        Object objValue = null;

        try {
            Field objField = iClass.getField(fieldName);
            objField.setAccessible(true);
            objValue = objField.get(iClass);
            return objValue;
        } catch (Exception var4) {
            throw new RuntimeException(var4);
        }
    }

    public static void setFiledValue(Object object, String fieldName, Object value) {
        Class clazz = object.getClass();
        Field objField = null;

        while(null != clazz) {
            try {
                objField = clazz.getDeclaredField(fieldName);
                objField.setAccessible(true);
                objField.set(object, value);
                break;
            } catch (Exception var6) {
                clazz = clazz.getSuperclass();
            }
        }

        if (null == objField) {
        }

    }

    public static Class getGenericType(Class clazz, String methodName) {
        Method[] methods = clazz.getDeclaredMethods();
        Method[] var3 = methods;
        int var4 = methods.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Method method = var3[var5];
            if (methodName.equals(method.getName())) {
                return method.getReturnType();
            }
        }

        return null;
    }
}
