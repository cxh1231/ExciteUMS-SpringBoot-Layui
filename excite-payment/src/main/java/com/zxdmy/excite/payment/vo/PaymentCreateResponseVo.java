package com.zxdmy.excite.payment.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.TreeMap;

/**
 * 创建订单返回数据
 *
 * @author 拾年之璐
 * @since 2022/4/2 10:45
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentCreateResponseVo extends BasePaymentVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 返回码
     */
    @JsonProperty(value = "sub_code")
    private Integer subCode;

    /**
     * 返回信息
     */
    @JsonProperty(value = "sub_msg")
    private String subMsg;

    /**
     * 商户单号（本平台）
     */
    @JsonProperty(value = "out_trade_no")
    private String outTradeNo;

    /**
     * 订单标题
     */
    private String title;

    /**
     * 订单金额
     */
    private String amount;

    /**
     * 微信、支付宝的当面付、Navicat场景下，返回二维码
     */
    private String qrcode;

    /**
     * 支付宝电脑网站、手机网站场景下，返回page字段
     */
    private String page;

    /**
     * 微信H5支付场景下，返回付款链接
     */
    private String url;

    /**
     * 微信预支付交易会话标识（APP支付、JSAPI支付 会返回）
     */
    @JsonProperty(value = "prepay_id")
    private String prepayId;

    public static final String SUB_CODE = "sub_code";

    public static final String SUB_MSG = "sub_msg";
    public static final String OUT_TRADE_NO = "out_trade_no";

    public static final String TITLE = "title";

    public static final String AMOUNT = "amount";

    public static final String QRCODE = "qrcode";

    public static final String PAGE = "page";

    public static final String URL = "url";

    public static final String PREPAY_ID = "prepay_id";

    /**
     * 生成字典序Map
     *
     * @return 字典序map
     */
    @JsonIgnore
    public TreeMap<String, Object> getTreeMap() {
        TreeMap<String, Object> treeMap = new TreeMap<>();
        treeMap.put(SUB_CODE, this.getSubCode());
        treeMap.put(SUB_MSG, this.getSubMsg());
        treeMap.put(OUT_TRADE_NO, this.getOutTradeNo());
        treeMap.put(TITLE, this.getTitle());
        treeMap.put(AMOUNT, this.getAmount());
        treeMap.put(QRCODE, this.getQrcode());
        treeMap.put(PAGE, this.getPage());
        treeMap.put(URL, this.getUrl());
        treeMap.put(PREPAY_ID, this.getPrepayId());

        treeMap.put(APPID, this.getAppid());
        treeMap.put(TIME, this.getTime());
        treeMap.put(NONCE, this.getNonce());
        return treeMap;
    }
}
