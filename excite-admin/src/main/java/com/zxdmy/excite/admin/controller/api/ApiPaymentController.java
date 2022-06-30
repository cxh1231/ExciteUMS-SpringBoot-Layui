package com.zxdmy.excite.admin.controller.api;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyV3Result;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.zxdmy.excite.common.base.BaseController;
import com.zxdmy.excite.common.base.BaseResult;
import com.zxdmy.excite.common.consts.PaymentConsts;
import com.zxdmy.excite.common.enums.ReturnCode;
import com.zxdmy.excite.common.utils.OrderUtils;
import com.zxdmy.excite.common.utils.SignUtils;
import com.zxdmy.excite.payment.api.AlipayApiService;
import com.zxdmy.excite.payment.api.WechatPayApiService;
import com.zxdmy.excite.payment.model.PaymentNotifyModel;
import com.zxdmy.excite.payment.vo.PaymentCreateReturnVo;
import com.zxdmy.excite.payment.vo.PaymentCreateRequestVo;
import com.zxdmy.excite.ums.entity.UmsApp;
import com.zxdmy.excite.ums.entity.UmsOrder;
import com.zxdmy.excite.ums.service.IUmsAppService;
import com.zxdmy.excite.ums.service.IUmsOrderService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * <p>
 * 支付开放接口
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/3/30 11:26
 */
@Controller
@AllArgsConstructor
@RequestMapping("/api/payment")
public class ApiPaymentController extends BaseController {

    private AlipayApiService alipayApiService;

    private WechatPayApiService wechatPayApiService;

    private IUmsAppService appService;

    private IUmsOrderService orderService;

    /**
     * 发起支付请求
     *
     * @param paymentCreateVo 请求参数
     * @return 支付结果
     */
    @RequestMapping(value = "/pay", method = {RequestMethod.POST})
    @ResponseBody
    public BaseResult pay(@RequestBody @Validated PaymentCreateRequestVo paymentCreateVo) {
        // 获取应用信息
        UmsApp app = appService.getOne(new QueryWrapper<UmsApp>().eq(UmsApp.APP_ID, paymentCreateVo.getAppid()));
        if (null == app) {
            // 无效的AppID参数
            return error(ReturnCode.INVALID_APP_ID.getCode(), ReturnCode.INVALID_APP_ID.getReason());
        }
        // 验签通过，则核验信息，进行支付
        if (SignUtils.checkSignMD5(paymentCreateVo.getTreeMap(), app.getAppSecret(), paymentCreateVo.getHash())) {
            // 生成订单号
            String outTradeNo = OrderUtils.createOrderCode();
            // 三项返回参数
            String qrcode = null;
            String page = null;
            String wechat = null;
            // 微信支付
            if (PaymentCreateReturnVo.WECHAT.equalsIgnoreCase(paymentCreateVo.getPayChannel())) {
                // 订单号加前缀
                outTradeNo = PaymentConsts.Order.ORDER_CODE_WECHAT + outTradeNo;
                // 修改金额格式（微信支付的单位是分）
                BigDecimal decimal = new BigDecimal(paymentCreateVo.getAmount()).multiply(new BigDecimal("100"));
                Integer amount = decimal.intValue();
                // 电脑扫码支付、二维码支付
                if (PaymentCreateReturnVo.QRCODE.equalsIgnoreCase(paymentCreateVo.getPayScene())) {
                    qrcode = wechatPayApiService.pay(
                            paymentCreateVo.getPayScene(),
                            paymentCreateVo.getTitle(),
                            outTradeNo, amount, ""
                    );
                }
                // 微信内支付
                else {
                    wechat = "";
                    // TODO 微信支付jsapi支付
                }
            }
            // 支付宝支付（已经做了二选一校验，无需多次校验）
            else {
                // 订单号加前缀
                outTradeNo = PaymentConsts.Order.ORDER_CODE_ALIPAY + outTradeNo;
                // 二维码支付
                if (PaymentCreateReturnVo.QRCODE.equalsIgnoreCase(paymentCreateVo.getPayScene())) {
                    qrcode = alipayApiService.pay(
                            paymentCreateVo.getPayScene(),
                            paymentCreateVo.getTitle(),
                            outTradeNo,
                            paymentCreateVo.getAmount(),
                            paymentCreateVo.getReturnUrl(),
                            paymentCreateVo.getCancelUrl()
                    );
                }
                // 网址支付
                else {
                    page = "";
                    // TODO 支付宝电脑支付
                }
            }
            // 如果所有的支付字段均为空，说明订单未生成
            if (StringUtils.isAllEmpty(qrcode, page, wechat)) {
                return error(9000, "发起支付请求错误，请重试！");
            }

            // 生成订单实体，并保存至数据库
            UmsOrder order = new UmsOrder();
            order.setAppId(app.getAppId())
                    .setOutTradeNo(outTradeNo)
                    .setTitle(paymentCreateVo.getTitle())
                    .setAmount(paymentCreateVo.getAmount())
                    .setStatus(PaymentConsts.Status.WAIT)
                    .setPayChannel(paymentCreateVo.getPayChannel())
                    .setPayScene(paymentCreateVo.getPayScene())
                    .setNotifyUrl(paymentCreateVo.getNotifyUrl())
                    .setReturnUrl(paymentCreateVo.getReturnUrl())
                    .setCancelUrl(paymentCreateVo.getCancelUrl())
                    .setAttach(paymentCreateVo.getAttach());
            orderService.createOrder(order);

            // 生成签名
            String time = String.valueOf((int) (System.currentTimeMillis() / 1000));
            // 随机值
            String nonce = RandomUtil.randomString(16);
            // 返回的实体
            PaymentCreateReturnVo returnVo = new PaymentCreateReturnVo();
            // 填充返回的参数
            returnVo.setAppid(app.getAppId()).setTime(time).setNonce(nonce);
            returnVo.setTitle(paymentCreateVo.getTitle()).setAmount(paymentCreateVo.getAmount()).setOutTradeNo(outTradeNo)
                    .setQrcode(qrcode).setPage(page).setWechat(wechat);
            // 生成签名
            returnVo.setHash(SignUtils.sign(returnVo.getTreeMap(), app.getAppSecret()));
            // 返回结果
            return success(returnVo);
        } else {
            // 签名错误
            return error(SignUtils.sign(paymentCreateVo.getTreeMap(), app.getAppSecret()));
//            return error(ReturnCode.INVALID_SIGNATURE.getCode(), ReturnCode.INVALID_SIGNATURE.getReason());
        }
    }

    /**
     * 对外：查询订单接口
     *
     * @return 支付结果
     */
    @RequestMapping(value = "/query", method = {RequestMethod.POST})
    @ResponseBody
    public BaseResult query() {
        // TODO 查询微信 → 查询支付宝 → 返回查询结果

        // TODO 异步：根据查询结果，更新数据库里的信息

        return error("666");
    }

    /**
     * 退款接口
     *
     * @return 支付结果
     */
    @RequestMapping(value = "/refund", method = {RequestMethod.POST})
    @ResponseBody
    public BaseResult refund() {
        return error("666");
    }

    /**
     * 微信支付的官方回调地址
     *
     * @param notifyData 收到的Body JSON数据
     * @param timeStamp  Header里的时间戳
     * @param nonce      Header里的随机数
     * @param signature  Header里的签名
     * @param serial     Header里的序列值
     * @return 状态码200
     */
    @PostMapping("/notify/wechat")
    @ResponseBody
    public String wechatPayNotify(@RequestBody String notifyData,
                                  @RequestHeader("Wechatpay-Timestamp") String timeStamp,
                                  @RequestHeader("Wechatpay-Nonce") String nonce,
                                  @RequestHeader("Wechatpay-Signature") String signature,
                                  @RequestHeader("Wechatpay-Serial") String serial
    ) {
        PaymentNotifyModel notifyModel = wechatPayApiService.verifyNotify(notifyData, timeStamp, nonce, signature, serial);
        // 验签成功
        if (null != notifyModel) {
            // 更新系统订单
            this.saveNotify2Order(notifyModel);
            // TODO 向下游发送POST推送


            return WxPayNotifyResponse.success("成功");
        }
        // 验签失败
        return WxPayNotifyResponse.fail("签名验证失败，请查询系统日志！");
    }

    @PostMapping("/notify/alipay")
    @ResponseBody
    public String alipayNotify() {
        // 将请求数据交给接口验签并返回结果
        PaymentNotifyModel notifyModel = alipayApiService.verifyNotify(request.getParameterMap());
        // 验签成功
        if (null != notifyModel) {
            // 更新系统订单
            this.saveNotify2Order(notifyModel);
            // TODO 向下游发送POST推送


            return "success";
        }
        return "error";
    }

    /**
     * 根据回调信息更新系统的订单数据
     *
     * @param notifyModel 回调信息
     */
    private void saveNotify2Order(PaymentNotifyModel notifyModel) {
        UmsOrder order = new UmsOrder();
        order.setOutTradeNo(notifyModel.getOutTradeNo())
                .setTradeNo(notifyModel.getTradeNo())
                .setPaidTime(LocalDateTimeUtil.parse(notifyModel.getPaidTime()))
                .setUserId(notifyModel.getUserId())
                .setStatus(notifyModel.getStatus());
        orderService.updateOrderByNotifyReceive(order);
    }
}
