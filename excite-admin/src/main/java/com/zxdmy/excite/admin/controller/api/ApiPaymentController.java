package com.zxdmy.excite.admin.controller.api;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.zxdmy.excite.common.base.BaseController;
import com.zxdmy.excite.common.base.BaseResult;
import com.zxdmy.excite.common.consts.PaymentConsts;
import com.zxdmy.excite.common.enums.PaymentEnums;
import com.zxdmy.excite.common.utils.OrderUtils;
import com.zxdmy.excite.common.utils.SignUtils;
import com.zxdmy.excite.offiaccount.api.OffiaccountApiService;
import com.zxdmy.excite.payment.api.AlipayApiService;
import com.zxdmy.excite.payment.api.WechatPayApiService;
import com.zxdmy.excite.payment.model.PaymentNotifyModel;
import com.zxdmy.excite.payment.vo.PaymentCreateRequestVo;
import com.zxdmy.excite.payment.vo.PaymentCreateResponseVo;
import com.zxdmy.excite.ums.entity.UmsApp;
import com.zxdmy.excite.ums.entity.UmsOrder;
import com.zxdmy.excite.ums.service.IUmsAppService;
import com.zxdmy.excite.ums.service.IUmsOrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

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

    private OffiaccountApiService offiaccountApiService;

    private AlipayApiService alipayApiService;

    private WechatPayApiService wechatPayApiService;

    private IUmsAppService appService;

    private IUmsOrderService orderService;

    /**
     * 发起支付请求
     *
     * @param paymentCreateVo 请求参数
     * @param bindingResult   @Validated的校验结果
     * @return 支付结果
     */
    @RequestMapping(value = "/pay", method = {RequestMethod.POST})
    @ResponseBody
    public Object pay(@RequestBody @Validated PaymentCreateRequestVo paymentCreateVo, BindingResult bindingResult) {
        // 返回的实体
        PaymentCreateResponseVo responseVo = new PaymentCreateResponseVo();
        // 参数格式校验失败：直接返回错误信息
        if (bindingResult.hasErrors()) {
            responseVo.setCode(PaymentEnums.ERROR_PARAM.getCode())
                    .setMsg(PaymentEnums.ERROR_PARAM.getMessage() + "：" + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            return responseVo;
        }
        // 应用信息获取失败：直接返回错误信息
        UmsApp app = appService.getOne(new QueryWrapper<UmsApp>().eq(UmsApp.APP_ID, paymentCreateVo.getAppid()));
        if (null == app) {
            responseVo.setCode(PaymentEnums.ERROR_APP_ID.getCode())
                    .setMsg(PaymentEnums.ERROR_APP_ID.getMessage());
            return responseVo;
        }
        // 签名验证失败：直接返回错误信息
        if (!SignUtils.checkSignMD5(paymentCreateVo.getTreeMap(), app.getAppSecret(), paymentCreateVo.getHash())) {
            responseVo.setCode(PaymentEnums.ERROR_SIGN.getCode())
                    .setMsg(PaymentEnums.ERROR_SIGN.getMessage() + "：" + SignUtils.sign(paymentCreateVo.getTreeMap(), app.getAppSecret()));
            return responseVo;
        }
        // 请求信息正确，执行支付操作

        // 生成订单号
        String outTradeNo = OrderUtils.createOrderCode();
        // 微信支付
        if (PaymentConsts.Channel.WECHAT.equalsIgnoreCase(paymentCreateVo.getPayChannel())) {
            // 订单号加前缀
            outTradeNo = PaymentConsts.Order.ORDER_CODE_WECHAT + outTradeNo;
            // 支付场景：wechat，需要先登录才能发起支付，统一使用支付页面
            if (PaymentConsts.Scene.WECHAT.equalsIgnoreCase(paymentCreateVo.getPayScene())) {
                // 生成支付页面地址
                String payUrl = offiaccountApiService.getAuthorizationUrlBase("http://dev.open.zxdmy.com/api/payment/wechat", outTradeNo);
                // 设置返回实体
                responseVo.setTitle(paymentCreateVo.getTitle())
                        .setOutTradeNo(outTradeNo)
                        .setAmount(paymentCreateVo.getAmount())
                        .setUrl(payUrl);
            }
            // 其他场景：直接发起支付
            else {
                // 微信支付公众号支付
                responseVo = wechatPayApiService.pay(
                        paymentCreateVo.getPayScene(),
                        paymentCreateVo.getTitle(),
                        outTradeNo,
                        paymentCreateVo.getAmount(),
                        null
                );
            }
        }
        // 支付宝支付（已经做了二选一校验，无需多次校验）
        else {
            // 订单号加前缀
            outTradeNo = PaymentConsts.Order.ORDER_CODE_ALIPAY + outTradeNo;
            // 发起支付并获取返回结果
            responseVo = alipayApiService.pay(
                    paymentCreateVo.getPayScene(),
                    paymentCreateVo.getTitle(),
                    outTradeNo,
                    paymentCreateVo.getAmount(),
                    paymentCreateVo.getReturnUrl(),
                    paymentCreateVo.getCancelUrl()
            );
        }

        // 如果生成订单成功，则将订单信息保存至数据库
        if (Objects.equals(responseVo.getCode(), PaymentEnums.SUCCESS.getCode())) {
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
        }
        // 携带的参数原样返回
        responseVo.setAttach(paymentCreateVo.getAttach());
        // 填充返回的参数
        responseVo.setAppid(app.getAppId());
        // 生成签名
        responseVo.setHash(SignUtils.sign(responseVo.getTreeMap(), app.getAppSecret()));
        // 返回结果
        return responseVo;
    }


    // 微信内支付
    @RequestMapping(value = "/wechat", method = {RequestMethod.GET})
    public String wechatPay(String code, String state, ModelMap map) {
        String openId = offiaccountApiService.getUserBaseByAuthCode(code).getUserid();
        if (openId == null) {
            return "授权失败";
        }
        UmsOrder order = orderService.getOne(new QueryWrapper<UmsOrder>().eq(UmsOrder.OUT_TRADE_NO, state));

        // 下单
        PaymentCreateResponseVo responseVo = wechatPayApiService.pay(
                PaymentConsts.Scene.WECHAT,
                order.getTitle(),
                order.getOutTradeNo(),
                order.getAmount(),
                "oEOXk5h4BEbm63QeK4m01aBOigXk"
        );
        String appId = "wx7cd3849a05d3db6e";
        String nonceStr = "1234567890";
        String timeStamp = String.valueOf((int) (System.currentTimeMillis() / 1000));
        String packageStr = "prepay_id=" + responseVo.getPrepayId();

        String signStr = appId + "\n" + timeStamp + "\n" + nonceStr + "\n" + packageStr;

        map.put("appId", appId);
        map.put("timeStamp", timeStamp);
        map.put("nonceStr", nonceStr);
        map.put("package", packageStr);
        map.put("signType", "RSA");

        map.put("paySign", MD5.create().digestHex(signStr));

        // 返回结果
        return "/api/pay/pay";
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
