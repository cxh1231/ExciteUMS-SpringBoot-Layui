package com.zxdmy.excite.offiaccount.config;

import com.zxdmy.excite.offiaccount.handler.*;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 消息/事件 路由配置
 *
 * @author 拾年之璐
 * @since 2022/6/28 16:44
 */
@Configuration
@AllArgsConstructor
public class MessageRouterConfig {

    private final LogHandler logHandler;

    private final MsgTextHandler textHandler;

    private final MenuHandler menuHandler;

    private final SubscribeHandler subscribeHandler;

    private final SubscribeCancelHandler subscribeCancelHandler;

    private final DefaultHandler defaultHandler;

    /**
     * 根据消息类型，选择路由进行处理
     *
     * @param wxMpService 微信公众号API的Service.
     * @return 路由结果
     */
    @Bean
    public WxMpMessageRouter messageRouter(WxMpService wxMpService) {

        final WxMpMessageRouter router = new WxMpMessageRouter(wxMpService);

        // 记录所有事件的日志 （异步执行）
        router.rule().handler(this.logHandler).next();

        // 接收文本消息事件
        router.rule().async(false).msgType(WxConsts.XmlMsgType.TEXT).handler(this.textHandler).end();

        // 自定义菜单事件
        // 详情：https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_event_pushes.html#自定义菜单事件
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event(WxConsts.EventType.CLICK).handler(this.menuHandler).end();

        // 点击菜单链接事件
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event(WxConsts.EventType.VIEW).handler(this.menuHandler).end();

        // 关注事件
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event(WxConsts.EventType.SUBSCRIBE).handler(this.subscribeHandler).end();

        // 取消关注事件
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event(WxConsts.EventType.UNSUBSCRIBE).handler(this.subscribeCancelHandler).end();

        // 默认
        router.rule().async(false).handler(this.defaultHandler).end();

        // 上报地理位置事件
//        router.rule().async(false).msgType(EVENT).event(EventType.LOCATION).handler(this.locationHandler).end();

        // 接收地理位置消息
//        newRouter.rule().async(false).msgType(XmlMsgType.LOCATION).handler(this.locationHandler).end();

        // 扫码事件
//        newRouter.rule().async(false).msgType(EVENT).event(EventType.SCAN).handler(this.scanHandler).end();
        // 接收客服会话管理事件
//        newRouter.rule().async(false).msgType(EVENT).event(KF_CREATE_SESSION)
//                .handler(this.kfSessionHandler).end();
//        newRouter.rule().async(false).msgType(EVENT).event(KF_CLOSE_SESSION)
//                .handler(this.kfSessionHandler).end();
//        newRouter.rule().async(false).msgType(EVENT).event(KF_SWITCH_SESSION)
//                .handler(this.kfSessionHandler).end();

        // 门店审核事件
//        newRouter.rule().async(false).msgType(EVENT).event(POI_CHECK_NOTIFY).handler(this.storeCheckNotifyHandler).end();

        return router;
    }
}
