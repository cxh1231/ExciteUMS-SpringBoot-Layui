package com.zxdmy.excite.payment.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.TreeMap;

/**
 * 订单查询接口请求数据
 *
 * @author 拾年之璐
 * @since 2022/4/2 20:11
 */
@Getter
@Setter
@Accessors(chain = true)
public class PaymentQueryRequestVO extends BasePaymentVo implements Serializable {

    /**
     * 交易单号（官方渠道的账号）
     */
    @SerializedName(value = "tradeNo", alternate = {"trade_no"})
    @Length(min = 8, max = 64, message = "交易单号格式错误")
    @JsonProperty(value = "trade_no")
    private String tradeNo;

    /**
     * 商户单号（本平台）
     */
    @SerializedName(value = "outTradeNo", alternate = {"out_trade_no"})
    @Length(min = 8, max = 64, message = "商户单号格式错误")
    @JsonProperty(value = "out_trade_no")
    private String outTradeNo;

    public static final String TRADE_NO = "trade_no";

    public static final String OUT_TRADE_NO = "out_trade_no";

    /**
     * 生成字典序Map
     *
     * @return 字典序map
     */
    public TreeMap<String, Object> getTreeMap() {
        TreeMap<String, Object> treeMap = new TreeMap<>();
        treeMap.put(TRADE_NO, this.tradeNo);
        treeMap.put(OUT_TRADE_NO, this.outTradeNo);

        treeMap.put(APPID, this.getAppid());
        treeMap.put(TIME, this.getTime());
        treeMap.put(NONCE, this.getNonce());
        return treeMap;
    }
}
