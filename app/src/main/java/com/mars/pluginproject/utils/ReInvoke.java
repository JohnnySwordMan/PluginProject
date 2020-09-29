package com.mars.pluginproject.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by geyan on 2020/9/29
 */
public class ReInvoke {

    /**
     * 创建对象实例
     *
     * @param className       类名
     * @param parameterTypes  构造函数入参类型
     * @param parameterValues 构造函数入参
     * @return 对象
     */
    public static Object createObject(String className, Class[] parameterTypes, Object[] parameterValues) {
        Object obj = null;
        try {
            Class<?> clazz = Class.forName(className);
            Constructor<?> constructor = clazz.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
            obj = constructor.newInstance(parameterValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static Object createObject(String className) {
        Class[] parameterTypes = new Class[]{};
        Object[] parameterValues = new Object[]{};
        return createObject(className, parameterTypes, parameterValues);
    }

    /**
     * 调用对象方法
     *
     * @param obj             实例
     * @param methodName      方法名称
     * @param parameterTypes  方法入参类型
     * @param parameterValues 方法入参
     * @return 方法返回值
     */
    public static Object invokeInstanceMethod(Object obj, String methodName, Class[] parameterTypes, Object[] parameterValues) {
        Object res = null;
        if (obj == null) {
            return null;
        }
        try {
            Method method = obj.getClass().getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            res = method.invoke(obj, parameterValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 调用静态方法
     *
     * @param className        类名
     * @param staticMethodName 静态方法名
     * @param parameterTypes   入参类型
     * @param parameterValues  入参
     * @return 方法的返回值
     */
    public static Object invokeStaticMethod(String className, String staticMethodName, Class[] parameterTypes, Object[] parameterValues) {
        try {
            Class<?> staticClazz = Class.forName(className);
            Method staticMethod = staticClazz.getDeclaredMethod(staticMethodName, parameterTypes);
            staticMethod.setAccessible(true);
            return staticMethod.invoke(null, parameterValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getFieldObject(String className, Object obj, String fieldName) {
        try {
            Class<?> clazz = Class.forName(className);
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setFieldObject(String className, Object obj, String fieldName, Object fieldValue) {
        try {
            Class<?> clazz = Class.forName(className);
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, fieldValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
