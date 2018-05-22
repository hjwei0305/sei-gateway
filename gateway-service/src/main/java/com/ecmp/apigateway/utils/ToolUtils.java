package com.ecmp.apigateway.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author: hejun
 * @date: 2018/4/25
 * @remark: 工具类
 * @update:liusonglin 新增获取routerKey的方法
 */
public class ToolUtils {

    /**
     * 正则表达式：验证汉字
     */
    public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";
    /**
     * 正则表达式：验证数字
     */
    public static final String REGEX_NUMBER = "^[0-9]*$";
    /**
     * 正则表达式：验证身份证
     */
    public static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";
    /**
     * 正则表达式：验证URL
     */
    public static final String REGEX_URL = "http(s)://([\\w-]+\\.)+[\\w-]+(/[\\w- ./%&=]*)";
    /**
     * 正则表达式：验证IP地址
     */
    public static final String REGEX_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]\\d)";

    /**
     * 校验汉字
     *
     * @param chinese
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isChinese(String chinese) {
        return Pattern.matches(REGEX_CHINESE, chinese);
    }

    /**
     * 校验身份证
     *
     * @param idCard
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isIDCard(String idCard) {
        return Pattern.matches(REGEX_ID_CARD, idCard);
    }

    /**
     * 校验URL
     *
     * @param url
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUrl(String url) {
        return Pattern.matches(REGEX_URL, url);
    }

    /**
     * 校验IP地址
     *
     * @param ipAddr
     * @return
     */
    public static boolean isIPAddr(String ipAddr) {
        return Pattern.matches(REGEX_IP_ADDR, ipAddr);
    }

    /**
     * 随机生成六位数验证码
     *
     * @return
     */
    public static int getRandomNum() {
        Random r = new Random();
        return r.nextInt(900000) + 100000; // (Math.random()*(999999-100000)+100000)
    }

    /**
     * 检测字符串是否为空(null,"","null")
     *
     * @param s
     * @return 为空则返回true，不否则返回false
     */
    public static boolean isEmpty(String s) {
        return s == null || "".equals(s) || s.length() == 0;
    }

    /**
     * 检测字符串是否不为空(null,"","null")
     *
     * @param s
     * @return 不为空则返回true，否则返回false
     */
    public static boolean notEmpty(String s) {
        return !isEmpty(s);
    }

    public static boolean isEmpty(Object o) {
        return o == null;
    }

    public static boolean notEmpty(Object o) {
        return !isEmpty(o);
    }

    public static boolean isEmpty(String[] s) {
        return s == null || s.length == 0;
    }

    public static boolean notEmpty(String[] s) {
        return !isEmpty(s);
    }

    public static boolean isEmpty(int i) {
        return i == Integer.MIN_VALUE;
    }

    public static boolean notEmpty(int i) {
        return !isEmpty(i);
    }

    public static boolean isEmpty(Collection c) {
        return c == null || c.size() == 0;
    }

    public static boolean notEmpty(Collection c) {
        return !isEmpty(c);
    }

    public static boolean isEmpty(StringBuffer sb) {
        return sb == null || sb.length() == 0;
    }

    public static boolean notEmpty(StringBuffer sb) {
        return !isEmpty(sb);
    }

    public static boolean isEmpty(Map m) {
        return m == null || m.isEmpty();
    }

    public static boolean notEmpty(Map m) {
        return !isEmpty(m);
    }

    public static boolean isHave(String s, String i) {
        return s.indexOf(i) != -1;
    }

    public static boolean notHave(String s, String i) {
        return !isHave(s, i);
    }

    public static boolean isNum(String n) {
        if (notEmpty(n)) return n.matches(REGEX_NUMBER);
        else return false;
    }

    public static boolean notNum(String n) {
        return !isNum(n);
    }

    /**
     * 字符串转换为字符串数组
     *
     * @param str        字符串
     * @param splitRegex 分隔符
     * @return
     */
    public static String[] str2StrArray(String str, String splitRegex) {
        if (isEmpty(str)) {
            return null;
        }
        return str.split(splitRegex);
    }

    /**
     * 用默认的分隔符(,)将字符串转换为字符串数组
     *
     * @param str 字符串
     * @return
     */
    public static String[] str2StrArray(String str) {
        return str2StrArray(str, ",\\s*");
    }

    /**
     * 按照yyyy-MM-dd HH:mm:ss的格式，转日期格式
     *
     * @param date
     * @return
     */
    public static Timestamp date2Tmp(Date date) {
        return Timestamp.valueOf(date2Str(date));
    }

    /**
     * 按照yyyy-MM-dd HH:mm:ss的格式，日期转字符串
     *
     * @param date
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String date2Str(Date date) {
        return date2Str(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 按照yyyy-MM-dd HH:mm:ss的格式，字符串转日期
     *
     * @param date
     * @return
     */
    public static Date str2Date(String date) {
        if (notEmpty(date)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                return sdf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return new Date();
        } else {
            return null;
        }
    }

    /**
     * 按照参数format的格式，日期转字符串
     *
     * @param date
     * @param format
     * @return
     */
    public static String date2Str(Date date, String format) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        } else {
            return "";
        }
    }

    /**
     * 把时间根据时、分、秒转换为时间段
     *
     * @param StrDate
     */
    public static String getTimes(String StrDate) {
        String resultTimes = "";

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now;

        try {
            now = new Date();
            Date date = df.parse(StrDate);
            long times = now.getTime() - date.getTime();
            long day = times / (24 * 60 * 60 * 1000);
            long hour = (times / (60 * 60 * 1000) - day * 24);
            long min = ((times / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long sec = (times / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

            StringBuffer sb = new StringBuffer();
            // sb.append("发表于：");
            if (hour > 0) {
                sb.append(hour + "小时前");
            } else if (min > 0) {
                sb.append(min + "分钟前");
            } else {
                sb.append(sec + "秒前");
            }

            resultTimes = sb.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return resultTimes;
    }

    /**
     * 路由Key转正常Path
     *
     * @param routeKey
     * @return
     */
    public static String key2Path(String routeKey) {
        String path = routeKey;
        if (ToolUtils.notEmpty(routeKey)) {
            if (!routeKey.startsWith("/")) path = "/" + path;
            if (!routeKey.endsWith("/**") && !routeKey.endsWith("/*")) {
                if (!routeKey.endsWith("/")) {
                    if (routeKey.endsWith("*")) path = path.replace("*", "") + "/**";
                    else path = path + "/**";
                } else path = path + "**";
            } else {
                if (routeKey.endsWith("/*")) path = path + "*";
            }
        }
        return path;
    }

    /**
     * 转换成in参数
     *
     * @param str
     * @return
     */
    public static List<String> id2List(String str) {
        List<String> result = new ArrayList<String>();
        String[] temp = str.split(",");
        for (String s : temp) result.add(s);
        return result;
    }

    /**
     * 根据配置中心的appUrl获取路由前缀 ，example：/basic-service/
     *
     * @param appUrl
     * @return
     */
    public static String getRouteKey(String appUrl) {
        //简单正则匹配ip地址
        return appUrl.replaceAll("http://\\d*\\.\\d*\\.\\d*\\.\\d*:\\d*","");
    }

    public static void main(String[] args) {
        System.out.println(getRandomNum());
    }

}
