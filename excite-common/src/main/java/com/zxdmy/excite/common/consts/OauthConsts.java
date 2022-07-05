package com.zxdmy.excite.common.consts;

/**
 * 授权登录的常量
 *
 * @author 拾年之璐
 * @since 2022/6/30 11:40
 */
public class OauthConsts {

    /**
     * 登录方式
     */
    public static class LoginType {

        /**
         * 公众号登录（需要关注公众号的形式）
         */
        public static final String MP = "mp";

        /**
         * 公众号登录（关注公众号回复关键字登录）
         */
        public static final String MP1 = "mp1";

        /**
         * 公众号登录（不关注公众号登录）
         */
        public static final String MP2 = "mp2";

        /**
         * 普通微信登录（直接登录的形式）
         */
        public static final String WECHAT = "wechat";

        /**
         * QQ登录
         */
        public static final String QQ = "qq";

        /**
         * 微博登录
         */
        public static final String WEIBO = "weibo";
    }

    /**
     * 登录的用户的一些基本信息预设
     */
    public static class User {
        // 用户缓存的Redis的前缀（根据场景值缓存）
        public static final String REDIS_KEY_PREFIX = "ums:mp:user:scene:";

        // 默认的过期时间（秒）
        public static final Long REDIS_DEFAULT_EXPIRE = 300L;

        // 默认的附加文本长度（字符）
        public static final int REDIS_TEXT_MAX_LENGTH = 64;
    }
}
