package com.zxdmy.excite.payment.api;

import cn.hutool.core.util.IdUtil;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.app.models.AlipayTradeAppPayResponse;
import com.alipay.easysdk.payment.common.models.AlipayDataDataserviceBillDownloadurlQueryResponse;
import com.alipay.easysdk.payment.common.models.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.easysdk.payment.common.models.AlipayTradeQueryResponse;
import com.alipay.easysdk.payment.common.models.AlipayTradeRefundResponse;
import com.alipay.easysdk.payment.facetoface.models.AlipayTradePrecreateResponse;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.alipay.easysdk.payment.wap.models.AlipayTradeWapPayResponse;
import com.zxdmy.excite.common.consts.PaymentConsts;
import com.zxdmy.excite.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 请求微信支付官方API的服务类实现
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/4/1 11:59
 */
@Service
@AllArgsConstructor
public class AlipayApiService {

    /**
     * 支付宝支付：统一支付服务接口（当面付、电脑网站支付、手机网站支付、APP支付）
     *
     * @param payScene    支付场景：当面付（qrcode），电脑网站支付（page），手机网站支付（wap），APP支付（app）
     * @param subject     商品名称
     * @param outTradeNo  商户订单号：商户内唯一
     * @param totalAmount 总金额（单位：元），实例：12.34
     * @param returnUrl   支付成功后跳转页面（只针对网站支付有效）
     * @param quitUrl     支付取消跳转页面（只针对手机网站支付有效）
     * @return 返回结果：当面付(payScene=qrcode)为二维码链接，其他均为网站Body代码
     */
    public String pay(String payScene, String subject, String outTradeNo, String totalAmount, String returnUrl, String quitUrl) {
        // 必填信息不能为空
        if (null == subject || null == outTradeNo || null == totalAmount) {
            throw new ServiceException("商品名称、商户订单号、商品价格不能为空！");
        }
        try {
            // 调用API发起创建支付
            // 场景：当面付（返回二维码URL）
            if (PaymentConsts.Scene.QRCODE.equalsIgnoreCase(payScene)) {
                // 创建订单
                AlipayTradePrecreateResponse response = Factory.Payment.FaceToFace().preCreate(subject, outTradeNo, totalAmount);
                // 成功？
                if (ResponseChecker.success(response))
                    return response.getQrCode();
            }
            // 场景：网站支付（返回网址body的html代码）
            else if (PaymentConsts.Scene.PAGE.equalsIgnoreCase(payScene)) {
                if (null == returnUrl) {
                    throw new ServiceException("电脑网站支付的跳转地址（returnUrl）不能为空！");
                }
                // 调用接口
                AlipayTradePagePayResponse response1 = Factory.Payment.Page().pay(subject, outTradeNo, totalAmount, returnUrl);
                // 成功？
                if (ResponseChecker.success(response1))
                    return response1.getBody();
            }
            // 手机网站支付
            else if (PaymentConsts.Scene.WAP.equalsIgnoreCase(payScene)) {
                if (null == returnUrl || null == quitUrl) {
                    throw new ServiceException("手机网站支付的取消跳转地址（quitUrl）、成功跳转地址（returnUrl）不能为空！");
                }
                AlipayTradeWapPayResponse response2 = Factory.Payment.Wap().pay(subject, outTradeNo, totalAmount, quitUrl, returnUrl);
                // 成功
                if (ResponseChecker.success(response2))
                    return response2.getBody();
            }
            // APP支付
            else if (PaymentConsts.Scene.APP.equalsIgnoreCase(payScene)) {
                AlipayTradeAppPayResponse response3 = Factory.Payment.App().pay(subject, outTradeNo, totalAmount);
                // 成功
                if (ResponseChecker.success(response3))
                    return response3.getBody();
            }
            // 其他情况：错误
            else {
                throw new ServiceException("支付场景[" + payScene + "]不在许可支付类型范围内。许可类型：当面付（qrcode），电脑网站支付（page），手机网站支付（wap），APP支付（app）");

            }
        } catch (Exception e) {
            System.err.println("调用遭遇异常，原因：" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return null;
    }

    /**
     * 查询支付订单接口
     *
     * @param tradeNo    特殊可选：支付宝交易号（订单号）
     * @param outTradeNo 特殊可选：商家订单号（与交易号二选一）
     * @return 查询成功：{ 0:Y，1:支付宝交易号，2:商家订单号，3:交易状态，4:订单金额，5:买家ID，6:买家支付宝账号 } <br> 查询失败：{ E，错误代码，错误描述 }
     * @apiNote tradeNo 和 outTradeNo 不能同时为空。同时存在优先取 tradeNo。
     */
    public String[] queryPay(String tradeNo, String outTradeNo) {
        // 判断
        if (null == tradeNo && null == outTradeNo) {
            throw new ServiceException("tradeNo 和 outTradeNo 不能同时为空！");
        }
        try {
            // 执行查询
            AlipayTradeQueryResponse response = Factory.Payment.Common().optional("trade_no", tradeNo).query(outTradeNo);
            // 请求成功（即返回信息中没有sub_code）
            if (ResponseChecker.success(response)) {
                return new String[]{
                        "Y",
                        response.tradeNo,
                        response.outTradeNo,
                        response.tradeStatus,
                        response.totalAmount,
                        response.buyerUserId,
                        response.buyerLogonId
                };
            } else {
                return new String[]{"E", response.subCode, response.subMsg};
            }
        } catch (Exception e) {
            System.err.println("调用遭遇异常，原因：" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 退款接口（支持部分退款）
     *
     * @param tradeNo      特殊可选：商户订单号
     * @param outTradeNo   特殊可选：商户订单号
     * @param refundAmount 必填：退款金额
     * @param reason       可选：退款原因
     * @return 本次请求退款成功：{ 0:Y，1:支付宝交易号，2:商家订单号，3:退款请求号，4:总退款金额 } <br>
     * 历史请求退款成功：{ 0:N，1:支付宝交易号，2:商家订单号，3:退款请求号，4:退款金额 } <br>
     * 退款发生错误：{ 0:E，1:错误代码，2:错误描述 }
     * @apiNote tradeNo 和 outTradeNo 不能同时为空。同时存在优先取 tradeNo。
     */
    public String[] refund(String tradeNo, String outTradeNo, String refundAmount, String reason) {
        // 判断
        if (null == tradeNo && null == outTradeNo) {
            throw new ServiceException("tradeNo 和 outTradeNo 不能同时为空！");
        }
        try {
            // 生成唯一的款请求号
            String outRequestNo = IdUtil.simpleUUID();
            // 发起请求
            AlipayTradeRefundResponse response = Factory.Payment.Common()
                    // 支付宝交易号
                    .optional("trade_no", tradeNo)
                    // 退款原因
                    .optional("refund_reason", reason)
                    // 退款请求号
                    .optional("out_request_no", outRequestNo)
                    // 执行退款
                    .refund(outTradeNo, refundAmount);
            // 如果请求成功（即返回信息中没有sub_code）
            if (ResponseChecker.success(response)) {
                return new String[]{
                        // 本次请求退款状态（即资金有改变，详情：https://opensupport.alipay.com/support/knowledge/27585/201602348776 ）
                        response.fundChange,
                        response.tradeNo,
                        response.outTradeNo,
                        outRequestNo,
                        response.refundFee
                };
            } else {
                return new String[]{"E", response.subCode, response.subMsg};
            }
        } catch (Exception e) {
            System.err.println("调用遭遇异常，原因：" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 查询退款
     *
     * @param outTradeNo   必填：商户订单号
     * @param outRequestNo 必填：退款请求号
     * @return 已退款：{ Y，支付宝交易号，商家订单号，退款请求号，订单金额，退款金额，退款原因 } <br> 未退款：{ N，描述 } <br> 发生错误：{ E，错误代码，错误描述 }
     */
    public String[] queryRefund(String outTradeNo, String outRequestNo) {
        // 如果请求号为空，则表示全额退款，设置请求号位商家订单号
        if (null == outRequestNo) {
            outRequestNo = outTradeNo;
        }
        try {
            // 发起请求
            AlipayTradeFastpayRefundQueryResponse response = Factory.Payment.Common().queryRefund(outTradeNo, outRequestNo);
            // 如果请求成功（即返回信息中没有sub_code）
            if (ResponseChecker.success(response)) {
                // 如果该接口返回了查询数据，则代表退款成功（详情：https://opensupport.alipay.com/support/knowledge/27585/201602348776 ）
                if (null != response.refundAmount) {
                    return new String[]{
                            "Y",
                            response.tradeNo,
                            response.outTradeNo,
                            response.outRequestNo,
                            response.totalAmount,
                            response.refundAmount,
                            response.refundReason
                    };
                } else {
                    return new String[]{
                            "N",
                            "该订单未退款或输入的退款请求号有误，请检查！"
                    };
                }
            } else {
                return new String[]{"E", response.subCode, response.subMsg};
            }
        } catch (Exception e) {
            System.err.println("调用遭遇异常，原因：" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 下载对账单（不能查询当天或当月的)
     * https://developer.aliyun.com/article/710922
     *
     * @param date 必填：交易的具体日期（如2022-01-01）或月份（2021-12）
     * @return 获取成功：{Y，下载URL} <br> 发生错误：{ E，错误代码，错误描述 }
     */
    public String[] downloadBill(String date) {

        try {
            // 发送请求
            AlipayDataDataserviceBillDownloadurlQueryResponse response = Factory.Payment.Common().downloadBill("trade", date);

            // 如果请求成功（即返回信息中没有sub_code）
            if (ResponseChecker.success(response)) {
                return new String[]{
                        "Y",
                        response.billDownloadUrl
                };
            } else {
                return new String[]{"E", response.subCode, response.subMsg};
            }

        } catch (Exception e) {
            System.err.println("调用遭遇异常，原因：" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

}
