package com.zxdmy.excite.offiaccount.config;

import lombok.AllArgsConstructor;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 微信服务配置
 *
 * @author 拾年之璐
 * @since 2022/6/28 16:44
 */
@Configuration
@AllArgsConstructor
public class OffiaccountConfig {
    /**
     * 注入配置，项目启动时，执行
     *
     * @return WxJava的配置类
     */
    @Bean
    public WxMpService wxMpService() {
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setMaxRetryTimes(3);
        return wxMpService;
    }
}
