package com.zxdmy.excite.payment.api;

import com.github.binarywang.wxpay.bean.request.WxPayRefundV3Request;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayRefundV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.zxdmy.excite.common.consts.PaymentConsts;
import com.zxdmy.excite.common.exception.ServiceException;
import com.zxdmy.excite.common.utils.HttpServletRequestUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/4/1 11:59
 */
@Service
@AllArgsConstructor
public class WechatPayApiService {

    private WxPayService wxPayService;

    /**
     * 下单接口（只设置了必填信息）（V3版本）
     *
     * @param payScene    必填：交易类型：qrcode（即native支付）、wechat（微信内支付，含小程序）、app、wap（手机网站）
     * @param description 必填：商品描述（商品标题）
     * @param outTradeNo  必填：商家订单号
     * @param total       必填：商品金额（单位：分）
     * @param openId      特殊必填：支付用户的OpenId，JSAPI支付时必填。
     * @return 支付返回结果：<br>APP支付、JSAPI支付为[预支付交易会话标识] <br>Native支付为[二维码链接] <br>H5支付为[支付跳转链接]
     */
    public String pay(String payScene, String description, String outTradeNo, Integer total, String openId) {
        // 构建统一下单请求参数对象
        WxPayUnifiedOrderV3Request wxPayUnifiedOrderV3Request = new WxPayUnifiedOrderV3Request();
        // 对象中写入数据
        wxPayUnifiedOrderV3Request
                // 商品描述：必填
                .setDescription(description)
                // 商户订单号：必填，同一个商户号下唯一
                .setOutTradeNo(outTradeNo)
                // 通知地址：必填，公网域名必须为https，外网可访问。
                // 可不填，通过配置信息读取（但是官方插件没写。。。）
                .setNotifyUrl(this.wxPayService.getConfig().getNotifyUrl())
                // 订单金额：单位（分）
                .setAmount(new WxPayUnifiedOrderV3Request.Amount().setTotal(total));

        try {
            // 根据请求类型，返回指定类型
            // qrcode支付（native支付）
            if (PaymentConsts.Scene.QRCODE.equalsIgnoreCase(payScene)) {
                return this.wxPayService.unifiedOrderV3(TradeTypeEnum.NATIVE, wxPayUnifiedOrderV3Request).getCodeUrl();
            }
            // wechat（微信内支付，含小程序）
            else if (PaymentConsts.Scene.WECHAT.equalsIgnoreCase(payScene)) {
                // 用户在直连商户appid下的唯一标识。下单前需获取到用户的Openid
                wxPayUnifiedOrderV3Request.setPayer(new WxPayUnifiedOrderV3Request.Payer().setOpenid(openId));
                return this.wxPayService.unifiedOrderV3(TradeTypeEnum.JSAPI, wxPayUnifiedOrderV3Request).getPrepayId();
            }
            // 手机网站支付
            else if (PaymentConsts.Scene.WAP.equalsIgnoreCase(payScene)) {
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
            }
            // app支付
            else if (PaymentConsts.Scene.APP.equalsIgnoreCase(payScene)) {
                return this.wxPayService.unifiedOrderV3(TradeTypeEnum.APP, wxPayUnifiedOrderV3Request).getPrepayId();
            }
            // 其他：暂不支持
            else {
                throw new ServiceException("支付场景[" + payScene + "]暂不支持，只支持qrcode、wechat、wap、app。");
            }
        } catch (WxPayException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 订单查询接口（新版V3）
     *
     * @param transactionId 微信订单号
     * @param outTradeNo    商户系统内部的订单号，当没提供transactionId时需要传这个。
     * @return 查询订单 返回结果对象
     */
    public WxPayOrderQueryV3Result query(String transactionId, String outTradeNo) {
        try {
            return this.wxPayService.queryOrderV3(transactionId, outTradeNo);
        } catch (WxPayException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 退款接口
     *
     * @param outTradeNo  商户订单号
     * @param outRefundNo 商户退款单号
     * @param total       订单总金额（单位：分）
     * @param refund      退款金额（单位：分）
     */
    public WxPayRefundV3Result refund(String outTradeNo, String outRefundNo, int total, int refund) {
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
}
