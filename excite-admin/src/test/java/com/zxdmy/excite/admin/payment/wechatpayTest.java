package com.zxdmy.excite.admin.payment;

import com.zxdmy.excite.payment.api.WechatPayApiService;
import com.zxdmy.excite.payment.bo.WechatPayBO;
import com.zxdmy.excite.payment.service.IPaymentConfigService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/3/31 20:25
 */
@SpringBootTest
public class wechatpayTest {

    @Autowired
    WechatPayApiService wechatPayApiService;

    @Autowired
    IPaymentConfigService paymentConfigService;

    @Test
    void saveTest() {
        WechatPayBO weChatPayBO = new WechatPayBO();
        weChatPayBO.setAppId("wx7cd3849a05d3db6e")
                .setMchId("1623473640")
                .setMchKey("cxh1231cxh1231cxh1231cxh1231cxh1")
                .setApiV3Key("cxh1231cxh1231cxh1231cxh1231cxh1")
                .setKeyPath("E:\\微信支付\\MyCert\\apiclient_cert.p12")
                .setPrivateKeyPath("E:\\微信支付\\MyCert\\apiclient_key.pem")
                .setPrivateCertPath("E:\\微信支付\\MyCert\\apiclient_cert.pem")
                .setNotifyUrl("http://dev.open.zxdmy.com/api/payment/notify");
        if (paymentConfigService.saveWechatPayConfig(weChatPayBO))
            System.out.println("成功！");

    }

    @Test
    void test() {
        System.out.println(wechatPayApiService.pay("wechat", "title,", "324235ewgfdfterw", 10, "rrrr"));
    }
}
