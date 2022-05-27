package com.zxdmy.excite.common.consts;

/**
 * <p>
 * 描述
 * </p>
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
        public static final String ORDER_CODE_WECHAT = "WX";

        public static final String ORDER_CODE_ALIPAY = "AL";
    }

    /**
     * 支付结果
     */
    public static class Status {
        public static final String WAIT = "WAIT";

        public static final String SUCCESS = "SUCCESS";

        public static final String CLOSED = "CLOSED";

        public static final String FINISHED = "FINISHED";
    }
}
