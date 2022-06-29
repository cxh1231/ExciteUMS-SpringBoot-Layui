package com.zxdmy.excite.offiaccount.handler;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 自定义菜单点击事件
 *
 * @author 拾年之璐
 * @since 2022/3/31 19:40
 */
@Component
public class EventMenuHandler extends AbstractHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {

        // 事件类型：CLICK（即点击菜单拉取消息时的事件推送，需要给用户返回消息）
        if (WxConsts.EventType.CLICK.equals(wxMessage.getEvent())) {
//            String msg = String.format("type:%s, event:%s, key:%s",
//                    wxMessage.getMsgType(),
//                    wxMessage.getEvent(),
//                    wxMessage.getEventKey());

            // TODO 从数据库的回复列表中选择需要回复的内容

            // TODO 进行其他处理，将消息返回给用户

            return WxMpXmlOutMessage
                    .TEXT()
                    .content(wxMessage.getEventKey())
                    .fromUser(wxMessage.getToUser())
                    .toUser(wxMessage.getFromUser())
                    .build();
        }
        // 事件类型：VIEW（点击菜单跳转链接时的事件推送，即跳转网页）
        else if (WxConsts.EventType.VIEW.equals(wxMessage.getEvent())) {
            return null;
        }

        return null;
    }
}
