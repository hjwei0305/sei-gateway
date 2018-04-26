package com.ecmp.apigateway.utils;

import java.util.Random;

/**
 * @author:wangdayin
 * @date:2018/4/25
 * @remark: 随机数工具类
 */
public class RandomUtil {
    /**
     * 随机生成唯一code
     *
     * @return
     */
    public static String getUniqueCode() {

        StringBuilder stringBuilder = new StringBuilder();

        long time = System.currentTimeMillis();
        stringBuilder.append(time);

        stringBuilder.append(get3RandomNumber());
        return stringBuilder.toString();
    }

    /**
     * 获取三位随机整数
     *
     * @return
     */
    public static String get3RandomNumber() {
        StringBuilder stringBuilder = new StringBuilder();
        int bound = 1000;
        int randStrSize = 3;

        Random random = new Random();
        int randomInt = random.nextInt(bound);
        String randomStr = String.valueOf(randomInt);
        int count0 = randStrSize - randomStr.length();
        for (int i = 0; i < count0; i++) {
            stringBuilder.append(0);
        }
        stringBuilder.append(randomStr);
        return stringBuilder.toString();
    }

}
