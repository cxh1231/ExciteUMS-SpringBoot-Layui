package com.zxdmy.excite.payment.api;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyV3Result;
import com.github.binarywang.wxpay.bean.request.WxPayRefundV3Request;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayRefundV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.zxdmy.excite.common.consts.PaymentConsts;
import com.zxdmy.excite.common.enums.PaymentEnums;
import com.zxdmy.excite.common.exception.ServiceException;
import com.zxdmy.excite.common.utils.HttpServletRequestUtil;
import com.zxdmy.excite.payment.model.PaymentNotifyModel;
import com.zxdmy.excite.payment.vo.PaymentCreateResponseVo;
import com.zxdmy.excite.payment.vo.PaymentQueryResponseVo;
import com.zxdmy.excite.ums.entity.UmsOrder;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/4/1 11:59
 */
@Component
@AllArgsConstructor
public class WechatPayApiService {

    private WxPayService wxPayService;

    final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 下单接口（只设置了必填信息）（V3版本）
     *
     * @param payScene    必填：交易类型：qrcode（即native支付）、wechat（微信内支付，含小程序）、app、wap（手机网站）
     * @param description 必填：商品描述（商品标题）
     * @param outTradeNo  必填：商家订单号
     * @param total       必填：商品金额（格式：88.88，内部自动完成单位转换）
     * @param openId      特殊必填：支付用户的OpenId，JSAPI支付时必填。
     * @return 支付返回结果：<br>APP支付、JSAPI支付为[预支付交易会话标识] <br>Native支付为[二维码链接] <br>H5支付为[支付跳转链接]
     */
    public PaymentCreateResponseVo pay(String payScene, String description, String outTradeNo, String total, String openId) {
        // 定义支付返回实体
        PaymentCreateResponseVo responseVo = new PaymentCreateResponseVo();
        // 必填信息不能为空
        if (payScene == null || description == null || outTradeNo == null || total == null) {
            this.log.error("\n【参数错误】错误详情：{}", "必填参数不能为空");
            responseVo.setSubCode(PaymentEnums.ERROR_PARAM.getCode())
                    .setSubMsg(PaymentEnums.ERROR_PARAM.getMessage() + "必填参数不能为空");
            return responseVo;
        }
        // 修改金额格式（微信支付的单位是分）
        BigDecimal decimal = new BigDecimal(total).multiply(new BigDecimal("100"));
        Integer amount = decimal.intValue();
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
                .setAmount(new WxPayUnifiedOrderV3Request.Amount().setTotal(amount));

        // 尝试根据请求类型，获取下单结果
        String qrcode = null;
        String prepayId = null;
        String url = null;
        try {
            // 场景：qrcode支付（native支付）
            if (PaymentConsts.Scene.QRCODE.equalsIgnoreCase(payScene)) {
                qrcode = this.wxPayService.unifiedOrderV3(TradeTypeEnum.NATIVE, wxPayUnifiedOrderV3Request).getCodeUrl();
            }
            // 场景：wechat（微信内支付，含小程序）
            else if (PaymentConsts.Scene.WECHAT.equalsIgnoreCase(payScene)) {
                // 微信内支付，含小程序，需要传入openId
                if (null == openId) {
                    this.log.error("\n【参数错误】错误详情：{}", "微信内支付必须传入openId");
                    responseVo.setSubCode(PaymentEnums.ERROR_PARAM.getCode())
                            .setSubMsg(PaymentEnums.ERROR_PARAM.getMessage() + "微信内支付必须传入openId");
                    return responseVo;
                }
                // 用户在直连商户 appid下的唯一标识。下单前需获取到用户的 Openid
                wxPayUnifiedOrderV3Request.setPayer(new WxPayUnifiedOrderV3Request.Payer().setOpenid(openId));
                prepayId = this.wxPayService.unifiedOrderV3(TradeTypeEnum.JSAPI, wxPayUnifiedOrderV3Request).getPrepayId();
            }
            // 场景：手机网站支付
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
                url = this.wxPayService.unifiedOrderV3(TradeTypeEnum.H5, wxPayUnifiedOrderV3Request).getH5Url();
            }
            // 场景：app支付
            else if (PaymentConsts.Scene.APP.equalsIgnoreCase(payScene)) {
                prepayId = this.wxPayService.unifiedOrderV3(TradeTypeEnum.APP, wxPayUnifiedOrderV3Request).getPrepayId();
            }
            // 其他：暂不支持
            else {
                this.log.error("\n【参数错误】错误详情：{}", "支付场景[" + payScene + "]暂不支持，只支持qrcode、wechat、wap、app。");
                responseVo.setSubCode(PaymentEnums.ERROR_PARAM.getCode())
                        .setSubMsg(PaymentEnums.ERROR_PARAM.getMessage() + "支付场景[" + payScene + "]暂不支持，只支持qrcode、wechat、wap、app");
                return responseVo;
            }
        } catch (WxPayException e) {
            this.log.error("\n【系统错误】错误详情：{}", e.getMessage());
            responseVo.setSubCode(PaymentEnums.ERROR_SYSTEM.getCode())
                    .setSubMsg(PaymentEnums.ERROR_SYSTEM.getMessage() + "：" + e.getMessage());
            return responseVo;
        }
        // 将下单结果写入返回实体
        responseVo.setSubCode(PaymentEnums.SUCCESS.getCode())
                .setSubMsg(PaymentEnums.SUCCESS.getMessage())
                .setTitle(description)
                .setOutTradeNo(outTradeNo)
                .setAmount(total)
                .setQrcode(qrcode)
                .setPrepayId(prepayId)
                .setUrl(url);
        return responseVo;
    }

    /**
     * 订单查询接口（新版V3）
     *
     * @param transactionId 微信订单号
     * @param outTradeNo    商户系统内部的订单号，当没提供transactionId时需要传这个。
     * @return 查询订单 返回结果对象
     */
    public PaymentQueryResponseVo query(String transactionId, String outTradeNo) {
        // 创建返回结果对象
        PaymentQueryResponseVo responseVo = new PaymentQueryResponseVo();
        // 必填信息不能为空
        if ((null == transactionId || transactionId.equals("")) && (null == outTradeNo || outTradeNo.equals(""))) {
            this.log.error("\n【参数错误】错误详情：{}", "微信订单号和商户系统内部的订单号不能同时为空");
            responseVo.setSubCode(PaymentEnums.ERROR_PARAM.getCode())
                    .setSubMsg(PaymentEnums.ERROR_PARAM.getMessage() + "微信订单号和商户系统内部的订单号不能同时为空");
            return responseVo;
        }
        // 尝试查询订单信息
        try {
            WxPayOrderQueryV3Result result = this.wxPayService.queryOrderV3(transactionId, outTradeNo);

            System.out.println(result);
            // 将结果写入返回实体
            responseVo.setSubCode(PaymentEnums.SUCCESS.getCode())
                    .setSubMsg(PaymentEnums.SUCCESS.getMessage())
                    // .setTitle("")
                    .setAmount(result.getAmount().getCurrency()) // TODO 这里是分，需要转换成元
                    .setTradeNo(result.getTransactionId())
                    .setOutTradeNo(result.getOutTradeNo())
                    .setPaidTime(result.getSuccessTime());
            // 根据交易状态修改
            if (result.getTradeState().equals("NOTPAY")) {
                responseVo.setStatus(PaymentConsts.Status.WAIT);
            } else {
                responseVo.setStatus(result.getTradeState());
            }
            return responseVo;
        }
        // 订单不存在等，则抛出异常
        catch (WxPayException e) {
            this.log.error("\n【查询错误】错误详情：{}", e.getMessage());
            responseVo.setSubCode(PaymentEnums.ERROR.getCode())
                    .setSubMsg(PaymentEnums.ERROR.getMessage() + "：" + e.getMessage());
            return responseVo;
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


    /**
     * 验证支付结果异步通知
     *
     * @param notifyData 通知数据
     * @param timeStamp  时间戳
     * @param nonce      随机串
     * @param signature  已签名字符串
     * @param serial     证书序列号
     * @return 校验结果
     */
    public PaymentNotifyModel verifyNotify(String notifyData, String timeStamp, String nonce, String signature, String serial) {
        // 设置签名实体
        SignatureHeader signatureHeader = new SignatureHeader();
        signatureHeader.setNonce(nonce);
        signatureHeader.setSignature(signature);
        signatureHeader.setSerial(serial);
        signatureHeader.setTimeStamp(timeStamp);
        try {
            // 验签，并解析数据
            final WxPayOrderNotifyV3Result notifyResult = wxPayService.parseOrderNotifyV3Result(notifyData, signatureHeader);
            // 支付成功
            if (PaymentConsts.Status.SUCCESS.equalsIgnoreCase(notifyResult.getResult().getTradeState())) {
                DecimalFormat df = new DecimalFormat("0.00");
                // 返回的实体
                PaymentNotifyModel notifyModel = new PaymentNotifyModel();
                notifyModel.setTitle("None")
                        .setAmount("" + df.format((float) notifyResult.getResult().getAmount().getTotal() / 100))
                        .setTradeNo(notifyResult.getResult().getTransactionId())
                        .setOutTradeNo(notifyResult.getResult().getOutTradeNo())
                        .setPaidTime(notifyResult.getResult().getSuccessTime().substring(0, 19))
                        .setUserId(notifyResult.getResult().getPayer().getOpenid())
                        .setStatus(PaymentConsts.Status.SUCCESS);
                // 返回回调数据
                return notifyModel;
            } else {
                System.err.println("签名验证失败，请求数据非官方请求！");
            }

        } catch (WxPayException e) {
            System.err.println("微信支付验签异常，详情：" + e.getMessage());
        }
        return null;
    }

}
