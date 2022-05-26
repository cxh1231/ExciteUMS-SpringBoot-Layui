package com.zxdmy.excite.payment.service.impl;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.zxdmy.excite.common.consts.PaymentConsts;
import com.zxdmy.excite.common.exception.ServiceException;
import com.zxdmy.excite.common.service.IGlobalConfigService;
import com.zxdmy.excite.payment.bo.WechatPayBO;
import com.zxdmy.excite.payment.service.IWechatPayConfigService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/5/26 16:20
 */
@Service
@AllArgsConstructor
public class WechatPayConfigServiceImpl implements IWechatPayConfigService {

    private IGlobalConfigService configService;

    private WxPayService wxPayService;

    private static final Boolean SAND_BOX_ENV = false;

    /**
     * 保存微信支付配置信息至数据库
     *
     * @param wechatPayBO 微信支付实体 微信支付实体
     * @return 结果：T|F
     */
    @Override
    public boolean saveWechatPayConfig(WechatPayBO wechatPayBO) {
        // 必填信息不能为空
        if (null == wechatPayBO.getAppId() || null == wechatPayBO.getMchId() || null == wechatPayBO.getMchKey()) {
            throw new ServiceException("APPID、商户号、商户秘钥不能为空！");
        }
        // 保存并返回结果
        try {
            if (configService.save(PaymentConsts.ConfigName.DEFAULT_SERVICE_WECHATPAY,
                    null != wechatPayBO.getKey() ? wechatPayBO.getKey() : PaymentConsts.ConfigName.DEFAULT_KEY_WECHATPAY,
                    wechatPayBO, false)
            ) {
                this.addWechatPayConfigToRuntime(wechatPayBO);
                return true;
            }
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
        return false;
    }

    /**
     * 从数据库中读取配置信息
     *
     * @param confKey 微信支付的配置KEY
     * @return 微信支付配置信息
     */
    @Override
    public WechatPayBO getWechatPayConfig(String confKey) {
        if (null == confKey) {
            return this.getWechatPayConfig();
        }
        return (WechatPayBO) configService.get(PaymentConsts.ConfigName.DEFAULT_SERVICE_WECHATPAY, confKey, new WechatPayBO());
    }

    /**
     * 从数据库中读取配置信息（默认KEY）
     *
     * @return 微信支付配置信息
     */
    @Override
    public WechatPayBO getWechatPayConfig() {
        return this.getWechatPayConfig(PaymentConsts.ConfigName.DEFAULT_KEY_WECHATPAY);
    }

    /**
     * 项目启动后，即注入支付配置信息
     */
    @PostConstruct
    public void loadPaymentConfig() {
        // 初始化微信支付
        WechatPayBO wechatPayBO = this.getWechatPayConfig();
        if (wechatPayBO != null) {
            this.addWechatPayConfigToRuntime(wechatPayBO);
        }
    }


    /**
     * 添加账号到当前程序。动态配置使用。首次添加需初始化configStorageMap
     *
     * @param wechatPayBO 配置
     */
    private synchronized void addWechatPayConfigToRuntime(WechatPayBO wechatPayBO) {
        // 生成配置
        WxPayConfig payConfig = new WxPayConfig();
        // 填充基本配置信息
        payConfig.setAppId(StringUtils.trimToNull(wechatPayBO.getAppId()));
        payConfig.setMchId(StringUtils.trimToNull(wechatPayBO.getMchId()));
        payConfig.setMchKey(StringUtils.trimToNull(wechatPayBO.getMchKey()));
        payConfig.setApiV3Key(StringUtils.trimToNull(wechatPayBO.getApiV3Key()));
        payConfig.setSubAppId(StringUtils.trimToNull(wechatPayBO.getSubAppId()));
        payConfig.setSubMchId(StringUtils.trimToNull(wechatPayBO.getSubMchId()));
        payConfig.setKeyPath(StringUtils.trimToNull(wechatPayBO.getKeyPath()));
        payConfig.setPrivateCertPath(StringUtils.trimToNull(wechatPayBO.getPrivateCertPath()));
        payConfig.setPrivateKeyPath(StringUtils.trimToNull(wechatPayBO.getPrivateKeyPath()));
        payConfig.setNotifyUrl(StringUtils.trimToNull(wechatPayBO.getNotifyUrl()));
        // 尝试动态添加配置类
        wxPayService.setConfig(payConfig);
        // 可以指定是否使用沙箱环境
        payConfig.setUseSandboxEnv(SAND_BOX_ENV);
        if (SAND_BOX_ENV) {
            try {
                payConfig.setMchKey(wxPayService.getSandboxSignKey());
                wxPayService.setConfig(payConfig);
            } catch (WxPayException e) {
                throw new ServiceException(e.getMessage());
            }
        }
    }
}
