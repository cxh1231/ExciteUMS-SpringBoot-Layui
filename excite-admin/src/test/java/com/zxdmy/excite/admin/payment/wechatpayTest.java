package com.zxdmy.excite.admin.payment;

import com.zxdmy.excite.payment.api.WechatPayApiService;
import com.zxdmy.excite.payment.bo.WechatPayBO;
import com.zxdmy.excite.payment.service.IAlipayConfigService;
import com.zxdmy.excite.payment.service.IWechatPayConfigService;
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
    IWechatPayConfigService paymentConfigService;

    @Test
    void saveTest() {
        WechatPayBO weChatPayBO = new WechatPayBO();
        weChatPayBO.setAppId("")
                .setMchId("")
                .setMchKey("")
                .setApiV3Key("")
                .setKeyPath("E:\\微信支付\\MyCert\\apiclient_cert.p12")
                .setPrivateKeyPath("E:\\微信支付\\MyCert\\apiclient_key.pem")
                .setPrivateCertPath("E:\\微信支付\\MyCert\\apiclient_cert.pem")
                .setNotifyUrl("");
        if (paymentConfigService.saveWechatPayConfig(weChatPayBO))
            System.out.println("成功！");

    }

    @Test
    void test() {
        System.out.println(wechatPayApiService.pay("wechat", "title,", "324235ewgfdfterw", 10, "rrrr"));
    }
}
