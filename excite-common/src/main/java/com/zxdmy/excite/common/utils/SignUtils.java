package com.zxdmy.excite.common.utils;

import cn.hutool.crypto.digest.MD5;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/4/1 20:44
 */
public class SignUtils {

    /**
     * 生成签名
     *
     * @param map    请求参数
     * @param secret 秘钥
     * @return 生成的签名
     */
    public static String sign(TreeMap<String, Object> map, String secret) {
        // 待签名的字串
        return MD5.create().digestHex(sortStr(map, secret));
    }

    /**
     * 验证签名（32位MD5值）
     *
     * @param map    请求参数
     * @param secret 秘钥
     * @param hash   待验证的签名
     * @return 结果：T-签名正确 | F-签名错误
     */
    public static Boolean checkSignMD5(TreeMap<String, Object> map, String secret, String hash) {
        // 验证签名
        return hash.equalsIgnoreCase(MD5.create().digestHex(sortStr(map, secret)));
    }

    private static String sortStr(TreeMap<String, Object> map, String secret) {
        // 待签名的字串
        StringBuilder str = new StringBuilder();
        for (String key : map.keySet()) {
            // 参数的值为空（含""、"  "、null）均不参与签名；
            if (map.get(key) != null && StringUtils.isNotBlank(map.get(key).toString())) {
                str.append(key).append("=").append(map.get(key)).append("&");
            }
        }
        // 拼接秘钥
        str.append(secret);
        // 返回按字典序排列的字符串
        return str.toString();
    }
}