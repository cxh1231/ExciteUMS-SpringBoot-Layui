package com.zxdmy.excite.payment.service;

import com.zxdmy.excite.payment.bo.AlipayBO;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/3/31 20:09
 */
public interface IAlipayService {
    /**
     * 保存信息至数据库
     *
     * @param alipayBO 支付宝支付培配置信息
     * @return 保存结果：T|F
     */
    public boolean saveConfig(AlipayBO alipayBO);
    /**
     * 统一支付服务接口（当面付、电脑网站支付、手机网站支付、APP支付）
     *
     * @param confKey     支付宝配置信息的KEY。如果加载默认的KEY，输入null
     * @param payType     支付类型：当面付（faceToFace），电脑网站支付（page），手机网站支付（wap），APP支付（app）
     * @param subject     商品名称
     * @param outTradeNo  商户订单号：商户内唯一
     * @param totalAmount 总金额（单位：元），实例：12.34
     * @param returnUrl   支付成功后跳转页面（只针对网站支付有效）
     * @param quitUrl     支付取消跳转页面（只针对手机网站支付有效）
     * @return 返回结果：当面付为二维码链接，其他均为网站Body代码
     */
    public String pay(String confKey, String payType, String subject, String outTradeNo, String totalAmount, String returnUrl, String quitUrl);

    /**
     * 查询支付订单接口
     *
     * @param confKey    可选：支付宝配置信息的KEY。如果加载默认的KEY，输入null
     * @param tradeNo    特殊可选：支付宝交易号（订单号）
     * @param outTradeNo 特殊可选：商家订单号
     * @return 查询成功：{ 0:Y，1:支付宝交易号，2:商家订单号，3:交易状态，4:订单金额，5:买家ID，6:买家支付宝账号 } <br> 查询失败：{ E，错误代码，错误描述 }
     * @apiNote tradeNo 和 outTradeNo 不能同时为空。同时存在优先取 tradeNo。
     */
    public String[] queryPay(String confKey, String tradeNo, String outTradeNo);

    /**
     * 退款接口（支持部分退款）
     *
     * @param confKey      可选：支付宝配置信息的KEY。如果加载默认的KEY，输入null
     * @param tradeNo      特殊可选：商户订单号
     * @param outTradeNo   特殊可选：商户订单号
     * @param refundAmount 必填：退款金额
     * @param reason       可选：退款原因
     * @return 本次请求退款成功：{ 0:Y，1:支付宝交易号，2:商家订单号，3:退款请求号，4:总退款金额 } <br>
     * 历史请求退款成功：{ 0:N，1:支付宝交易号，2:商家订单号，3:退款请求号，4:退款金额 } <br>
     * 退款发生错误：{ 0:E，1:错误代码，2:错误描述 }
     * @apiNote tradeNo 和 outTradeNo 不能同时为空。同时存在优先取 tradeNo。
     */
    public String[] refund(String confKey, String tradeNo, String outTradeNo, String refundAmount, String reason);
}
