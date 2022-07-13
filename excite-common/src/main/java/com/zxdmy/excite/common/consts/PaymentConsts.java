package com.zxdmy.excite.common.consts;

/**
 * 支付用到的常量
 *
 * @author 拾年之璐
 * @since 2022/4/1 14:15
 */
public class PaymentConsts {

    /**
     * 配置的默认名称
     */
    public static class ConfigName {
        public static final String DEFAULT_SERVICE_ALIPAY = "alipay";

        public static final String DEFAULT_KEY_ALIPAY = "alipay";

        public static final String DEFAULT_SERVICE_WECHATPAY = "wechatPay";

        public static final String DEFAULT_KEY_WECHATPAY = "wechatPay";
    }

    /**
     * 支付渠道
     */
    public static class Channel {
        public static final String ALIPAY = "alipay";

        public static final String WECHAT = "wechat";
    }

    /**
     * 支付场景
     */
    public static class Scene {
        public static final String QRCODE = "qrcode";

        public static final String WAP = "wap";

        public static final String PAGE = "page";

        public static final String WECHAT = "wechat";

        public static final String APP = "app";
    }

    /**
     * 订单前缀类别头
     */
    public static class Order {
        public static final String OUT_TRADE_NO_PREFIX_WECHAT = "WX";

        public static final String OUT_TRADE_NO_PREFIX_ALIPAY = "AL";

        public static final String TRADE_NO_PREFIX_WECHAT = "42";

        public static final String TRADE_NO_PREFIX_ALIPAY = "20";
    }

    /**
     * 订单状态
     */
    public static class Status {
        /**
         * 待支付
         */
        public static final String WAIT = "WAIT";

        /**
         * 支付成功
         */
        public static final String SUCCESS = "SUCCESS";

        /**
         * 交易有退款
         */
        public static final String REFUND = "REFUND";

        /**
         * 交易关闭（已全额退款或超时未支付）
         */
        public static final String CLOSED = "CLOSED";

        /**
         * 支付完成（超过可退款时间）
         */
        public static final String FINISHED = "FINISHED";

        /**
         * 订单错误
         */
        public static final String ERROR = "ERROR";

    }
}
