package com.zxdmy.excite.payment.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.TreeMap;

/**
 * 回调信息实体
 *
 * @author 拾年之璐
 * @since 2022/5/28 19:12
 */
@Getter
@Setter
@Accessors(chain = true)
public class PaymentNotifyModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 应用ID
     */
    private String appid;

    private String userId;

    /**
     * 商品标题
     */
    private String title;

    /**
     * 商品价格
     */
    private String amount;

    /**
     * 交易单号
     */
    private String tradeNo;

    /**
     * 商户单号
     */
    private String outTradeNo;

    /**
     * 订单状态
     */
    private String status;

    /**
     * 付款时间
     */
    private String paidTime;

    /**
     * 附加信息
     */
    private String attach;

    /**
     * 时间戳
     */
    private String time;

    /**
     * 随机值
     */
    private String nonce;

    /**
     * 签名
     */
    private String hash;

    public static final String APPID = "appid";

    public static final String TITLE = "title";

    public static final String AMOUNT = "amount";

    public static final String TRADE_NO = "trade_no";

    public static final String OUT_TRADE_NO = "out_trade_no";

    public static final String STATUS = "status";

    public static final String PAID_TIME = "paid_time";

    public static final String ATTACH = "attach";

    public static final String TIME = "time";

    public static final String NONCE = "nonce";

    public static final String HASH = "hash";

    /**
     * 生成字典序Map
     *
     * @return 字典序map
     */
    public TreeMap<String, Object> getTreeMap() {
        TreeMap<String, Object> treeMap = new TreeMap<>();
        treeMap.put(APPID, this.getAppid());
        treeMap.put(TITLE, this.getTitle());
        treeMap.put(AMOUNT, this.getAmount());
        treeMap.put(TRADE_NO, this.getTradeNo());
        treeMap.put(OUT_TRADE_NO, this.getOutTradeNo());
        treeMap.put(STATUS, this.getStatus());
        treeMap.put(PAID_TIME, this.getPaidTime());
        treeMap.put(ATTACH, this.getAttach());
        treeMap.put(TIME, this.getTime());
        treeMap.put(NONCE, this.getNonce());
        return treeMap;
    }
}
