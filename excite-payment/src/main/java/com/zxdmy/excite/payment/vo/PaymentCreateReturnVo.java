package com.zxdmy.excite.payment.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.TreeMap;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/4/2 10:45
 */
@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentCreateReturnVo extends BasePaymentVo implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * qrcode场景下，返回二维码
     */
    private String qrcode;

    /**
     * page场景下，返回page字段
     */
    private String page;

    /**
     * wechat场景下，返回微信支付链接
     */
    private String wechat;

    public static final String OUT_TRADE_NO = "out_trade_no";

    public static final String TITLE = "title";

    public static final String AMOUNT = "amount";

    public static final String QRCODE = "qrcode";

    public static final String PAGE = "page";

    public static final String WECHAT = "wechat";

    /**
     * 生成字典序Map
     *
     * @return 字典序map
     */
    @JsonIgnore
    public TreeMap<String, Object> getTreeMap() {
        TreeMap<String, Object> treeMap = new TreeMap<>();
        treeMap.put(APPID, this.getAppid());
        treeMap.put(OUT_TRADE_NO, this.outTradeNo);
        treeMap.put(TITLE, this.title);
        treeMap.put(AMOUNT, this.amount);
        treeMap.put(QRCODE, this.qrcode);
        treeMap.put(PAGE, this.page);
        treeMap.put(WECHAT, this.wechat);
        treeMap.put(TIME, this.getTime());
        treeMap.put(NONCE, this.getNonce());
        return treeMap;
    }
}
