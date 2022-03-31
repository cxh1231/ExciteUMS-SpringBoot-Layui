package com.zxdmy.excite.payment.service;

import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayRefundV3Result;
import com.zxdmy.excite.payment.bo.WechatPayBO;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/3/31 20:08
 */
public interface IWechatPayService {

    /**
     * 保存微信支付配置信息至数据库
     *
     * @param wechatPayBO 微信支付实体 微信支付实体
     * @return 结果：T|F
     */
    boolean saveConfig(WechatPayBO wechatPayBO);

    /**
     * 从数据库中读取配置信息
     *
     * @param confKey 微信支付的配置KEY
     * @return 微信支付配置信息
     */
    WechatPayBO getConfig(String confKey);

    /**
     * 从数据库中读取配置信息（默认KEY）
     *
     * @return 微信支付配置信息
     */
    WechatPayBO getConfig();

    /**
     * 下单接口（只设置了必填信息）（V3版本）
     *
     * @param confKey     必填：微信支付配置信息的KEY。如果使用默认KEY，输入null
     * @param tradeType   必填：交易类型：jsapi（含小程序）、app、h5、native
     * @param description 必填：商品描述（商品标题）
     * @param outTradeNo  必填：商家订单号
     * @param total       必填：商品金额（单位：分）
     * @param openId      特殊必填：支付用户的OpenId，JSAPI支付时必填。
     * @return 支付返回结果：<br>APP支付、JSAPI支付为[预支付交易会话标识] <br>Native支付为[二维码链接] <br>H5支付为[支付跳转链接]
     */
    String pay(String confKey, String tradeType, String description, String outTradeNo, Integer total, String openId);

    /**
     * 订单查询接口（新版V3）
     *
     * @param confKey       必填：微信支付配置信息的KEY。如果使用默认KEY，输入null
     * @param transactionId 微信订单号
     * @param outTradeNo    商户系统内部的订单号，当没提供transactionId时需要传这个。
     * @return 查询订单 返回结果对象
     */
    WxPayOrderQueryV3Result query(String confKey, String transactionId, String outTradeNo);

    /**
     * 退款接口
     *
     * @param confKey     必填：微信付配置信息的KEY。如果使用默认KEY，输入null
     * @param outTradeNo  商户订单号
     * @param outRefundNo 商户退款单号
     * @param total       订单总金额（单位：分）
     * @param refund      退款金额（单位：分）
     */
    WxPayRefundV3Result refund(String confKey, String outTradeNo, String outRefundNo, int total, int refund);
}
