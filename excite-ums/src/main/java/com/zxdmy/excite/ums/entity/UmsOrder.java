package com.zxdmy.excite.ums.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * UMS系统的交易平台的订单信息
 * </p>
 *
 * @author 拾年之璐
 * @since 2022-05-26
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("ums_order")
public class UmsOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * app用户的appid
     */
    private String appId;

    /**
     * 用户ID，微信：OpenID，支付宝：付款用户唯一标识
     */
    private String userId;

    /**
     * 交易单号（官方平台）
     */
    private String tradeNo;

    /**
     * 商户单号（本平台）
     */
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
     * 订单状态：WAIT-等待付款、SUCCESS-付款成功、CLOSED-交易关闭、FINISHED交易结束（不可退款）
     */
    private String status;

    /**
     * 支付渠道：wechat | alipay
     */
    private String payChannel;

    /**
     * 支付场景：qrcode | page | wechat
     */
    private String payScene;

    /**
     * 支付成功的回调地址
     */
    private String notifyUrl;

    /**
     * 支付成功的返回网址
     */
    private String returnUrl;

    /**
     * 支付取消的返回网址
     */
    private String cancelUrl;

    /**
     * 附加信息
     */
    private String attach;

    /**
     * 支付成功的回调结果
     */
    private String notifyResult;

    /**
     * 订单创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;

    /**
     * 订单支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime paidTime;

    /**
     * 回调时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime notifyTime;

    /**
     * 订单更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updateTime;

    /**
     * 订单删除时间
     */
    @JsonIgnore
    private LocalDateTime deleteTime;


    public static final String ID = "id";

    public static final String APP_ID = "app_id";

    public static final String TRADE_NO = "trade_no";

    public static final String OUT_TRADE_NO = "out_trade_no";

    public static final String TITLE = "title";

    public static final String AMOUNT = "amount";

    public static final String STATUS = "status";

    public static final String PAY_CHANNEL = "pay_channel";

    public static final String PAY_SCENE = "pay_scene";

    public static final String NOTIFY_URL = "notify_url";

    public static final String RETURN_URL = "return_url";

    public static final String CANCEL_URL = "cancel_url";

    public static final String ATTACH = "attach";

    public static final String NOTIFY_RESULT = "notify_result";

    public static final String CREATE_TIME = "create_time";

    public static final String PAID_TIME = "paid_time";

    public static final String NOTIFY_TIME = "notify_time";

    public static final String UPDATE_TIME = "update_time";

    public static final String DELETE_TIME = "delete_time";

}
