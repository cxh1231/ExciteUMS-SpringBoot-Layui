package com.zxdmy.excite.offiaccount.handler;

import com.zxdmy.excite.common.consts.OffiaccountConsts;
import com.zxdmy.excite.offiaccount.builder.MsgBuilder;
import com.zxdmy.excite.offiaccount.service.IOffiaccountCommonService;
import com.zxdmy.excite.ums.entity.UmsMpReply;
import com.zxdmy.excite.ums.service.IUmsMpReplyService;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class EventMenuHandler extends AbstractHandler {

    private IUmsMpReplyService mpReplyService;

    private IOffiaccountCommonService commonService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        // 消息
        UmsMpReply mpReply = null;
        // 事件类型：CLICK（即点击菜单拉取消息时的事件推送，需要给用户返回消息）
        if (wxMessage.getEvent().equals(WxConsts.EventType.CLICK)) {
            // 根据菜单的KEY获取消息
            mpReply = mpReplyService.getReplyByType(OffiaccountConsts.ReplyType.MENU_CLICK_REPLY, wxMessage.getEventKey());
        }
        // 唤起扫码
//        else if (wxMessage.getEvent().equals(WxConsts.EventType.SCAN)) {
//
//        }
        // 点击网页
//        else if (wxMessage.getEvent().equals(WxConsts.EventType.VIEW)) {
//
//        }
        // 构造消息
        WxMpXmlOutMessage outMessage = new MsgBuilder().build(mpReply, wxMessage, wxMpService);
        // 消息异步持久化
        commonService.saveEvent2DB(wxMessage, mpReply, outMessage);
        // 将消息返回给用户
        return outMessage;
    }
}
