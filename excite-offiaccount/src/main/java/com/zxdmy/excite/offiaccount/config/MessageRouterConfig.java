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

    private final EventSubscribeHandler subscribeHandler;

    private final EventSubscribeCancelHandler subscribeCancelHandler;

    private final EventScanHandler scanHandler;

    private final EventMenuHandler menuHandler;

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

        // 关注事件
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event(WxConsts.EventType.SUBSCRIBE).handler(this.subscribeHandler).end();

        // 取消关注事件
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event(WxConsts.EventType.UNSUBSCRIBE).handler(this.subscribeCancelHandler).end();

        // 已关注后扫描带参数二维码
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event(WxConsts.EventType.SCAN).handler(this.scanHandler).end();

        // 自定义菜单事件
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event(WxConsts.EventType.CLICK).handler(this.menuHandler).end();

        // 点击菜单链接事件
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event(WxConsts.EventType.VIEW).handler(this.menuHandler).end();

        // 默认
        router.rule().async(false).handler(this.defaultHandler).end();

        return router;
    }
}
