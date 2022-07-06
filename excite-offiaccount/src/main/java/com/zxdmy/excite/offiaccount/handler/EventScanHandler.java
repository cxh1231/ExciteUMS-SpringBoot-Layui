package com.zxdmy.excite.offiaccount.handler;

import com.zxdmy.excite.common.consts.OffiaccountConsts;
import com.zxdmy.excite.offiaccount.builder.MsgBuilder;
import com.zxdmy.excite.offiaccount.builder.TextBuilder;
import com.zxdmy.excite.offiaccount.service.IOffiaccountCommonService;
import com.zxdmy.excite.ums.entity.UmsMpReply;
import com.zxdmy.excite.ums.service.IUmsMpReplyService;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author 拾年之璐
 * @since 2022/6/29 17:01
 */
@Component
@AllArgsConstructor
public class EventScanHandler extends AbstractHandler {

    private IUmsMpReplyService mpReplyService;

    IOffiaccountCommonService commonService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        // 处理登录逻辑
        commonService.saveUserByScanLogin2Redis(wxMessage.getEventKey(), wxMessage.getFromUser());
        // 获取登录成功后返回的内容
        UmsMpReply mpReply = mpReplyService.getOneReplyByTypeOrKey(OffiaccountConsts.ReplyType.SCAN_LOGIN_REPLY, null);
        // 构造返回的消息
        WxMpXmlOutMessage outMessage = new MsgBuilder().build(mpReply, wxMessage, wxMpService);
        // 异步调用：事件消息入库
        commonService.saveEvent2DB(wxMessage, mpReply, outMessage);
        return outMessage;
    }
}
