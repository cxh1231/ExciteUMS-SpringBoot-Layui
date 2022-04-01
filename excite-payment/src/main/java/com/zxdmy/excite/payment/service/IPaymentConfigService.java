package com.zxdmy.excite.payment.service;

import com.zxdmy.excite.payment.bo.AlipayBO;
import com.zxdmy.excite.payment.bo.WechatPayBO;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/4/1 12:03
 */
public interface IPaymentConfigService {

    /**
     * 保存支付宝支付配置信息至数据库
     *
     * @param alipayBO 支付宝支付培配置信息
     * @return 保存结果：T|F
     */
    boolean saveAlipayConfig(AlipayBO alipayBO);

    /**
     * 从数据库读取支付宝支付配置信息（默认KEY）
     *
     * @return 支付宝支付配置信息
     */
    AlipayBO getAlipayConfig(String confKey);

    /**
     * 从数据库读取支付宝支付配置信息（指定KEY）
     *
     * @return 支付宝支付配置信息
     */
    AlipayBO getAlipayConfig();

    /**
     * 保存微信支付配置信息至数据库
     *
     * @param wechatPayBO 微信支付实体 微信支付实体
     * @return 结果：T|F
     */
    boolean saveWechatPayConfig(WechatPayBO wechatPayBO);

    /**
     * 从数据库中读取配置信息
     *
     * @param confKey 微信支付的配置KEY
     * @return 微信支付配置信息
     */
    WechatPayBO getWechatPayConfig(String confKey);

    /**
     * 从数据库中读取配置信息（默认KEY）
     *
     * @return 微信支付配置信息
     */
    WechatPayBO getWechatPayConfig();

}
