package com.zxdmy.excite.admin.controller.api;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.zxdmy.excite.common.base.BaseController;
import com.zxdmy.excite.common.base.BaseResult;
import com.zxdmy.excite.common.consts.PaymentConsts;
import com.zxdmy.excite.common.enums.PaymentEnums;
import com.zxdmy.excite.common.enums.SystemCode;
import com.zxdmy.excite.common.utils.OrderUtils;
import com.zxdmy.excite.common.utils.SignUtils;
import com.zxdmy.excite.offiaccount.api.OffiaccountApiService;
import com.zxdmy.excite.payment.api.AlipayApiService;
import com.zxdmy.excite.payment.api.WechatPayApiService;
import com.zxdmy.excite.payment.model.PaymentNotifyModel;
import com.zxdmy.excite.payment.vo.PaymentCreateRequestVo;
import com.zxdmy.excite.payment.vo.PaymentCreateResponseVo;
import com.zxdmy.excite.payment.vo.PaymentQueryResponseVo;
import com.zxdmy.excite.ums.entity.UmsApp;
import com.zxdmy.excite.ums.entity.UmsOrder;
import com.zxdmy.excite.ums.service.IUmsAppService;
import com.zxdmy.excite.ums.service.IUmsOrderService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.TreeMap;

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
        UmsApp app = appService.getByAppId(paymentCreateVo.getAppid());
        if (null == app) {
            responseVo.setCode(PaymentEnums.ERROR_APP_ID.getCode())
                    .setMsg(PaymentEnums.ERROR_APP_ID.getMessage());
            return responseVo;
        }
        // 如果该应用已被禁用，则直接返回错误信息
//        if (app.getStatus() == 0) {
//            responseVo.setCode(PaymentEnums.ERROR_APP_STATUS.getCode())
//                    .setMsg(PaymentEnums.ERROR_APP_STATUS.getMessage());
//            return responseVo;
//        }
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
            outTradeNo = PaymentConsts.Order.OUT_TRADE_NO_PREFIX_WECHAT + outTradeNo;
            // 支付场景：wechat，需要先登录才能发起支付，统一使用支付页面
            if (PaymentConsts.Scene.WECHAT.equalsIgnoreCase(paymentCreateVo.getPayScene())) {
                // 生成支付页面地址
                String payUrl = offiaccountApiService.getAuthorizationUrlBase("http://dev.open.zxdmy.com/api/payment/wechat/jsapi", outTradeNo);
                // 设置返回实体
                responseVo.setTitle(paymentCreateVo.getTitle())
                        .setOutTradeNo(outTradeNo)
                        .setAmount(paymentCreateVo.getAmount())
                        .setUrl(payUrl);
            }
            // 其他场景：直接发起支付
            else {
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
            outTradeNo = PaymentConsts.Order.OUT_TRADE_NO_PREFIX_ALIPAY + outTradeNo;
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

    /**
     * JSAPI支付页面
     *
     * @param code  微信授权码
     * @param state 商户单号
     * @param map   参数集合
     * @return 返回支付页面或错误页面
     */
    @RequestMapping(value = "/wechat/jsapi", method = {RequestMethod.GET})
    public String wechatPayPage(String code, String state, ModelMap map) {
        // 根据微信授权码获取用户OpenID
        String openId;
        try {
            openId = offiaccountApiService.getUserBaseByAuthCode(code).getUserid();
        }
        // 如果获取不到用户OpenID，捕获错误信息并显示
        catch (Exception e) {
            map.put("error", "授权失败：" + e.getMessage());
            return "/api/pay/error";
        }
        System.out.println(openId);

        // 根据商户单号获取订单信息
        UmsOrder order = orderService.getOrderByOutTradeNo(state);
        // 如果获取不到订单信息，则跳转到错误页面
        if (null == order) {
            map.put("error", "获取订单失败：订单不存在");
            return "/api/pay/error";
        }
        // 当前订单已支付
        if (Objects.equals(order.getStatus(), PaymentConsts.Status.SUCCESS)) {
            map.put("error", "当前订单已支付，请勿重复支付");
            return "/api/pay/error";
        }
        // 如果订单状态不是待支付，则跳转到错误页面
        if (!order.getStatus().equals(PaymentConsts.Status.WAIT)) {
            map.put("error", "订单状态错误，当前订单状态：" + order.getStatus());
            return "/api/pay/error";
        }
        // 生成订单
        PaymentCreateResponseVo responseVo = wechatPayApiService.pay(
                PaymentConsts.Scene.WECHAT,
                order.getTitle(),
                order.getOutTradeNo(),
                order.getAmount(),
                // TODO 这里替换为用户的OpenID
                "oEOXk5h4BEbm63QeK4m01aBOigXk"
        );
        // 如果生成订单失败，则跳转到错误页面
        if (!responseVo.getCode().equals(PaymentEnums.SUCCESS.getCode())) {
            map.put("error", "创建订单失败：" + responseVo.getMsg());
            return "/api/pay/error";
        }
        // 订单信息参数
        map.put("amount", responseVo.getAmount());
        map.put("title", responseVo.getTitle());
        map.put("outTradeNo", responseVo.getOutTradeNo());
        // 生成加密签名
        String appid = order.getAppId();
        String nonce = RandomUtil.randomString(16);
        String time = String.valueOf((int) (System.currentTimeMillis() / 1000));
        String appSecret = appService.getByAppId(order.getAppId()).getAppSecret();
        TreeMap<String, Object> treeMap = new TreeMap<>();
        treeMap.put("appid", appid);
        treeMap.put("nonce", nonce);
        treeMap.put("time", time);
        treeMap.put("out_trade_no", responseVo.getOutTradeNo());
        String hash = SignUtils.sign(treeMap, appSecret);
        // 构建回调链接参数
        String params = "?appid=" + appid + "&nonce=" + nonce + "&out_trade_no=" + responseVo.getOutTradeNo() + "&time=" + time + "&hash=" + hash;
        // 支付成败跳转的页面，这里需要给回调地址加上商户单号，以便后续查询订单状态
        // /callback_url?appid=202207284800&nonce=5456127336&out_trade_no=WX1234556&time=1657542633&hash=3bc7e059cdd4047fe00219b198d15127
        map.put("returnUrl", order.getReturnUrl() + params);
        map.put("cancelUrl", order.getCancelUrl() + params);

        // JSAPI唤起支付页面的参数
        map.put("appId", responseVo.getAppid());
        map.put("timeStamp", responseVo.getTime());
        map.put("nonceStr", responseVo.getNonce());
        map.put("package", responseVo.getPrepayId());
        map.put("signType", "RSA");
        map.put("paySign", responseVo.getHash());

        // 返回结果
        return "/api/pay/jsapi";
    }

    /**
     * 对外：查询订单接口
     *
     * @param appId      应用ID
     * @param tradeNo    交易单号
     * @param outTradeNo 商户单号
     * @param time       时间戳
     * @param nonce      随机值
     * @param hash       签名
     * @return 支付结果
     */
    @RequestMapping(value = "/query", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object query(
            @RequestParam(name = "appid", required = false) String appId,
            @RequestParam(name = "trade_no", required = false) String tradeNo,
            @RequestParam(name = "out_trade_no", required = false) String outTradeNo,
            @RequestParam(name = "time", required = false) String time,
            @RequestParam(name = "nonce", required = false) String nonce,
            @RequestParam(name = "hash", required = false) String hash
    ) {
        PaymentQueryResponseVo responseVo = new PaymentQueryResponseVo();
        // 必填参数不能为空
        if (StrUtil.hasBlank(appId, time, nonce, hash)) {
            responseVo.setCode(PaymentEnums.ERROR_PARAM.getCode())
                    .setMsg(PaymentEnums.ERROR_PARAM.getMessage() + "：必填参数不能为空");
            return responseVo;
        }
        // 商户单号和交易单号不能同时为空
        if (StrUtil.isAllBlank(outTradeNo, tradeNo)) {
            responseVo.setCode(PaymentEnums.ERROR_PARAM.getCode())
                    .setMsg(PaymentEnums.ERROR_PARAM.getMessage() + "：商户单号和交易单号不能同时为空");
            return responseVo;
        }
        // 获取该应用的秘钥
        UmsApp app = appService.getByAppId(appId);
        if (null == app || app.getStatus() == null || app.getStatus() == SystemCode.STATUS_N.getCode()) {
            responseVo.setCode(PaymentEnums.ERROR_APP_ID.getCode())
                    .setMsg(PaymentEnums.ERROR_APP_ID.getMessage());
            return responseVo;
        }
        // 校验签名
        TreeMap<String, Object> treeMap = new TreeMap<>();
        treeMap.put("appid", appId);
        treeMap.put("nonce", nonce);
        treeMap.put("time", time);
        treeMap.put("out_trade_no", outTradeNo);
        treeMap.put("trade_no", tradeNo);
        // 验证签名是否正确
        String sign = SignUtils.sign(treeMap, app.getAppSecret());
        if (!sign.equals(hash)) {
            responseVo.setCode(PaymentEnums.ERROR_SIGN.getCode())
                    .setMsg(PaymentEnums.ERROR_SIGN.getMessage());
            return responseVo;
        }

        // 微信支付渠道：商户单号不为空并且以WX开头，或交易单号不为空并且以42开头
        if (StrUtil.isNotBlank(outTradeNo) && (outTradeNo.startsWith(PaymentConsts.Order.OUT_TRADE_NO_PREFIX_WECHAT)
                || (StrUtil.isNotBlank(tradeNo) && tradeNo.startsWith(PaymentConsts.Order.TRADE_NO_PREFIX_WECHAT)))) {
            responseVo = wechatPayApiService.query(outTradeNo, tradeNo);
        }
        // 支付宝支付渠道：商户单号不为空并且以AL开头，或交易单号不为空并且以20开头
        else if (StrUtil.isNotBlank(outTradeNo) && (outTradeNo.startsWith(PaymentConsts.Order.OUT_TRADE_NO_PREFIX_ALIPAY)
                || (StrUtil.isNotBlank(tradeNo) && tradeNo.startsWith(PaymentConsts.Order.TRADE_NO_PREFIX_ALIPAY)))) {
            responseVo = alipayApiService.query(outTradeNo, tradeNo);
        }
        // 其他情况：错误
        else {
            responseVo.setCode(PaymentEnums.ERROR_PARAM.getCode())
                    .setMsg(PaymentEnums.ERROR_PARAM.getMessage() + "：商户单号或交易单号格式要求");
            return responseVo;
        }
        // 异步请求，更新本系统订单状态

        // 构建返回结果
        hash = SignUtils.sign(responseVo.getTreeMap(), app.getAppSecret());

        responseVo.setHash(hash);
        return responseVo;
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
            // 返回给微信支付的结果
            return WxPayNotifyResponse.success("成功");
        }
        // 验签失败
        return WxPayNotifyResponse.fail("签名验证失败，请查询系统日志！");
    }

    /**
     * 支付宝支付的官方回调地址
     *
     * @return 验签成功为success，验签失败为 error
     */
    @PostMapping("/notify/alipay")
    @ResponseBody
    public String alipayNotify() {
        // 将请求数据交给接口验签并返回结果
        PaymentNotifyModel notifyModel = alipayApiService.verifyNotify(request.getParameterMap());
        // 验签成功
        if (null != notifyModel) {
            // 更新系统订单并向下游发送POST推送
            this.saveNotify2Order(notifyModel);
            return "success";
        }
        return "error";
    }

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        this.notifyTest();
        return "success";
    }


    @Async
    public void notifyTest() {
        // 延时3秒
        try {
            System.out.println("延时3秒开始");
            Thread.sleep(10000);
            System.out.println("延时3秒结束");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 异步请求：根据回调信息更新系统的订单数据
     *
     * @param notifyModel 回调信息
     */
    private void saveNotify2Order(PaymentNotifyModel notifyModel) {
        // 构造订单信息
        UmsOrder order = new UmsOrder();
        order.setOutTradeNo(notifyModel.getOutTradeNo())
                .setTradeNo(notifyModel.getTradeNo())
                .setPaidTime(LocalDateTimeUtil.parse(notifyModel.getPaidTime()))
                .setUserId(notifyModel.getUserId())
                .setStatus(notifyModel.getStatus());
        // 异步：更新订单信息
        orderService.updateOrderByNotify(order);
    }
}
