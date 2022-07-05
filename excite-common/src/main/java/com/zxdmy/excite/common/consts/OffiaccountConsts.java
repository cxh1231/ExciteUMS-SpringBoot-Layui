package com.zxdmy.excite.common.consts;

/**
 * 公众号场景用到的常量
 *
 * @author 拾年之璐
 * @since 2022/6/24 10:19
 */
public class OffiaccountConsts {


    /**
     * 消息回复的类型
     */
    public static class ReplyType {

        // 收到消息的默认回复
        public static final Integer DEFAULT_REPLY = 0;

        // 关键词回复
        public static final Integer KEYWORD_REPLY = 1;

        // 点击菜单回复
        public static final Integer MENU_CLICK_REPLY = 2;

        // 关注默认回复
        public static final Integer SUBSCRIBE_REPLY = 3;

        // 登录成功后回复
        public static final Integer SCAN_LOGIN_REPLY = 4;
    }

    public static class ReplyStatus {

        // 启用
        public static final Integer ENABLE = 1;

        // 禁用
        public static final Integer DISABLE = 0;
    }

    /**
     * 消息类型
     */
    public static class ReplyMessageType {
        // 文本消息
        public static final String TEXT = "text";

        // 图片消息
        public static final String IMAGE = "image";

        // 语音消息
        public static final String VOICE = "voice";

        // 视频消息
        public static final String VIDEO = "video";

        // 音乐消息
        public static final String MUSIC = "music";

        // 图文消息
        public static final String NEWS = "news";
    }

    /**
     * 关键词匹配类型
     */
    public static class ReplyMate {
        // 全匹配
        public static final Integer FULL_MATE = 1;

        // 半匹配
        public static final Integer HALF_MATE = 2;
    }

    /**
     * 关注与否
     */
    public static class Subscribe {

        public static final Integer YES = 1;

        public static final Integer NO = 0;
    }
}
