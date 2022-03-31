package com.zxdmy.excite.payment.config;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/3/31 20:14
 */
@Configuration
public class WechatPayConfig {

    @Bean
    public WxPayService payService() {
        return new WxPayServiceImpl();
    }
}
