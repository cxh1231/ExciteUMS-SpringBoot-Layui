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
import com.zxdmy.excite.common.enums.PaymentEnums;
import com.zxdmy.excite.common.exception.ServiceException;
import com.zxdmy.excite.payment.model.PaymentNotifyModel;
import com.zxdmy.excite.payment.vo.PaymentCreateResponseVo;
import com.zxdmy.excite.payment.vo.PaymentQueryResponseVo;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 请求微信支付官方API的服务类实现
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/4/1 11:59
 */
@Component
@AllArgsConstructor
public class AlipayApiService {

    final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 支付宝支付：统一支付服务接口（当面付、电脑网站支付、手机网站支付、APP支付）
     *
     * @param payScene    支付场景：当面付（qrcode），电脑网站支付（page），手机网站支付（wap），APP支付（app）
     * @param subject     商品名称
     * @param outTradeNo  商户订单号：商户内唯一
     * @param totalAmount 总金额（单位：元），实例：12.34
     * @param returnUrl   支付成功后跳转页面（只针对网站支付有效）
     * @param quitUrl     支付取消跳转页面（只针对手机网站支付有效）
     * @return 返回结果：PaymentCreateResponseVo实体，其中[subCode=1000]表示成功，其他表示失败，subMsg表示失败原因
     */
    public PaymentCreateResponseVo pay(String payScene, String subject, String outTradeNo, String totalAmount, String returnUrl, String quitUrl) {
        // 定义支付返回实体
        PaymentCreateResponseVo responseVo = new PaymentCreateResponseVo();
        // 必填信息不能为空
        if (null == subject || null == outTradeNo || null == totalAmount || null == payScene) {
            this.log.error("\n【参数错误】错误详情：{}", "必填参数不能为空");
            responseVo.setCode(PaymentEnums.ERROR_PARAM.getCode())
                    .setMsg(PaymentEnums.ERROR_PARAM.getMessage() + "：必填参数不能为空");
            return responseVo;
        }
        // 尝试发起支付，并获取各个场景的支付代码
        String qrCode = null;
        String bodyCode = null;
        try {
            // 调用API发起创建支付
            // 场景：当面付（返回二维码URL）
            if (PaymentConsts.Scene.QRCODE.equalsIgnoreCase(payScene)) {
                // 创建订单
                AlipayTradePrecreateResponse response = Factory.Payment.FaceToFace().preCreate(subject, outTradeNo, totalAmount);
                // 成功？
                if (ResponseChecker.success(response))
                    qrCode = response.getQrCode();
            }
            // 场景：网站支付（返回网址body的html代码）
            else if (PaymentConsts.Scene.PAGE.equalsIgnoreCase(payScene)) {
                // 参数错误直接返回
                if (null == returnUrl) {
                    this.log.error("\n【参数错误】错误详情：{}", "电脑网站支付的跳转地址（returnUrl）不能为空");
                    responseVo.setCode(PaymentEnums.ERROR_PARAM.getCode())
                            .setMsg(PaymentEnums.ERROR_PARAM.getMessage() + "：电脑网站支付的跳转地址（returnUrl）不能为空");
                    return responseVo;
                }
                // 调用接口
                AlipayTradePagePayResponse response1 = Factory.Payment.Page().pay(subject, outTradeNo, totalAmount, returnUrl);
                // 成功？
                if (ResponseChecker.success(response1))
                    bodyCode = response1.getBody();
            }
            // 场景：手机网站支付
            else if (PaymentConsts.Scene.WAP.equalsIgnoreCase(payScene)) {
                // 参数错误直接返回
                if (null == returnUrl || null == quitUrl) {
                    this.log.error("\n【参数错误】错误详情：{}", "手机网站支付的取消跳转地址（quitUrl）、成功跳转地址（returnUrl）不能为空");
                    responseVo.setCode(PaymentEnums.ERROR_PARAM.getCode())
                            .setMsg(PaymentEnums.ERROR_PARAM.getMessage() + "：手机网站支付的取消跳转地址（quitUrl）、成功跳转地址（returnUrl）不能为空");
                    return responseVo;
                }
                AlipayTradeWapPayResponse response2 = Factory.Payment.Wap().pay(subject, outTradeNo, totalAmount, quitUrl, returnUrl);
                // 成功
                if (ResponseChecker.success(response2))
                    bodyCode = response2.getBody();
            }
            // 场景：APP支付
            else if (PaymentConsts.Scene.APP.equalsIgnoreCase(payScene)) {
                AlipayTradeAppPayResponse response3 = Factory.Payment.App().pay(subject, outTradeNo, totalAmount);
                // 成功
                if (ResponseChecker.success(response3))
                    bodyCode = response3.getBody();
            }
            // 其他情况：错误
            else {
                this.log.error("\n【参数错误】错误详情：{}", "支付场景[" + payScene + "]不在许可支付类型范围内。许可类型：当面付（qrcode），电脑网站支付（page），手机网站支付（wap），APP支付（app）");
                responseVo.setCode(PaymentEnums.ERROR_PARAM.getCode())
                        .setMsg(PaymentEnums.ERROR_PARAM.getMessage() + "：支付场景[" + payScene + "]不在许可支付类型范围内。许可类型：当面付（qrcode），电脑网站支付（page），手机网站支付（wap），APP支付（app）");
                return responseVo;
            }
        } catch (Exception e) {
            this.log.error("\n【系统错误】错误详情：{}", e.getMessage());
            responseVo.setCode(PaymentEnums.ERROR_SYSTEM.getCode())
                    .setMsg(PaymentEnums.ERROR_SYSTEM.getMessage() + "：" + e.getMessage());
            return responseVo;
        }
        // 将基本信息填入实体，并返回
        responseVo.setCode(PaymentEnums.SUCCESS.getCode())
                .setMsg(PaymentEnums.SUCCESS.getMessage());
        responseVo.setTitle(subject)
                .setOutTradeNo(outTradeNo)
                .setAmount(totalAmount)
                .setQrcode(qrCode)
                .setPage(bodyCode);
        return responseVo;
    }

    /**
     * 查询支付订单接口
     *
     * @param tradeNo    特殊可选：支付宝交易号（订单号）
     * @param outTradeNo 特殊可选：商家订单号（与交易号二选一）
     * @return 返回查询结果：status ERROR为查询失败，其他为查询成功
     * @apiNote tradeNo 和 outTradeNo 不能同时为空。同时存在优先取 tradeNo。
     */
    public PaymentQueryResponseVo query(String tradeNo, String outTradeNo) {
        // 构造返回对象
        PaymentQueryResponseVo returnVo = new PaymentQueryResponseVo();
        // 判断
        if (null == tradeNo && null == outTradeNo) {
            this.log.error("\n【参数错误】错误详情：{}", "tradeNo 和 outTradeNo 不能同时为空！");
            // 设置返回结果
            returnVo.setStatus(PaymentConsts.Status.ERROR)
                    .setCode(PaymentEnums.ERROR.getCode())
                    .setMsg("tradeNo 和 outTradeNo 不能同时为空！");
            return returnVo;
        }
        try {
            // 执行查询
            AlipayTradeQueryResponse response = Factory.Payment.Common().optional("trade_no", tradeNo).query(outTradeNo);
            // 订单查询成功（即返回信息中没有 sub_code）
            if (ResponseChecker.success(response)) {
                returnVo.setCode(PaymentEnums.SUCCESS.getCode())
                        .setMsg(response.msg);
                returnVo.setTitle(response.subject)
                        .setAmount(response.totalAmount)
                        .setTradeNo(response.tradeNo)
                        .setOutTradeNo(response.outTradeNo)
                        .setPaidTime(response.sendPayDate);
                // 根据订单状态，设置支付状态
                if (response.tradeStatus.equalsIgnoreCase("TRADE_SUCCESS")) {
                    returnVo.setStatus(PaymentConsts.Status.SUCCESS);
                } else if (response.tradeStatus.equalsIgnoreCase("WAIT_BUYER_PAY")) {
                    returnVo.setStatus(PaymentConsts.Status.WAIT);
                } else if (response.tradeStatus.equalsIgnoreCase("TRADE_CLOSED")) {
                    returnVo.setStatus(PaymentConsts.Status.CLOSED);
                } else if (response.tradeStatus.equalsIgnoreCase("TRADE_FINISHED")) {
                    returnVo.setStatus(PaymentConsts.Status.FINISHED);
                } else {
                    returnVo.setStatus(PaymentConsts.Status.ERROR);
                }
            }
            // 订单查询失败（可能订单不存在）
            else {
                this.log.error("\n【订单查询错误】错误代码：{}\n错误描述：{}", response.subCode, response.subMsg);
                // 写入错误信息
                returnVo.setCode(PaymentEnums.ERROR.getCode())
                        .setMsg(PaymentEnums.ERROR.getCode() + "：" + response.subMsg);
            }
            // 返回结果
            return returnVo;
        } catch (Exception e) {
            this.log.error("\n【发生异常】异常原因：{}", e.getMessage());
            // 设置返回结果
            returnVo.setStatus(PaymentConsts.Status.ERROR)
                    .setCode(PaymentEnums.ERROR.getCode())
                    .setMsg(PaymentEnums.ERROR.getCode() + "：" + e.getMessage());
            return returnVo;
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

    public PaymentNotifyModel verifyNotify(Map<String, String[]> requestParameterMap) {
        // 参数不能为空
        if (null == requestParameterMap) {
            throw new ServiceException("请求数据为空，请检查系统日志！");
        }
        // 将支付宝异步回调信息，转换为 Map<String, String> 格式
        Map<String, String> map = new HashMap<>();
        for (String key : requestParameterMap.keySet()) {
            String[] values = requestParameterMap.get(key);
            String value = "";
            for (int i = 0; i < values.length; i++) {
                value = (i == values.length - 1) ? value + values[i] : value + values[i] + ",";
            }
            map.put(key, value);
        }
        // 尝试验签
        try {
            // 验证签名成功
            if (Factory.Payment.Common().verifyNotify(map)) {
                // 定义返回实体
                PaymentNotifyModel notifyModel = new PaymentNotifyModel();
                notifyModel.setTitle(map.getOrDefault("subject", "无标题"))
                        .setAmount(map.get("receipt_amount"))
                        .setTradeNo(map.get("trade_no"))
                        .setOutTradeNo(map.get("out_trade_no"))
                        .setPaidTime(map.get("gmt_payment").replace(' ', 'T'))
                        .setUserId(map.getOrDefault("buyer_id", "None"));
                if (map.get("trade_status").contains(PaymentConsts.Status.SUCCESS)) {
                    notifyModel.setStatus(PaymentConsts.Status.SUCCESS);
                }
                // 返回回调数据
                return notifyModel;
            } else {
                System.err.println("签名验证失败，请求数据非官方请求！");
            }
        } catch (Exception e) {
            System.err.println("支付宝验签异常，详情：" + e.getMessage());
        }
        return null;
    }
}
