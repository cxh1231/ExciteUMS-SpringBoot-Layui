package com.zxdmy.excite.admin.controller.api;

import com.zxdmy.excite.common.base.BaseController;
import com.zxdmy.excite.common.base.BaseResult;
import com.zxdmy.excite.common.utils.SignUtils;
import com.zxdmy.excite.payment.api.AlipayApiService;
import com.zxdmy.excite.payment.api.WechatPayApiService;
import com.zxdmy.excite.payment.entity.UmsOrder;
import com.zxdmy.excite.ums.service.IUmsAppService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
@RequestMapping("/api/pay")
public class ApiPaymentController extends BaseController {

    private AlipayApiService alipayApiService;

    private WechatPayApiService wechatPayApiService;

    private IUmsAppService appService;

    // 发起支付
    @RequestMapping(value = "/pay", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseResult pay(
            @RequestParam(name = UmsOrder.APP_ID) String appId,
            @RequestParam(name = UmsOrder.TITLE) String title,
            @RequestParam(name = UmsOrder.AMOUNT) String amount,
            @RequestParam(name = UmsOrder.NOTIFY_URL) String notifyUrl,
            @RequestParam(name = UmsOrder.RETURN_URL, required = false) String returnUrl,
            @RequestParam(name = UmsOrder.CANCEL_URL, required = false) String cancelUrl,
            @RequestParam(name = UmsOrder.PAY_CHANNEL) String payChannel,
            @RequestParam(name = UmsOrder.PAY_SCENE, required = false) String payScene,
            @RequestParam(name = UmsOrder.ATTACH, required = false) String attach,
            @RequestParam(name = UmsOrder.TIME) String time,
            @RequestParam(name = UmsOrder.NONCE) String nonce,
            @RequestParam(name = UmsOrder.HASH) String hash
    ) {
        // 获取请求的用户信息

        // 将请求参数交给验签方法
        TreeMap<String, Object> requestMap = new TreeMap<>();
        // 填充参数
        requestMap.putIfAbsent(UmsOrder.APP_ID, appId);
        requestMap.putIfAbsent(UmsOrder.TITLE, title);
        requestMap.putIfAbsent(UmsOrder.AMOUNT, amount);
        requestMap.putIfAbsent(UmsOrder.NOTIFY_URL, notifyUrl);
        requestMap.putIfAbsent(UmsOrder.RETURN_URL, returnUrl);
        requestMap.putIfAbsent(UmsOrder.CANCEL_URL, cancelUrl);
        requestMap.putIfAbsent(UmsOrder.PAY_CHANNEL, payChannel);
        requestMap.putIfAbsent(UmsOrder.PAY_SCENE, payScene);
        requestMap.putIfAbsent(UmsOrder.ATTACH, attach);
        requestMap.putIfAbsent(UmsOrder.TIME, time);
        requestMap.putIfAbsent(UmsOrder.NONCE, nonce);
        // 验签通过，则核验信息，进行支付
//        if (SignUtils.checkSignMD5(requestMap, "666", hash)) {
////            return success("签名验证成功！");
//        }
        // 获取支付信息

        // 组合返回结果，并生成签名

        // 返回结果

        return error(SignUtils.sign(requestMap, "666888"));
    }
    // 官方的回调

    // 查询接口

    // 退款接口
}
