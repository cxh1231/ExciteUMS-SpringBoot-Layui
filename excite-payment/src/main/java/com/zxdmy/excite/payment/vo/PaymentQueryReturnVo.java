package com.zxdmy.excite.payment.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.TreeMap;

/**
 * 查询订单返回数据
 *
 * @author 拾年之璐
 * @since 2022/4/2 20:11
 */
@Getter
@Setter
@Accessors(chain = true)
public class PaymentQueryReturnVo extends BasePaymentVo implements Serializable {

    /**
     * 交易单号（官方渠道的账号）
     */
    @JsonProperty(value = "trade_no")
    private String tradeNo;

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
     * 订单状态
     */
    private String status;

    /**
     * 创建时间
     */
    @JsonProperty(value = "create_time")
    private String createTime;

    /**
     * 付款时间
     */
    @JsonProperty(value = "paid_time")
    private String paidTime;

    public static final String TRADE_NO = "trade_no";

    public static final String OUT_TRADE_NO = "out_trade_no";

    public static final String TITLE = "title";

    public static final String AMOUNT = "amount";

    public static final String STATUS = "status";

    public static final String CREATE_TIME = "create_time";

    public static final String PAID_TIME = "paid_time";


    /**
     * 生成字典序Map
     *
     * @return 字典序map
     */
    public TreeMap<String, Object> getTreeMap() {
        TreeMap<String, Object> treeMap = new TreeMap<>();
        treeMap.put(APPID, this.getAppid());
        treeMap.put(TRADE_NO, this.tradeNo);
        treeMap.put(OUT_TRADE_NO, this.outTradeNo);
        treeMap.put(TITLE, this.title);
        treeMap.put(AMOUNT, this.amount);
        treeMap.put(STATUS, this.status);
        treeMap.put(CREATE_TIME, this.createTime);
        treeMap.put(PAID_TIME, this.paidTime);
        treeMap.put(TIME, this.getTime());
        treeMap.put(NONCE, this.getNonce());
        return treeMap;
    }
}
