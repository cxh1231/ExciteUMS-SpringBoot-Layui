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
        // 关注默认回复
        public static final Integer SUBSCRIBE_REPLY = 1;

        // 收到消息的默认回复
        public static final Integer MESSAGE_REPLY = 2;

        // 关键词回复
        public static final Integer KEYWORD_REPLY = 3;

        // 登录成功后回复
        public static final Integer LOGIN_REPLY = 4;
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
}
