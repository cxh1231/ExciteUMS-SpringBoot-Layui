package com.zxdmy.excite.payment.service.imlp;

import com.github.binarywang.wxpay.bean.request.WxPayRefundV3Request;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayRefundV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.zxdmy.excite.common.exception.ServiceException;
import com.zxdmy.excite.common.service.IGlobalConfigService;
import com.zxdmy.excite.common.utils.HttpServletRequestUtil;
import com.zxdmy.excite.payment.bo.WechatPayBO;
import com.zxdmy.excite.payment.service.IWechatPayService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付服务实现类
 * 只实现V3版本的接口（文档地址：https://pay.weixin.qq.com/wiki/doc/apiv3/apis/index.shtml ）
 *
 * @author 拾年之璐
 * @since 2022-01-02 0002 23:34
 */
@Service
@AllArgsConstructor
public class WechatPayServiceImpl implements IWechatPayService {

    private IGlobalConfigService configService;

    private WxPayService wxPayService;

    private static final String DEFAULT_SERVICE = "wechatPay";

    private static final String DEFAULT_KEY = "wechatPay";

    // 千万不要使用这个垃圾的微信支付沙盒环境.....
    private static final Boolean SAND_BOX_ENV = false;

    /**
     * 保存微信支付配置信息至数据库
     *
     * @param wechatPayBO 微信支付实体 微信支付实体
     * @return 结果：T|F
     */
    @Override
    public boolean saveConfig(WechatPayBO wechatPayBO) {
        // 必填信息不能为空
        if (null == wechatPayBO.getAppId() || null == wechatPayBO.getMchId() || null == wechatPayBO.getMchKey()) {
            throw new ServiceException("APPID、商户号、商户秘钥不能为空！");
        }
        // 保存并返回结果
        try {
            if (configService.save(DEFAULT_SERVICE, null != wechatPayBO.getKey() ? wechatPayBO.getKey() : DEFAULT_KEY, wechatPayBO, false)) {
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
    public WechatPayBO getConfig(String confKey) {
        if (null == confKey) {
            return this.getConfig();
        }
        return (WechatPayBO) configService.get(DEFAULT_SERVICE, confKey, new WechatPayBO());
    }

    /**
     * 从数据库中读取配置信息（默认KEY）
     *
     * @return 微信支付配置信息
     */
    @Override
    public WechatPayBO getConfig() {
        return this.getConfig(DEFAULT_KEY);
    }

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
    @Override
    public String pay(String confKey, String tradeType, String description, String outTradeNo, Integer total, String openId) {
        // 构建统一下单请求参数对象
        WxPayUnifiedOrderV3Request wxPayUnifiedOrderV3Request = new WxPayUnifiedOrderV3Request();
        // 对象中写入数据
        wxPayUnifiedOrderV3Request
                // 【1】必填信息
                // 应用ID：必填，可不填，通过配置信息读取
                // .setAppid(weChatPayBO.getAppId())
                // 直连商户号：必填，可不填，通过配置信息读取
                // .setMchid(weChatPayBO.getMchId())
                // 商品描述：必填
                .setDescription(description)
                // 商户订单号：必填，同一个商户号下唯一
                .setOutTradeNo(outTradeNo)
                // 通知地址：必填，公网域名必须为https，外网可访问。
                // 可不填，通过配置信息读取
                .setNotifyUrl(this.getConfig(confKey).getNotifyUrl())
                // 订单金额：单位（分）
                .setAmount(new WxPayUnifiedOrderV3Request.Amount().setTotal(total))
                // 【2】选填信息
                // 附加信息
                .setAttach("附加信息")
                // 订单优惠标记
                .setGoodsTag("ABCD")
        // ...
        ;

        try {
            // 根据请求类型，返回指定类型，其中包含：【3】条件必填信息
            switch (tradeType.toLowerCase()) {
                // Native支付
                case "native":
                    return this.wxPayService.unifiedOrderV3(TradeTypeEnum.NATIVE, wxPayUnifiedOrderV3Request).getCodeUrl();
                // JSAPI支付
                case "jsapi":
                    // 用户在直连商户appid下的唯一标识。 下单前需获取到用户的Openid
                    wxPayUnifiedOrderV3Request.setPayer(new WxPayUnifiedOrderV3Request.Payer().setOpenid(openId));
                    return this.wxPayService.unifiedOrderV3(TradeTypeEnum.JSAPI, wxPayUnifiedOrderV3Request).getPrepayId();
                // H5支付
                case "h5":
                    wxPayUnifiedOrderV3Request.setSceneInfo(
                            new WxPayUnifiedOrderV3Request.SceneInfo()
                                    // 用户终端IP
                                    .setPayerClientIp(HttpServletRequestUtil.getRemoteIP())
                                    .setH5Info(
                                            new WxPayUnifiedOrderV3Request.H5Info()
                                                    // 场景类型
                                                    .setType("wechat")
                                    )
                    );
                    return this.wxPayService.unifiedOrderV3(TradeTypeEnum.H5, wxPayUnifiedOrderV3Request).getH5Url();
                // APP支付
                case "app":
                    return this.wxPayService.unifiedOrderV3(TradeTypeEnum.APP, wxPayUnifiedOrderV3Request).getPrepayId();
                default:
                    throw new ServiceException("输入的[" + tradeType + "]不合法，只能为native、jsapi、h5、app其一，请核实！");
            }
        } catch (WxPayException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 订单查询接口（新版V3）
     *
     * @param confKey       必填：微信支付配置信息的KEY。如果使用默认KEY，输入null
     * @param transactionId 微信订单号
     * @param outTradeNo    商户系统内部的订单号，当没提供transactionId时需要传这个。
     * @return 查询订单 返回结果对象
     */
    @Override
    public WxPayOrderQueryV3Result query(String confKey, String transactionId, String outTradeNo) {
        return null;
    }

    /**
     * 退款接口
     *
     * @param confKey     必填：微信支付配置信息的KEY。如果使用默认KEY，输入null
     * @param outTradeNo  商户订单号
     * @param outRefundNo 商户退款单号
     * @param total       订单总金额（单位：分）
     * @param refund      退款金额（单位：分）
     */
    public WxPayRefundV3Result refund(String confKey, String outTradeNo, String outRefundNo, int total, int refund) {
        WxPayRefundV3Request wxPayRefundV3Request = new WxPayRefundV3Request();
        wxPayRefundV3Request
                .setOutTradeNo(outTradeNo)
                .setOutRefundNo(outRefundNo)
                .setAmount(new WxPayRefundV3Request.Amount()
                        .setTotal(total)
                        .setRefund(refund)
                        .setCurrency("CNY")
                );
        try {
            return this.wxPayService.refundV3(wxPayRefundV3Request);
        } catch (WxPayException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 项目启动后，即注入公众号的配置（单个注册）
     */
    @PostConstruct
    public void loadWxPayConfigStorages() {
        WechatPayBO wechatPayBO = this.getConfig();
        // 公众号配置不为空
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
