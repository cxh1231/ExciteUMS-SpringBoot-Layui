package com.zxdmy.excite.payment.service.impl;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.zxdmy.excite.common.consts.PaymentConsts;
import com.zxdmy.excite.common.exception.ServiceException;
import com.zxdmy.excite.common.service.IGlobalConfigService;
import com.zxdmy.excite.payment.bo.AlipayBO;
import com.zxdmy.excite.payment.bo.WechatPayBO;
import com.zxdmy.excite.payment.service.IPaymentConfigService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * <p>
 * 支付的配置保存与读取服务实现
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/4/1 14:33
 */
@Service
@AllArgsConstructor
public class PaymentConfigServiceImpl implements IPaymentConfigService {

    private IGlobalConfigService configService;

    private WxPayService wxPayService;

    private static final Boolean SAND_BOX_ENV = false;

    /**
     * 保存支付宝支付配置信息至数据库
     *
     * @param alipayBO 支付宝支付培配置信息
     * @return 保存结果：T|F
     */
    @Override
    public boolean saveAlipayConfig(AlipayBO alipayBO) {
        // 必填信息不能为空
        if (null == alipayBO.getAppId() || null == alipayBO.getMerchantPrivateKey()) {
            throw new ServiceException("APPID和应用私钥不能为空，请检查！");
        }
        // 如果支付宝公钥为空，说明是证书模式，则三个证书不能为空
        if (null == alipayBO.getAlipayPublicKey())
            if (null == alipayBO.getMerchantCertPath() || null == alipayBO.getAlipayCertPath() || null == alipayBO.getAlipayRootCertPath())
                throw new ServiceException("证书模式下，[应用公钥证书][支付宝公钥证书][支付宝根证书]不能为空，请检查！");
        // 保存信息至数据库
        try {
            if (configService.save(
                    PaymentConsts.ConfigName.DEFAULT_SERVICE_ALIPAY,
                    alipayBO.getKey() != null ? alipayBO.getKey() : PaymentConsts.ConfigName.DEFAULT_KEY_ALIPAY,
                    alipayBO, false)
            ) {
                // 如果信息保存/更新成功，则更新配置
                this.addAlipayOptionsRuntime(alipayBO);
                return true;
            }
        } catch (JsonProcessingException e) {
            throw new ServiceException(e.getMessage());
        }
        return false;
    }

    /**
     * 从数据库读取支付宝支付配置信息（默认KEY）
     *
     * @return 支付宝支付配置信息
     */
    @Override
    public AlipayBO getAlipayConfig(String confKey) {
        if (null == confKey) {
            return this.getAlipayConfig();
        }
        return (AlipayBO) configService.get(PaymentConsts.ConfigName.DEFAULT_SERVICE_ALIPAY, confKey, new AlipayBO());
    }

    /**
     * 从数据库读取支付宝支付配置信息（指定KEY）
     *
     * @return 支付宝支付配置信息
     */
    @Override
    public AlipayBO getAlipayConfig() {
        return this.getAlipayConfig(PaymentConsts.ConfigName.DEFAULT_KEY_ALIPAY);
    }

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
        // 初始化支付宝支付
        AlipayBO alipayBO = this.getAlipayConfig();
        if (alipayBO != null) {
            this.addAlipayOptionsRuntime(alipayBO);
        }
        // 初始化微信支付
        WechatPayBO wechatPayBO = this.getWechatPayConfig();
        if (wechatPayBO != null) {
            this.addWechatPayConfigToRuntime(wechatPayBO);
        }
    }

    private synchronized void addAlipayOptionsRuntime(AlipayBO alipayBO) {
        // 将配置信息写入支付宝支付配置类中
        Config config = new Config();
        config.protocol = alipayBO.getProtocol();
        config.gatewayHost = alipayBO.getGatewayHost();
        config.signType = alipayBO.getSignType();

        config.appId = alipayBO.getAppId();

        // 为避免私钥随源码泄露，推荐从文件中读取私钥字符串而不是写入源码中
        config.merchantPrivateKey = alipayBO.getMerchantPrivateKey();
        // 如果三个证书不为空，则优先设置三个证书
        if (null != alipayBO.getMerchantCertPath() && null != alipayBO.getAlipayCertPath() && null != alipayBO.getAlipayRootCertPath()) {
            //注：证书文件路径支持设置为文件系统中的路径或CLASS_PATH中的路径，优先从文件系统中加载，加载失败后会继续尝试从CLASS_PATH中加载
            config.merchantCertPath = alipayBO.getMerchantCertPath();
            config.alipayCertPath = alipayBO.getAlipayCertPath();
            config.alipayRootCertPath = alipayBO.getAlipayRootCertPath();
        } else if (null != alipayBO.getAlipayPublicKey()) {
            //注：如果采用非证书模式，则无需赋值上面的三个证书路径，改为赋值如下的支付宝公钥字符串即可
            config.alipayPublicKey = alipayBO.getAlipayPublicKey();
        } else {
            throw new ServiceException("配置信息有误：证书模式配置不完整或未配置公钥模式，请检查！");
        }

        //可设置异步通知接收服务地址（可选）
        config.notifyUrl = alipayBO.getNotifyUrl();

        //可设置AES密钥，调用AES加解密相关接口时需要（可选）
        config.encryptKey = alipayBO.getEncryptKey();

        Factory.setOptions(config);
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
