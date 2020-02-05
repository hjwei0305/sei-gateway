package com.changhong.sei.apigateway.utils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author:wangdayin
 * @date:2018/4/25
 * @remark: 实体-反射复制工具类
 */
public class EntityUtils {
    private static ConcurrentHashMap<String, ConcurrentHashMap<String, Method>[]> classGetAndSetMethodMap = new ConcurrentHashMap<>();

    /**
     * 调用Class的get方法取出数据，并调用接收数据对象的set方法赋值
     *
     * @param object       被赋值对象
     * @param resultObject 赋值对象
     * @param <T>
     */
    public static <T> void resolveAllFieldsSet(final T object, T resultObject) {
        if (null == object || null == resultObject) {
            return;
        }
        Class cls = resultObject.getClass();
        ConcurrentHashMap<String, Method>[] concurrentHashMapArray = searchGetAndSetMethods(cls);
        for (Map.Entry<String, Method> entry : concurrentHashMapArray[0].entrySet()) {
            String fieldName = entry.getKey();
            Method getMethod = entry.getValue();
            Method setMethod = concurrentHashMapArray[1].get(fieldName);
            if (null == setMethod || null == getMethod) {
                continue;
            }

            try {
                Object fieldVal = getMethod.invoke(resultObject);
                //赋值对象如果存在值
                Object invoke = getMethod.invoke(object);
                if (invoke == null || String.valueOf(invoke).equals("0")) {
                    setMethod.invoke(object, fieldVal);
                }
            } catch (Exception ignored) {
                throw new RuntimeException();
            }
        }
    }

    /**
     * 遍历class的get&set方便，并存入数组中缓存
     *
     * @param cls
     * @return
     */
    private static ConcurrentHashMap<String, Method>[] searchGetAndSetMethods(Class<?> cls) {
        String className = cls.getName();
        ConcurrentHashMap<String, Method>[] getSetMethodArray;
        getSetMethodArray = classGetAndSetMethodMap.get(className);
        if (null == getSetMethodArray) {
            ConcurrentHashMap<String, Method> getMethodsMap = new ConcurrentHashMap<>();
            ConcurrentHashMap<String, Method> setMethodsMap = new ConcurrentHashMap<>();

//            Method[] methods = cls.getDeclaredMethods();//得到本类的方法，不会得到父类的方法
            Method[] methods = cls.getMethods();//得到本类的方法，得到父类的方法

            for (Method method : methods) {
                try {
                    method.setAccessible(true);
                    String methodName = method.getName();

                    if (isGetMethod(methodName)) {
                        String fieldName = getMethodField(methodName);
                        getMethodsMap.put(fieldName, method);
                        continue;
                    }

                    if (isSetMethod(methodName)) {
                        String fieldName = getMethodField(methodName);
                        setMethodsMap.put(fieldName, method);
                    }

                } catch (Exception ignored) {
                    throw new RuntimeException();
                }
            }
            getSetMethodArray = new ConcurrentHashMap[2];
            getSetMethodArray[0] = getMethodsMap;
            getSetMethodArray[1] = setMethodsMap;
            classGetAndSetMethodMap.put(className, getSetMethodArray);
        }
        return getSetMethodArray;
    }

    private static String getMethodField(String getMethodName) {
        return getMethodName.substring(3, getMethodName.length());
    }

    private static boolean isGetMethod(String methodName) {
        return methodName.indexOf("get") == 0;
    }

    private static boolean isSetMethod(String methodName) {
        return methodName.indexOf("set") == 0;
    }
}
