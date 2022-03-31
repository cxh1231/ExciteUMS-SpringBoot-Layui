package com.zxdmy.excite.offiaccount.config;

import com.zxdmy.excite.offiaccount.handler.*;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 微信公众号服务类配置
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/3/31 18:44
 */
@Configuration
@AllArgsConstructor
public class WechatMpConfig {

    private final LogHandler logHandler;

    private final MsgHandler msgHandler;

    private final MenuHandler menuHandler;

    private final SubscribeHandler subscribeHandler;
    private final UnsubscribeHandler unsubscribeHandler;


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


    @Bean
    public WxMpMessageRouter messageRouter(WxMpService wxMpService) {

        final WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);

        // 记录所有事件的日志 （异步执行）
        newRouter.rule().handler(this.logHandler).next();

        // 接收客服会话管理事件
//        newRouter.rule().async(false).msgType(EVENT).event(KF_CREATE_SESSION)
//                .handler(this.kfSessionHandler).end();
//        newRouter.rule().async(false).msgType(EVENT).event(KF_CLOSE_SESSION)
//                .handler(this.kfSessionHandler).end();
//        newRouter.rule().async(false).msgType(EVENT).event(KF_SWITCH_SESSION)
//                .handler(this.kfSessionHandler).end();

        // 门店审核事件
//        newRouter.rule().async(false).msgType(EVENT).event(POI_CHECK_NOTIFY).handler(this.storeCheckNotifyHandler).end();

        // 自定义菜单事件
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event(WxConsts.EventType.CLICK).handler(this.menuHandler).end();

        // 点击菜单连接事件
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event(WxConsts.EventType.VIEW).handler(this.menuHandler).end();

        // 关注事件
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event(WxConsts.EventType.SUBSCRIBE).handler(this.subscribeHandler).end();

        // 取消关注事件
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event(WxConsts.EventType.UNSUBSCRIBE).handler(this.unsubscribeHandler).end();

        // 上报地理位置事件
//        newRouter.rule().async(false).msgType(EVENT).event(EventType.LOCATION).handler(this.locationHandler).end();

        // 接收地理位置消息
//        newRouter.rule().async(false).msgType(XmlMsgType.LOCATION).handler(this.locationHandler).end();

        // 扫码事件
//        newRouter.rule().async(false).msgType(EVENT).event(EventType.SCAN).handler(this.scanHandler).end();

        // 默认
        newRouter.rule().async(false).handler(this.msgHandler).end();

        return newRouter;
    }
}
