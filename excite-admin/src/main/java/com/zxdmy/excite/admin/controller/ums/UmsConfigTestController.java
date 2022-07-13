package com.zxdmy.excite.admin.controller.ums;

import cn.hutool.core.util.IdUtil;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayRefundV3Result;
import com.zxdmy.excite.common.base.BaseController;
import com.zxdmy.excite.common.base.BaseResult;
import com.zxdmy.excite.payment.api.AlipayApiService;
import com.zxdmy.excite.payment.api.WechatPayApiService;
import com.zxdmy.excite.payment.vo.PaymentCreateResponseVo;
import com.zxdmy.excite.payment.vo.PaymentQueryResponseVo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 配置测试类，实现各种测试方法，以验证配置是否生效
 *
 * @author 拾年之璐
 * @since 2022/5/26 20:23
 */
@Controller
@AllArgsConstructor
@RequestMapping("/ums/config/test")
public class UmsConfigTestController extends BaseController {

    private AlipayApiService alipayApiService;

    private WechatPayApiService wechatPayApiService;

    @PostMapping(value = "/alipay/qrcode")
    @ResponseBody
    public BaseResult alipayQrcode(String title, String price) {
        String outTradeNo = IdUtil.simpleUUID();
        PaymentCreateResponseVo responseVo = alipayApiService.pay("qrcode", title, outTradeNo, price, null, null);
        return success(200, "获取支付二维码成功！")
                .put("qrcode", responseVo.getQrcode())
                .put("outTradeNo", outTradeNo);
    }

    @PostMapping(value = "/alipay/query")
    @ResponseBody
    public BaseResult alipayQuery(String outTradeNo) {
        PaymentQueryResponseVo result = alipayApiService.query(null, outTradeNo);
        return success("查询支付结果成功！", result);
    }

    @PostMapping(value = "/alipay/refund")
    @ResponseBody
    public BaseResult alipayRefund(String outTradeNo, String amount) {
        String[] result = alipayApiService.refund(null, outTradeNo, amount, "用户取消退款");
        if ("Y".equals(result[0])) {
            return success(200, "退款成功，退款金额：" + result[4]);
        } else if ("N".equals(result[0])) {
            return success(200, "该订单早已退款成功！退款金额：" + result[4]);
        } else
            return error("退款错误：" + result[1] + result[2]);
    }


    @PostMapping(value = "/wechatPay/qrcode")
    @ResponseBody
    public BaseResult pay(String title, String price) {
        System.out.println(title);
        System.out.println(price);
        String outTradeNo = IdUtil.simpleUUID();
        PaymentCreateResponseVo result = wechatPayApiService.pay("qrcode", title, outTradeNo, price, null);

        return success(200, "获取支付二维码成功！")
                .put("qrcode", result.getQrcode())
                .put("outTradeNo", outTradeNo);
    }

    @PostMapping(value = "/wechatPay/query")
    @ResponseBody
    public BaseResult query(String outTradeNo) {
        PaymentQueryResponseVo responseVo = wechatPayApiService.query(null, outTradeNo);
        return success(responseVo.toString());
    }

//    @PostMapping(value = "/wechatPay/refund")
//    @ResponseBody
//    public BaseResult refund(String outTradeNo, Float amount) {
//        int refund = (int) (amount * 100);
//        // 查询该订单的实际支付金额
//        int total = wechatPayApiService.query(null, outTradeNo).getAmount().getTotal();
//        if (refund > total) {
//            return error("退款金额大于支付金额！");
//        }
//        WxPayRefundV3Result wxPayRefundV3Result = wechatPayApiService.refund(outTradeNo, IdUtil.simpleUUID(), total, refund);
//        if ("SUCCESS".equals(wxPayRefundV3Result.getStatus())) {
//            return success(200, "退款成功！");
//        } else if ("PROCESSING".equals(wxPayRefundV3Result.getStatus())) {
//            return success(200, "退款处理中...稍后到账，请注意查收微信通知！");
//        }
//        return error("退款失败！");
//    }

}
