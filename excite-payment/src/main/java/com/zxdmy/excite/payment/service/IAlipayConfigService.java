package com.zxdmy.excite.payment.service;

import com.zxdmy.excite.payment.bo.AlipayBO;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/5/26 16:19
 */
public interface IAlipayConfigService {


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
}
