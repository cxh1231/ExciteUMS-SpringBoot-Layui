package com.zxdmy.excite.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 生成订单号：20221231200845000
 *
 * @author 拾年之璐
 * @since 2022/5/27 14:47
 */
public class OrderUtils {

    /**
     * 随机
     */
    private static final int[] r = new int[]{7, 9, 6, 2, 8, 1, 3, 0, 5, 4};

    /**
     * 用户id和随机数总长度
     */
    private static final int maxLength = 11;

    /**
     * 根据id进行加密+加随机数组成固定长度编码
     *
     * @param userId 用户ID
     * @return 字符串
     */
    private static String toCode(Integer userId) {
        String idStr = userId.toString();
        StringBuilder idsbs = new StringBuilder();
        for (int i = idStr.length() - 1; i >= 0; i--) {
            idsbs.append(r[idStr.charAt(i) - '0']);
        }
        return idsbs.append(getRandom(maxLength - idStr.length())).toString();
    }

    /**
     * 生成17位时间戳字符串
     *
     * @return 17位时间戳字符串
     */
    private static String getDateTime() {
        DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return sdf.format(new Date());
    }

    /**
     * 生成固定长度随机码
     *
     * @param n 长度
     */
    private static long getRandom(long n) {
        long min = 1, max = 9;
        for (int i = 1; i < n; i++) {
            min *= 10;
            max *= 10;
        }
        return (((long) (new Random().nextDouble() * (max - min)))) + min;
    }

    /**
     * 生成订单单号编码(调用方法)
     *
     * @return 订单号
     */
    public static synchronized String createOrderCode() {
        return getDateTime() + getRandom(maxLength);
    }

}
