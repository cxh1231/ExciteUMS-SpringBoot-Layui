package com.zxdmy.excite.offiaccount.handler;

import com.zxdmy.excite.common.consts.OffiaccountConsts;
import com.zxdmy.excite.offiaccount.builder.MsgBuilder;
import com.zxdmy.excite.offiaccount.service.IOffiaccountCommonService;
import com.zxdmy.excite.ums.entity.UmsMpReply;
import com.zxdmy.excite.ums.service.IUmsMpReplyService;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 关注用户事件
 *
 * @author 拾年之璐
 * @since 2022/3/31 19:58
 */
@Component
@AllArgsConstructor
public class EventSubscribeHandler extends AbstractHandler {

    private IUmsMpReplyService mpReplyService;

    private IOffiaccountCommonService commonService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        // 用户扫码关注后：
        UmsMpReply mpReply;
        // 普通关注用户：没有场景值，或场景值为空
        if (wxMessage.getEventKey() == null || wxMessage.getEventKey().equals("")) {
            // 异步调用：普通关注用户信息入库
            commonService.saveUserBySubscribe2DB(wxMessage.getFromUser(), wxMessage.getCreateTime());
            // 获取关注后返回的内容
            mpReply = mpReplyService.getOneReplyByType(OffiaccountConsts.ReplyType.SUBSCRIBE_REPLY, null);
        }
        // 通过带参数的二维码关注：场景值不为空，需要根据场景值进行二次处理（比如登录）
        else {
            // 异步调用：登录的用户信息入库
            commonService.saveUserBySubscribeLogin(wxMessage.getEventKey(), wxMessage.getFromUser(), wxMessage.getCreateTime());
            // 获取登录成功后返回的内容
            mpReply = mpReplyService.getOneReplyByType(OffiaccountConsts.ReplyType.SCAN_LOGIN_REPLY, null);
        }
        // 构造返回的消息
        WxMpXmlOutMessage outMessage = new MsgBuilder().build(mpReply, wxMessage, wxMpService);

        // 异步调用：事件消息入库
        commonService.saveEvent2DB(wxMessage, mpReply, outMessage);


        return outMessage;
    }

}
